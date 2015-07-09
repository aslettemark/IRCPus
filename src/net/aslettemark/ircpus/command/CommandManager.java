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

package net.aslettemark.ircpus.command;

import net.aslettemark.ircpus.IRCPus;

import java.util.HashMap;

public class CommandManager {

    private IRCPus pus;
    private HashMap<String, CommandExecutor> executors;

    public CommandManager(IRCPus pus) {
        this.pus = pus;
        this.executors = new HashMap<>();
    }

    /**
     * Creates an entry for the specified command with the specified CommandExecutor
     * @param command The command to register
     * @param executor The CommandExecutor to associate with the command
     */
    public void registerCommand(String command, CommandExecutor executor) {
        this.executors.put(command, executor);
    }

    public CommandExecutor getExecutor(String command) {
        return this.executors.get(command);
    }

}
