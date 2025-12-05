package me.fullidle.ficore.ficore.common.api.inventory.actions;

import me.fullidle.ficore.ficore.common.api.inventory.InvButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface InvAction {
    default void run(InventoryClickEvent event, InvButton button, @Nullable Iterator<InvAction> nextRunList) {
        run(event,button);
        if (nextRunList != null && nextRunList.hasNext()) nextRunList.next().run(event, button, nextRunList);
    }

    void run(InventoryClickEvent event, InvButton button);
}
