package net.aslettemark.ircpus;

import net.aslettemark.ircpus.config.Config;
import net.aslettemark.ircpus.config.ConnectionConfig;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.ClientBuilder;
import org.kitteh.irc.client.library.util.Sanity;

import java.io.File;
import java.util.HashMap;

public class IRCPus {

    private Client client;
    private HashMap<String, Config> configs = new HashMap<>();
    private ConnectionConfig connectionConfig;

    public IRCPus() {
        File connection = new File(Strings.CONFIG_CONNECTION);
        this.configs.put(Strings.CONFIG_CONNECTION, new ConnectionConfig(connection));
        this.connectionConfig = (ConnectionConfig) this.configs.get(Strings.CONFIG_CONNECTION);

        String nick = this.connectionConfig.fetchString("nickname");
        String server = this.connectionConfig.fetchString("server");
        Sanity.nullCheck(nick, Strings.ERROR_BAD_CONNECTION_CONFIG);
        Sanity.nullCheck(server, Strings.ERROR_BAD_CONNECTION_CONFIG);
        this.client = new ClientBuilder().nick(nick).server(server).build();
        this.client.addChannel("#aksels");
        this.client.sendMessage("#aksels", "lel");
    }

}
