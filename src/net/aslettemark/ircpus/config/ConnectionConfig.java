package net.aslettemark.ircpus.config;

import net.aslettemark.ircpus.Strings;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class ConnectionConfig extends HashMap implements Config {

    private File file;

    public ConnectionConfig(File c) {
        this.file = c;
        this.load();
    }

    public String fetchString(String key) {
        return (String) this.fetch(key);
    }

    public void set(String key, Object value) {
        this.put(key, value);
    }

    public Object fetch(String key) {
        return this.get(key);
    }

    public void wipe(String key, Object value) {
        this.put(key, "");
    }

    public void wipe() {
        this.clear();
    }

    public HashMap<String, Object> getSaveValues() {
        return this;
    }

    public void load() {
        Yaml yaml = new Yaml();
        HashMap<String, String> values;

        try {
            values = (HashMap<String, String>) yaml.load(new FileInputStream(this.file));

            for (String key : values.keySet()) {
                System.out.print(key + ": ");
                System.out.println(values.get(key));
                this.put(key, values.get(key));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Creating connection config file. Edit before restarting bot.");
            try {
                Files.copy(ConnectionConfig.class.getResourceAsStream("/" + Strings.CONFIG_CONNECTION), this.file.toPath());
            } catch (IOException e1) {
                System.out.println("Failed to write file: " + Strings.CONFIG_CONNECTION);
                System.exit(1);
            }
            System.exit(0);
        }
    }

    public void setSaveLocation(File file) {
        this.file = file;
    }

    public File getSaveLocation() {
        return file;
    }
}