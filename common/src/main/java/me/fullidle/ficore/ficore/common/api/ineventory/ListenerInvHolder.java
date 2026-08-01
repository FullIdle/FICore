package me.fullidle.ficore.ficore.common.api.ineventory;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.function.Consumer;

@Getter
public abstract class ListenerInvHolder extends ListenerInventoryHolder {
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

    @Override
    public void onClick(InventoryClickEvent evt) {
        if (this.click != null) this.click.accept(evt);
    }

    @Override
    public void onClose(InventoryCloseEvent evt) {
        if (this.close != null) this.close.accept(evt);
    }

    @Override
    public void onOpen(InventoryOpenEvent evt) {
        if (this.open != null) this.open.accept(evt);
    }

    @Override
    public void onDrag(InventoryDragEvent evt) {
        if (this.drag != null) this.drag.accept(evt);
    }
}
