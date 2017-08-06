package net.aslettemark.ircpus.command;

import net.aslettemark.ircpus.event.CommandEvent;

public class HelpCommand implements CommandExecutor {

    @Override
    public void execute(CommandEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("Available commands: ");
        for (String c : event.getPus().getCommandManager().getCommands()) {
            sb.append(c + ", ");
        }
        String message = sb.toString();
        event.getFeedbackReceiver().sendMessage(message.substring(0, message.length() - 2));
    }
}
