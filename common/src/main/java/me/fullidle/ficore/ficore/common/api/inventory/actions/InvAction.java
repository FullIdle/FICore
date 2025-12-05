package me.fullidle.ficore.ficore.common.api.inventory.actions;

import me.fullidle.ficore.ficore.common.api.inventory.InvButton;
import me.fullidle.ficore.ficore.common.api.inventory.transformers.InvTransformer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface InvAction {
    default void run(InventoryClickEvent event, InvButton button, @NotNull InvTransformer transformer, @Nullable Iterator<InvAction> nextRunList) {
        run(event, button, transformer);
        if (nextRunList != null && nextRunList.hasNext())
            nextRunList.next().run(event, button, transformer, nextRunList);
    }

    void run(InventoryClickEvent event, InvButton button, @NotNull InvTransformer transformer);
}
