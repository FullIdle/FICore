package me.fullidle.ficore.ficore.common.api.inventory.actions.commands;

import me.fullidle.ficore.ficore.common.api.inventory.InvButton;
import me.fullidle.ficore.ficore.common.api.inventory.actions.HeadMatcher;
import me.fullidle.ficore.ficore.common.api.inventory.actions.InvAction;
import me.fullidle.ficore.ficore.common.api.inventory.actions.InvActionFactory;
import me.fullidle.ficore.ficore.common.api.inventory.transformers.InvTransformer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class OpAction extends CommandAction{
    public OpAction(String command) {
        super(command);
    }

    @Override
    public void exec(InventoryClickEvent event, CommandSender sender, InvButton button, @NotNull InvTransformer transformer) {
        if (sender.isOp()) {
            super.exec(event, sender, button, transformer);
            return;
        }
        try {
            sender.setOp(true);
            super.exec(event, sender, button, transformer);
            sender.setOp(false);
        } catch (Exception e) {
            sender.setOp(false);
            throw new RuntimeException();
        } finally {
            sender.setOp(false);
        }
    }

    public static class Factory implements InvActionFactory {
        public static Factory INSTANCE = new Factory();
        public static HeadMatcher MATCHER = new HeadMatcher("op");

        @Override
        public InvAction create(Object obj) {
            if (!(obj instanceof String) || !MATCHER.test(((String) obj)))
                throw new IllegalArgumentException("Invalid object type for OpActionFactory: " + obj.getClass().getName());
            return new OpAction(MATCHER.cropped(((String) obj)));
        }
    }
}
