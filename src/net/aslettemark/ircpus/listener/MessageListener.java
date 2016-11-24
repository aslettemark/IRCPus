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

package net.aslettemark.ircpus.listener;

import net.aslettemark.ircpus.IRCPus;
import net.aslettemark.ircpus.element.Note;
import net.aslettemark.ircpus.event.CommandEvent;
import org.kitteh.irc.client.library.element.MessageReceiver;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.helper.MessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;

import java.util.ArrayList;

public class MessageListener {

    private IRCPus pus;

    public MessageListener(IRCPus pus) {
        this.pus = pus;
    }

    @Handler
    public void onChannelMessage(ChannelMessageEvent event) {
        String message = event.getMessage();

        if (this.pus.getNoteHandler().hasNotes(event.getActor().getNick())) {
            this.sendNotes(event.getActor(), event.getChannel());
        }

        if (message.startsWith(".") && message.length() > 1) {
            String command = message.replaceFirst(".", "");
            if (this.pus.getCommandManager().getExecutor(command.split(" ")[0].toLowerCase()) != null) {
                pus.client.getEventManager().callEvent(new CommandEvent(this.pus.client, event.getActor(), event.getChannel(), command, this.pus));
            }
        }
    }

    @Handler
    public void onPrivateMessage(PrivateMessageEvent event) {
        if (this.pus.getNoteHandler().hasNotes(event.getActor().getNick())) {
            this.sendNotes(event.getActor(), event.getActor());
        }

        if (this.pus.getCommandManager().getExecutor(event.getMessage().split(" ")[0].toLowerCase()) != null) {
            this.pus.client.getEventManager().callEvent(new CommandEvent(this.pus.client, event.getActor(), null, event.getMessage(), this.pus));
        }
    }

    @Handler
    public void onMessage(MessageEvent event) {

    }

    /**
     * @param user     The target of the notes
     * @param receiver The MessageReceiver to print the notes to
     */
    public void sendNotes(User user, MessageReceiver receiver) {
        ArrayList<Note> notes = this.pus.getNoteHandler().getNotes(user);
        receiver.sendMessage(user.getNick() + ": You have notes!");
        if (notes.size() > 4 && user != receiver) {
            receiver.sendMessage("Too many notes! Sending in a private message!");
            receiver = user;
        }
        for (Note n : notes) {
            receiver.sendMessage(user.getNick() + ": " + n.getPrintOut());
            this.pus.getNoteHandler().removeNote(n);
        }
    }
}
