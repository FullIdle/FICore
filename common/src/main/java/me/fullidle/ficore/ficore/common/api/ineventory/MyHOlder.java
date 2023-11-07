package me.fullidle.ficore.ficore.common.api.ineventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Function;

public class MyHOlder extends ListenerInvHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }
}
