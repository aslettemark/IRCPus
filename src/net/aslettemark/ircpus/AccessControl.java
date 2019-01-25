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

import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.util.Sanity;

import java.util.HashSet;
import java.util.Set;

public class AccessControl {

    private Set<String> admins;

    public AccessControl() {
        this.admins = new HashSet<>();
    }

    /**
     * Determines if an account has admin access
     *
     * @param candidate The account/channel to test
     * @return Whether the candidate has admin access to the "pusekatt"
     */
    public boolean isAdmin(String candidate) {
        return this.admins.contains(candidate);
    }

    /**
     * Determines if a User has admin access
     *
     * @param user The User to test
     * @return Whether the User has admin access to the "pusekatt"
     */
    public boolean isAdmin(User user) {
        return user != null && user.getAccount().isPresent() && this.isAdmin(user.getAccount().get());

    }

    /**
     * Determines if a Channel has admin access
     *
     * @param channel The Channel to test
     * @return Whether the channel is marked as an admin channel
     */
    public boolean isAdmin(Channel channel) {
        return channel != null && this.isAdmin(channel.getName());
    }

    /**
     * @param admin The account to give admin rights
     */
    public void addAdmin(String admin) {
        Sanity.nullCheck(admin, "Cannot give admin rights to null");
        this.admins.add(admin);
    }

    /**
     * @return A Set of all admins: accounts and channels
     */
    public Set<String> getAdmins() {
        return this.admins;
    }

}
