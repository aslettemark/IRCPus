/*
 *  Copyright (C) 2015-2019 Aksel H. Slettemark http://aslettemark.net/
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
import net.aslettemark.ircpus.config.ConnectionConfig;
import net.aslettemark.ircpus.config.JsonConnectionConfig;
import net.aslettemark.ircpus.listener.CommandListener;
import net.aslettemark.ircpus.listener.MessageListener;
import org.json.JSONException;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.feature.auth.NickServ;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IRCPus {

    private Client client;

    private ConnectionConfig connectionConfig;
    private AccessControl accessControl;
    private NoteHandler noteHandler;

    private Map<String, CommandExecutor> commandExecutors = new HashMap<>();

    public IRCPus(String connectionFile) {
        try {
            this.connectionConfig = new JsonConnectionConfig(connectionFile);
        } catch (JSONException je) {
            log("Malformed connection config file: " + connectionFile);
            je.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            log("Error reading connection config file: " + connectionFile);
            e.printStackTrace();
            System.exit(1);
        }

        String nick = this.connectionConfig.getIntendedNick();
        String server = this.connectionConfig.getServerHostName();

        this.client = Client.builder()
                .nick(nick)
                .server()
                .host(server)
                .secure(true)
                .then()
                .buildAndConnect();

        Optional<String> nsNick = this.connectionConfig.getNickServUserName();
        Optional<String> nsPass = this.connectionConfig.getNickServPassword();
        if (nsNick.isPresent() && nsPass.isPresent()) {
            this.client.getAuthManager().addProtocol(
                    NickServ.builder(client)
                            .account(nsNick.get())
                            .password(nsPass.get())
                            .build()
            );
        }

        for (String s : this.connectionConfig.getIntendedChannels()) {
            this.client.addChannel(s);
            log("Added " + s);
        }

        this.accessControl = new AccessControl();
        for (String s : this.connectionConfig.getAdmins()) {
            this.accessControl.addAdmin(s);
            log("Added " + s + " as an admin " + (s.startsWith("#") ? "(Channel)" : "(User)"));
        }

        this.client.getEventManager().registerEventListener(new MessageListener(this));
        this.client.getEventManager().registerEventListener(new CommandListener(this));

        commandExecutors.put("ping", new PingCommand());
        commandExecutors.put("note", new NoteCommand());
        commandExecutors.put("whoami", new WhoAmICommand());
        commandExecutors.put("join", new JoinCommand());
        commandExecutors.put("part", new PartCommand());
        commandExecutors.put("quit", new QuitCommand());
        commandExecutors.put("help", new HelpCommand());


        this.noteHandler = new NoteHandler(server + ".notes");
    }

    /**
     * @param msg The message to log
     */
    public static void log(String msg) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println("[" + ts.toString().split(" ")[1].substring(0, 8) + "] " + msg); //Haha I know how terrible this is
    }

    /**
     * @return The access control class
     */
    public AccessControl getAccessControl() {
        return accessControl;
    }

    public NoteHandler getNoteHandler() {
        return noteHandler;
    }

    public Client getClient() {
        return this.client;
    }

    public Map<String, CommandExecutor> getCommandExecutorMap() {
        return this.commandExecutors;
    }

}
