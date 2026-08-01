package me.fullidle.ficore.ficore.common.api.ineventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public abstract class ListenerInventoryHolder implements InventoryHolder {
    abstract public void onClick(InventoryClickEvent evt);

    abstract public void onClose(InventoryCloseEvent evt);

    abstract public void onOpen(InventoryOpenEvent evt);

    abstract public void onDrag(InventoryDragEvent evt);
}
