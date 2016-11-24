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

package net.aslettemark.ircpus;

import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.util.Sanity;

import java.util.HashSet;

public class AccessControl {

    private IRCPus pus;
    private HashSet<String> admins;

    public AccessControl(IRCPus pus) {
        this.pus = pus;
        this.admins = new HashSet<>();
    }

    /**
     * Determines if an account has admin access
     *
     * @param account The account to test
     * @return Whether the account has admin access to the "pusekatt"
     */
    public boolean isAdmin(String account) {
        return this.admins.contains(account);
    }

    /**
     * Determines if a User has admin access
     *
     * @param user The User to test
     * @return Whether the User has admin access to the "pusekatt"
     */
    public boolean isAdmin(User user) {
        if (user == null) {
            return false;
        }

        return this.isAdmin(user.getAccount().get());
    }

    /**
     * Determines if a Channel has admin access
     *
     * @param channel The Channel to test
     * @return Whether the channel is marked as an admin channel
     */
    public boolean isAdmin(Channel channel) {
        if (channel == null) {
            return false;
        }
        return this.isAdmin(channel.getName());
    }

    /**
     * @param admin The account to give admin rights
     */
    public void addAdmin(String admin) {
        Sanity.nullCheck(admin, "Cannot give admin rights to null");
        this.admins.add(admin);
    }

    /**
     * @param channel The channel to mark as an admin channel
     */
    public void addAdmin(Channel channel) {
        Sanity.nullCheck(channel, "Cannot give admin rights to null");
        this.admins.add(channel.getName());
    }

    /**
     * @param user The user to give admin rights
     */
    public void addAdmin(User user) {
        Sanity.nullCheck(user.getAccount(), "Cannot give admin rights to users without accounts");
        this.admins.add(user.getAccount().get());
    }

    /**
     * @return A HashSet of all admins, accounts and channels
     */
    public HashSet<String> getAdmins() {
        return this.admins;
    }

}
