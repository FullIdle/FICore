package me.fullidle.ficore.ficore.common.api.inventory.transformers;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.inventory.InvButton;
import me.fullidle.ficore.ficore.common.api.inventory.InvConfig;
import me.fullidle.ficore.ficore.common.api.inventory.actions.InvAction;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface InvTransformer {
    InvTransformer NO_OPERATION = new InvTransformer(){};

    /**
     * 需要有字符串提供的动作所调用
     */
    @Deprecated
    default String action(InventoryClickEvent event, InvButton button, InvAction action, String needTransformed) {
        return needTransformed;
    }

    default String title(@Nullable InvConfig config, @Nullable InventoryHolder invHolder, @Nullable OfflinePlayer papiTarget, String title) {
        return title;
    }

    default ItemStack button(InvConfig config, InventoryHolder holder, InvButton invButton, OfflinePlayer papiTarget) {
        val icon = invButton.getIcon();
        val itemMeta = icon.getItemMeta();
        if (itemMeta == null) return icon;
        if (itemMeta.hasDisplayName())
            itemMeta.setDisplayName(buttonStr(config, holder, invButton, papiTarget, itemMeta.getDisplayName()));
        if (itemMeta.hasLore()) {
            val lore = new ArrayList<>(itemMeta.getLore());
            lore.replaceAll(s -> buttonStr(config, holder, invButton, papiTarget, s));
            itemMeta.setLore(lore);
        }
        icon.setItemMeta(itemMeta);
        return icon;
    }

    default String buttonStr(InvConfig config, InventoryHolder holder, InvButton invButton, OfflinePlayer papiTarget, String needTransformed) {
        return needTransformed;
    }
}
