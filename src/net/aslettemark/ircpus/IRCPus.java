/*
 *  Copyright (C) 2015 Aksel H. Slettemark http://aslettemark.net/
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

import net.aslettemark.ircpus.command.CommandManager;
import net.aslettemark.ircpus.command.PingCommand;
import net.aslettemark.ircpus.config.Config;
import net.aslettemark.ircpus.config.ConnectionConfig;
import net.aslettemark.ircpus.listener.CommandListener;
import net.aslettemark.ircpus.listener.MessageListener;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.util.AcceptingTrustManagerFactory;
import org.kitteh.irc.client.library.util.Sanity;

import java.io.File;
import java.util.HashMap;

public class IRCPus {

    public Client client;
    private HashMap<String, Config> configs = new HashMap<>();
    private ConnectionConfig connectionConfig;
    private CommandManager commandManager;
    private AccessControl accessControl;

    public IRCPus() {
        File connection = new File(Strings.CONFIG_CONNECTION);
        this.configs.put(Strings.CONFIG_CONNECTION, new ConnectionConfig(connection));
        this.connectionConfig = (ConnectionConfig) this.configs.get(Strings.CONFIG_CONNECTION);

        String nick = this.connectionConfig.fetchString(Strings.CONFIG_KEY_NICKNAME);
        String server = this.connectionConfig.fetchString(Strings.CONFIG_KEY_SERVER);
        Sanity.nullCheck(nick, Strings.ERROR_BAD_CONNECTION_CONFIG);
        Sanity.nullCheck(server, Strings.ERROR_BAD_CONNECTION_CONFIG);
        this.client = Client.builder().nick(nick).serverHost(server).secureTrustManagerFactory(new AcceptingTrustManagerFactory()).build();

        for(String s : ((String) this.connectionConfig.get(Strings.CONFIG_KEY_CHANNELS)).split(", ")) {
            this.client.addChannel(s.replace("\\", ""));
            System.out.println("Added " + s.replace("\\", ""));
        }

        this.client.getEventManager().registerEventListener(new MessageListener(this));
        this.client.getEventManager().registerEventListener(new CommandListener(this));

        this.commandManager = new CommandManager(this);
        this.getCommandManager().registerCommand("ping", new PingCommand());

        this.accessControl = new AccessControl(this);
    }

    /**
     * @return The command manager
     */
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    /**
     * @return The access control class
     */
    public AccessControl getAccessControl() {
        return accessControl;
    }

    public Config getConfig(String file) {
        return this.configs.get(file);
    }
}
