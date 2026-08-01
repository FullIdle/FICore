package me.fullidle.ficore.ficore.listener;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInventoryHolder;
import me.fullidle.ficore.ficore.common.api.inventory.InvHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

public class PlayerListener implements Listener {
    public static final PlayerListener INSTANCE = new PlayerListener();

    public void register() {
        val plugin = FIData.plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof ListenerInventoryHolder) {
            ((ListenerInventoryHolder) holder).onOpen(e);
            return;
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof ListenerInventoryHolder) {
            ((ListenerInventoryHolder) holder).onClick(e);
            return;
        }

        if (holder instanceof InvHolder) {
            ((InvHolder) holder).onClick(e);
            return;
        }
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof ListenerInventoryHolder) {
            ((ListenerInventoryHolder) holder).onDrag(e);
            return;
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof ListenerInventoryHolder) {
            ((ListenerInventoryHolder) holder).onClose(e);
            return;
        }
        if (holder instanceof InvHolder) {
            ((InvHolder) holder).onClose(e);
        }
    }
}
