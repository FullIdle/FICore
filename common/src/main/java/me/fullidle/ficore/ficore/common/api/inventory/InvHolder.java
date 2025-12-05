package me.fullidle.ficore.ficore.common.api.inventory;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InvHolder implements InventoryHolder {
    @Getter
    private final InvConfig config;
    private final Inventory inventory;

    public InvHolder(InvConfig config, @Nullable OfflinePlayer papiTarget) {
        this.config = config;
        this.inventory = config.createInv(this, papiTarget);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!this.getInventory().equals(e.getClickedInventory()) || e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
            return;
        InvButton button = this.getConfig().getButton(e.getSlot());
        if (button == null) return;
        button.onClick(e, this.config.getTransformer());
    }
}
