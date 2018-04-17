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

package net.aslettemark.ircpus.listener;

import net.aslettemark.ircpus.IRCPus;
import net.aslettemark.ircpus.config.ConnectionConfig;
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;

public class ConnectionListener {

    private IRCPus pus;

    public ConnectionListener(IRCPus pus) {
        this.pus = pus;
    }

    @Handler
    public void onConnected(ClientConnectedEvent event) {
        IRCPus.log("Connected to " + this.pus.getClient().getServerInfo().getAddress().get());
        ConnectionConfig cc = this.pus.getConnectionConfig();
        if (cc.getNickServPassword().isPresent() && cc.getNickServUserName().isPresent()) {
            String nick = cc.getNickServUserName().get();
            String pass = cc.getNickServPassword().get();
            IRCPus.log("Attempting NickServ authentication");
            this.pus.nickServAuth(nick, pass);
        } else {
            IRCPus.log("Skipping NickServ authentication");
        }
    }
}
