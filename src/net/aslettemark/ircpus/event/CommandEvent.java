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

package net.aslettemark.ircpus.event;

import net.aslettemark.ircpus.IRCPus;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.MessageReceiver;
import org.kitteh.irc.client.library.element.User;

public class CommandEvent {

    private Client client;
    private User user;
    private String command;
    private IRCPus pus;
    private Channel channel;
    private MessageReceiver feedbackReceiver;
    private boolean privileged;

    public CommandEvent(Client client, User user, Channel channel, String command, IRCPus pus) {
        this.client = client;
        this.user = user;
        this.command = command;
        this.pus = pus;
        this.channel = channel;
        this.feedbackReceiver = channel;
        this.privileged = false;
        if (channel == null) {
            this.feedbackReceiver = user;
        }
        if (this.pus.getAccessControl().isAdmin(channel) || this.pus.getAccessControl().isAdmin(user)) {
            this.privileged = true;
        }
    }

    public String getCommand() {
        return command;
    }

    public Client getClient() {
        return client;
    }

    public User getUser() {
        return user;
    }

    public User getActor() {
        return this.getUser();
    }

    public IRCPus getPus() {
        return pus;
    }

    public Channel getChannel() {
        return channel;
    }

    public MessageReceiver getFeedbackReceiver() {
        return feedbackReceiver;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    @Override
    public String toString() {
        return user.getNick() + " in " + channel.getName() + ": " + command;
    }
}
