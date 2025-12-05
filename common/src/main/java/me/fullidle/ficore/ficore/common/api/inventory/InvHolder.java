package me.fullidle.ficore.ficore.common.api.inventory;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InvHolder implements InventoryHolder {
    @Getter
    private final InvConfig config;
    private Inventory inventory;

    public InvHolder(InvConfig config, @Nullable OfflinePlayer player) {
        this.config = config;
        this.init(player);
    }

    /**
     * 没有初始化的构造
     */
    public InvHolder(InvConfig config) {
        this.config = config;
    }

    public void init(@Nullable OfflinePlayer player) {
        this.inventory = config.createInv(this, player);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return Objects.requireNonNull(this.inventory, "Inventory is not initialized yet!");
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
