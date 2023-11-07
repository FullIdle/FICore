package me.fullidle.ficore.ficore.listener;

import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class PlayerListener implements Listener {
    @EventHandler
    public void onInvOpen(InventoryOpenEvent e){
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof ListenerInvHolder))return;
        ListenerInvHolder invHolder = (ListenerInvHolder) holder;
        invHolder.getOpen().accept(e);
    }
    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof ListenerInvHolder))return;
        ListenerInvHolder invHolder = (ListenerInvHolder) holder;
        invHolder.getClick().accept(e);
    }
    @EventHandler
    public void onInvDrag(InventoryDragEvent e){
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof ListenerInvHolder))return;
        ListenerInvHolder invHolder = (ListenerInvHolder) holder;
        invHolder.getDrag().accept(e);
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof ListenerInvHolder))return;
        ListenerInvHolder invHolder = (ListenerInvHolder) holder;
        invHolder.getClose().accept(e);
    }
}
