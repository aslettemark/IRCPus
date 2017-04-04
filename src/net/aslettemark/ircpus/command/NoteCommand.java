/*
 *  Copyright (C) 2015-2017 Aksel H. Slettemark http://aslettemark.net/
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

package net.aslettemark.ircpus.command;

import net.aslettemark.ircpus.event.CommandEvent;

public class NoteCommand implements CommandExecutor {

    @Override
    public void execute(CommandEvent event) {
        String[] split = event.getCommand().split(" ");
        if (!(split.length > 2)) {
            event.getUser().sendMessage("Syntax: note <target> <note>");
            return;
        }
        final String target = split[1].toLowerCase();
        final String[] targets = target.split(",");
        final String content = event.getCommand().replaceFirst("note " + event.getCommand().split(" ")[1] + " ", "");

        //Check for max nick length
        int maxNickLength = event.getPus().getClient().getServerInfo().getNickLengthLimit();
        for (String t : targets) {
            if (t.length() > maxNickLength) {
                event.getUser().sendMessage("The maximum nick length limit on this server is " + maxNickLength);
                return;
            }
        }

        //Check for illegal strings
        final String[] disallowed = {"\n", "\r"};
        for (final String rule : disallowed) {
            if (event.getCommand().contains(rule)) {
                event.getUser().sendMessage("Your note contains a disallowed character");
                return;
            }
        }

        //Add the note to the target(s)
        for (String t : targets) {
            event.getPus().getNoteHandler().addNote(event.getUser().getNick(), t, content);
        }
        event.getFeedbackReceiver().sendMessage(event.getActor().getNick() + ": Let me write that down for you.");
    }
}
