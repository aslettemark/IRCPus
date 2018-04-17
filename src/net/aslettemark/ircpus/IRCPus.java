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

package net.aslettemark.ircpus;

import net.aslettemark.ircpus.command.*;
import net.aslettemark.ircpus.config.Config;
import net.aslettemark.ircpus.config.ConnectionConfig;
import net.aslettemark.ircpus.listener.CommandListener;
import net.aslettemark.ircpus.listener.ConnectionListener;
import net.aslettemark.ircpus.listener.MessageListener;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.util.AcceptingTrustManagerFactory;
import org.kitteh.irc.client.library.util.Sanity;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class IRCPus {

    private Client client;

    private Map<String, Config> configs = new HashMap<>();
    private ConnectionConfig connectionConfig;
    private CommandManager commandManager;
    private AccessControl accessControl;
    private NoteHandler noteHandler;

    public IRCPus() {
        File connection = new File(Strings.CONFIG_CONNECTION);
        this.configs.put(Strings.CONFIG_CONNECTION, new ConnectionConfig(connection));
        this.connectionConfig = (ConnectionConfig) this.configs.get(Strings.CONFIG_CONNECTION);

        String nick = this.connectionConfig.fetchString(Strings.CONFIG_KEY_NICKNAME);
        String server = this.connectionConfig.fetchString(Strings.CONFIG_KEY_SERVER);
        Sanity.nullCheck(nick, Strings.ERROR_BAD_CONNECTION_CONFIG);
        Sanity.nullCheck(server, Strings.ERROR_BAD_CONNECTION_CONFIG);

        Client.Builder builder = Client.builder();
        builder.nick(nick);
        builder.serverHost(server);
        builder.secureTrustManagerFactory(new AcceptingTrustManagerFactory());

        this.client = builder.build();

        for (String s : ((String) this.connectionConfig.get(Strings.CONFIG_KEY_CHANNELS)).replaceAll(" ", "").split(",")) {
            this.client.addChannel(s.replace("\\", ""));
            log("Added " + s.replace("\\", ""));
        }

        this.accessControl = new AccessControl(this);
        for (String s : ((String) this.connectionConfig.get(Strings.CONFIG_KEY_ADMINS)).replaceAll(" ", "").split(",")) {
            this.accessControl.addAdmin(s);
            String fb = "Added " + s + " as an admin " + (s.startsWith("#") ? "(Channel)" : "(User)");
            log(fb);
        }

        this.client.getEventManager().registerEventListener(new MessageListener(this));
        this.client.getEventManager().registerEventListener(new CommandListener(this));
        this.client.getEventManager().registerEventListener(new ConnectionListener(this));

        this.commandManager = new CommandManager(this);
        this.getCommandManager().registerCommand("ping", new PingCommand());
        this.getCommandManager().registerCommand("note", new NoteCommand());
        this.getCommandManager().registerCommand("whoami", new WhoAmICommand());
        this.getCommandManager().registerCommand("join", new JoinCommand());
        this.getCommandManager().registerCommand("part", new PartCommand());
        this.getCommandManager().registerCommand("quit", new QuitCommand());
        this.getCommandManager().registerCommand("help", new HelpCommand());
        //this.getCommandManager().registerCommand("lol", e -> e.getFeedbackReceiver().sendMessage("lol"));

        this.noteHandler = new NoteHandler(this, server + ".notes");
    }

    /**
     * @param msg The message to log
     */
    public static void log(String msg) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println("[" + ts.toString().split(" ")[1].substring(0, 8) + "] " + msg); //Haha I know how terrible this is
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

    public NoteHandler getNoteHandler() {
        return noteHandler;
    }

    public ConnectionConfig getConnectionConfig() {
        return this.connectionConfig;
    }

    public void nickServAuth(String username, String password) {
        this.client.sendMessage("nickserv", "identify " + username + " " + password);
    }

    public Client getClient() {
        return this.client;
    }

}
