package me.fullidle.ficore.ficore.common.api.inventory.actions;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.inventory.InvButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CommandAction implements InvAction {
    private final String command;

    public CommandAction(String command){
        this.command = command;
    }

    @Override
    public void run(InventoryClickEvent event, InvButton button) {
        HumanEntity whoClicked = event.getWhoClicked();
        if (!(whoClicked instanceof Player)) {
            FIData.plugin.getLogger().warning("A non-player entity tried to run a command!");
            return;
        }
        Bukkit.dispatchCommand(whoClicked, this.command);
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
