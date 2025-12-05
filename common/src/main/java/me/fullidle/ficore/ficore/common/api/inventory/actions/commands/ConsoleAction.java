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

public class ConsoleAction extends CommandAction{
    public ConsoleAction(String command) {
        super(command);
    }

    @Override
    public void exec(InventoryClickEvent event, CommandSender sender, InvButton button, @NotNull InvTransformer transformer) {
        super.exec(event, Bukkit.getConsoleSender(), button, transformer);
    }

    public static class Factory implements InvActionFactory {
        public static Factory INSTANCE = new Factory();
        public static HeadMatcher MATCHER = new HeadMatcher("console");

        @Override
        public InvAction create(Object obj) {
            if (!(obj instanceof String) || !MATCHER.test(((String) obj)))
                throw new IllegalArgumentException("Invalid object type for ConsoleActionFactory: " + obj.getClass().getName());
            return new ConsoleAction(MATCHER.cropped(((String) obj)));
        }
    }
}
