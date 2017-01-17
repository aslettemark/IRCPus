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

package net.aslettemark.ircpus.command;

import net.aslettemark.ircpus.event.CommandEvent;
import org.kitteh.irc.client.library.element.MessageReceiver;
import org.kitteh.irc.client.library.element.User;

import java.util.Optional;

public class WhoAmICommand implements CommandExecutor {

    @Override
    public void execute(CommandEvent event) {
        User user = event.getActor();
        String nick = user.getNick();
        String userString = user.getUserString();
        Optional<String> account = user.getAccount();
        String acc = "Not logged in.";
        if (account.get() != null) {
            acc = account.get();
        }

        MessageReceiver mr = event.getFeedbackReceiver();
        mr.sendMessage("Nickname: " + nick);
        mr.sendMessage("User: " + userString);
        mr.sendMessage("Account: " + acc);
    }
}
