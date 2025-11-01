package me.fullidle.ficore.ficore.common.api.ineventory;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

@Getter
public abstract class ListenerInvHolder implements InventoryHolder {
    private Consumer<InventoryClickEvent> click;
    private Consumer<InventoryDragEvent> drag;
    private Consumer<InventoryOpenEvent> open;
    private Consumer<InventoryCloseEvent> close;

    public void onClick(Consumer<InventoryClickEvent> consumer) {
        this.click = consumer;
    }

    public void onClose(Consumer<InventoryCloseEvent> consumer) {
        this.close = consumer;
    }

    public void onOpen(Consumer<InventoryOpenEvent> consumer) {
        this.open = consumer;
    }

    public void onDrag(Consumer<InventoryDragEvent> consumer) {
        this.drag = consumer;
    }
}
