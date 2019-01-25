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

package net.aslettemark.ircpus.config;

import net.aslettemark.ircpus.FileUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonConnectionConfig implements ConnectionConfig {

    private String nick;
    private String server;
    private List<String> admins = new ArrayList<>();
    private List<String> channels = new ArrayList<>();
    private String nickservUser;
    private String nickservPass;

    public JsonConnectionConfig(String filename) throws IOException, JSONException {
        String contents = FileUtil.toSingleLine(FileUtil.readFile(filename));
        JSONObject root = new JSONObject(contents);

        this.nick = root.getString("nickname");
        this.server = root.getString("server");

        JSONArray admins = root.getJSONArray("admins");
        for (int i = 0; i < admins.length(); i++) {
            this.admins.add(admins.getString(i));
        }

        JSONArray chans = root.getJSONArray("channels");
        for (int i = 0; i < chans.length(); i++) {
            this.channels.add(chans.getString(i));
        }

        String user;
        String pass;
        try {
            user = root.getString("nickserv-username");
            pass = root.getString("nickserv-password");
        } catch (JSONException je) {
            //One or more not present, don't know which
            user = null;
            pass = null;
        }
        //Legacy values for not authing
        if (user != null && !user.equalsIgnoreCase("DONOTAUTH") && !pass.equalsIgnoreCase("DONOTAUTH")) {
            this.nickservUser = user;
            this.nickservPass = pass;
        }
    }

    @Override
    public String getIntendedNick() {
        return this.nick;
    }

    @Override
    public String getServerHostName() {
        return this.server;
    }

    @Override
    public List<String> getIntendedChannels() {
        return this.channels;
    }

    @Override
    public List<String> getAdmins() {
        return this.admins;
    }

    @Override
    public Optional<String> getNickServUserName() {
        return Optional.ofNullable(this.nickservUser);
    }

    @Override
    public Optional<String> getNickServPassword() {
        return Optional.ofNullable(this.nickservPass);
    }
}
