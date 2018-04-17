/*
 *  Copyright (C) 2015-2018 Aksel H. Slettemark http://aslettemark.net/
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

import net.aslettemark.ircpus.event.CommandEvent;
import org.kitteh.irc.client.library.element.MessageReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartCommand implements CommandExecutor {

    @Override
    public void execute(CommandEvent event) {
        MessageReceiver mr = event.getFeedbackReceiver();
        String[] split = event.getCommand().split(" ");
        if (!event.isPrivileged()) {
            mr.sendMessage(event.getActor().getNick() + ": I can't let you do that.");
            return;
        }
        if (!(split.length > 1) || !split[1].startsWith("#")) {
            mr.sendMessage(event.getActor().getNick() + ": Syntax: part <channel>");// [-remove]");
            return;
        }
        event.getPus().getClient().removeChannel(split[1]);
        /*if (split.length > 2 && split[2].equalsIgnoreCase("-remove")) {
            List<String> channels = Arrays.asList(((String) event.getPus().getConnectionConfig().get(Strings.CONFIG_KEY_CHANNELS)).replaceAll("\\\\", "").split(", "));
            channels = new ArrayList<>(channels);
            channels.remove(split[1]);
            String save = String.join(", ", channels);
            event.getPus().getConnectionConfig().set(Strings.CONFIG_KEY_CHANNELS, save);
            event.getPus().getConnectionConfig().save();
        }*/
    }

}
