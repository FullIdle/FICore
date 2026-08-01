package me.fullidle.ficore.ficore.common.api.ineventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public abstract class ListenerInventoryHolder implements InventoryHolder {
    public void onClick(InventoryClickEvent evt) {
    }

    public void onClose(InventoryCloseEvent evt) {
    }

    public void onOpen(InventoryOpenEvent evt) {
    }

    public void onDrag(InventoryDragEvent evt) {
    }
}
