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

package net.aslettemark.ircpus.config;

import net.aslettemark.ircpus.Strings;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
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
                System.out.println(key + ": " + values.get(key));
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

    public boolean save() {
        try {
            FileWriter writer = new FileWriter(this.file);
            (new Yaml()).dump(this, writer);
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setSaveLocation(File file) {
        this.file = file;
    }

    public File getSaveLocation() {
        return file;
    }
}