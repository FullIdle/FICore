package me.fullidle.ficore.ficore.common.api.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class Context {
    public CommandSender sender;
    public Command command;
    public Map<String, String> args;

    public Context(CommandSender sender, Command command, Map<String, String> args) {
        this.sender = sender;
        this.command = command;
        this.args = args;
    }
}
