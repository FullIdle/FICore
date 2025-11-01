package me.fullidle.ficore.ficore.listener;

import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

public class PlayerListener implements Listener {
    @EventHandler
    public void onInvOpen(InventoryOpenEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof ListenerInvHolder)) return;
        ListenerInvHolder invHolder = (ListenerInvHolder) holder;
        Consumer<InventoryOpenEvent> consumer = invHolder.getOpen();
        if (consumer != null) {
            consumer.accept(e);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof ListenerInvHolder)) return;
        ListenerInvHolder invHolder = (ListenerInvHolder) holder;
        Consumer<InventoryClickEvent> consumer = invHolder.getClick();
        if (consumer != null) {
            consumer.accept(e);
        }
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof ListenerInvHolder)) return;
        ListenerInvHolder invHolder = (ListenerInvHolder) holder;
        Consumer<InventoryDragEvent> consumer = invHolder.getDrag();
        if (consumer != null) {
            consumer.accept(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof ListenerInvHolder)) return;
        ListenerInvHolder invHolder = (ListenerInvHolder) holder;
        Consumer<InventoryCloseEvent> consumer = invHolder.getClose();
        if (consumer != null) {
            consumer.accept(e);
        }
    }
}
