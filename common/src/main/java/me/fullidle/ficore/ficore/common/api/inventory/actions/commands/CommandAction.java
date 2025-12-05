package me.fullidle.ficore.ficore.common.api.inventory.actions.commands;

import me.fullidle.ficore.ficore.common.api.inventory.InvButton;
import me.fullidle.ficore.ficore.common.api.inventory.actions.HeadMatcher;
import me.fullidle.ficore.ficore.common.api.inventory.actions.InvAction;
import me.fullidle.ficore.ficore.common.api.inventory.actions.InvActionFactory;
import me.fullidle.ficore.ficore.common.api.inventory.transformers.InvTransformer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class CommandAction implements InvAction {
    private final String command;

    public CommandAction(String command){
        this.command = command;
    }

    @Override
    public void run(InventoryClickEvent event, InvButton button, @NotNull InvTransformer transformer) {
        exec(event, event.getWhoClicked(), button, transformer);
    }

    public void exec(InventoryClickEvent event, CommandSender sender, InvButton button, @NotNull InvTransformer transformer) {
        Bukkit.dispatchCommand(sender, transformer.action(event, button, this, command));
    }

    public static class Factory implements InvActionFactory {
        public static Factory INSTANCE = new Factory();
        public static HeadMatcher MATCHER = new HeadMatcher("command");

        @Override
        public InvAction create(Object obj) {
            if (!(obj instanceof String) || !MATCHER.test(((String) obj)))
                throw new IllegalArgumentException("Invalid object type for CommandActionFactory: " + obj.getClass().getName());
            return new CommandAction(MATCHER.cropped(((String) obj)));
        }
    }
}
