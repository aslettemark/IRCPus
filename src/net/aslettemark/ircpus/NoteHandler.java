/*
 *  Copyright (C) 2015-2016 Aksel H. Slettemark http://aslettemark.net/
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.aslettemark.ircpus;

import net.aslettemark.ircpus.element.Note;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.util.StringUtil;

import java.io.*;
import java.util.ArrayList;

public class NoteHandler {

    private IRCPus pus;

    public NoteHandler(IRCPus pus) {
        this.pus = pus;
    }

    public void addNote(String sender, String target, String content) {
        this.pus.notes.add(new Note(sender, target, content));
        this.saveNotes();
    }

    public void removeNote(Note note) {
        this.pus.notes.remove(note);
        this.saveNotes();
    }

    public void saveNotes() {
        this.wipeFile();
        try {
            final FileWriter fileWriter = new FileWriter(Strings.NOTES_FILE);
            final BufferedWriter buffer = new BufferedWriter(fileWriter);
            for (final Note note : this.pus.notes) {
                buffer.write(note.getSender() + " " + note.getTarget() + " " + note.getContent());
                buffer.newLine();
            }
            buffer.close();
            fileWriter.close();
            IRCPus.log("Saved notes.");
        } catch (final IOException e) {
            IRCPus.log("Error saving notes.");
        }
    }

    private void wipeFile() {
        this.validateFile(); //Ironic - validating that it exists before removing its content - it makes my life easier :-)
        FileOutputStream writer;
        try {
            writer = new FileOutputStream(Strings.NOTES_FILE);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(); //Can't really imagine this happening after validation - w/e
        }
    }

    public ArrayList<Note> loadNotes() {
        this.validateFile();
        ArrayList<Note> notes = new ArrayList<>();
        final String fileName = Strings.NOTES_FILE;
        String line;
        try {
            final FileReader reader = new FileReader(fileName);
            final BufferedReader buffer = new BufferedReader(reader);
            while ((line = buffer.readLine()) != null) {
                //Each line is supposed to be: <sender> <target> <content>
                String[] split = line.split(" ");
                notes.add(new Note(split[0], split[1], StringUtil.combineSplit(split, 2)));
            }
            buffer.close();
            reader.close();
        } catch (final FileNotFoundException ex) {
            IRCPus.log("Not able to find file " + fileName);
        } catch (final IOException e) {
            IRCPus.log("Encountered an error reading file " + fileName);
        }
        return notes;
    }

    public boolean hasNotes(String nick) {
        return this.getNotes(nick).size() > 0;
    }

    public boolean hasNotes(User user) {
        return this.hasNotes(user.getNick());
    }

    public ArrayList<Note> getNotes(String nick) {
        ArrayList<Note> notes = new ArrayList<>();
        for (Note n : this.pus.notes) {
            if (n.getTarget().equalsIgnoreCase(nick)) {
                notes.add(n);
            }
        }
        return notes;
    }

    public ArrayList<Note> getNotes(User user) {
        return this.getNotes(user.getNick());
    }

    private void validateFile() {
        final String fileName = Strings.NOTES_FILE;
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                IRCPus.log("Created file: " + fileName);
            } catch (IOException e) {
                IRCPus.log("Error creating file: " + fileName);
            }
        }
    }

}
