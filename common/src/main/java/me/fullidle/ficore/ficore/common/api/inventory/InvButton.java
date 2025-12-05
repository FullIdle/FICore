package me.fullidle.ficore.ficore.common.api.inventory;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.inventory.actions.InvAction;
import me.fullidle.ficore.ficore.common.api.inventory.actions.InvActionFactories;
import me.fullidle.ficore.ficore.common.api.inventory.transformers.InvTransformer;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Logger;

public class InvButton {
    private final ItemStack icon;
    private final Map<ClickType, List<InvAction>> actions = new HashMap<>();

    public InvButton(
            ItemStack icon,
            Map<ClickType, List<InvAction>> actions
    ) {
        this.icon = icon;
        this.actions.putAll(actions);
    }

    public ItemStack getIcon() {
        return icon.clone();
    }

    public ItemStack getIcon(InvConfig config, InventoryHolder holder, OfflinePlayer papiTarget, InvTransformer transformer) {
        return transformer.button(config, holder, this, papiTarget);
    }

    public void onClick(InventoryClickEvent event, InvTransformer transformer) {
        List<InvAction> actions = this.actions.getOrDefault(event.getClick(), new ArrayList<>());
        Iterator<InvAction> iterator = actions.iterator();
        if (iterator.hasNext()) iterator.next().run(event, this, transformer, iterator);
    }

    public static InvButton parseYaml(ConfigurationSection section) {
        String materialStr = section.getString("material", "STONE");
        Material material = Material.matchMaterial(materialStr);
        ItemStack itemStack = new ItemStack(material == null ? Material.STONE : material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        val name = section.getString("name", null);
        if (name != null) itemMeta.setDisplayName(name);
        val lore = section.getList("lore", null);
        if (lore != null) itemMeta.setLore((List<String>) lore);
        itemStack.setItemMeta(itemMeta);

        int amount = section.getInt("amount", -1);
        if (amount != -1) itemStack.setAmount(amount);

        ConfigurationSection actionsSection = section.getConfigurationSection("actions");
        Logger logger = FIData.plugin.getLogger();
        HashMap<ClickType, List<InvAction>> actions = new HashMap<>();
        if (actionsSection != null) for (String key : actionsSection.getKeys(false)) {
            ClickType clickType = clickTypeFromString(key);
            if (clickType == null) {
                logger.warning("Unknown click type: " + key);
                continue;
            }
            List<?> list = actionsSection.getList(key);
            if (list == null) continue;
            ArrayList<InvAction> actionsList = new ArrayList<>();
            for (Object obj : list) {
                InvAction action = InvActionFactories.create(obj);
                if (action == null) continue;
                actionsList.add(action);
            }
            actions.put(clickType, actionsList);
        }
        return new InvButton(itemStack, actions);
    }

    private static ClickType clickTypeFromString(String name) {
        try {
            ClickType.valueOf(name);
        } catch (IllegalArgumentException e) {
            for (ClickType value : ClickType.values()) if (value.name().equalsIgnoreCase(name)) return value;
        }
        return null;
    }

    /**
     * 对 {@link #display} 只有简单的一些处理
     * 实在不行可以考虑使用 {@link #display(ItemStack)}
     */
    public static class Builder {
        private ItemStack display = new ItemStack(Material.STONE);
        private Map<ClickType, List<InvAction>> actions = new HashMap<>();

        public ItemStack display() {
            return this.display;
        }

        public Builder display(ItemStack display) {
            this.display = display;
            return this;
        }

        public Builder displayMaterial(Material material) {
            this.display.setType(material);
            return this;
        }

        public Builder displayName(String name) {
            ItemMeta itemMeta = this.display.getItemMeta();
            itemMeta.setDisplayName(name);
            this.display.setItemMeta(itemMeta);
            return this;
        }

        public Builder displayLore(String... lore) {
            ItemMeta itemMeta = this.display.getItemMeta();
            itemMeta.setLore(Arrays.asList(lore));
            this.display.setItemMeta(itemMeta);
            return this;
        }

        public Map<ClickType, List<InvAction>> actions() {
            return this.actions;
        }

        @NotNull
        public List<InvAction> actions(ClickType clickType) {
            return this.actions.getOrDefault(clickType, new ArrayList<>());
        }

        /**
         * 增加动作
         * 如果已经有数据了不会覆盖而是追加!
         */
        public Builder actions(ClickType clickType, InvAction... actions) {
            this.actions.computeIfAbsent(clickType, k -> new ArrayList<>()).addAll(Arrays.asList(actions));
            return this;
        }

        /**
         * 所有点击方式 增加 动作
         */
        public Builder actions(InvAction... actions) {
            List<InvAction> list = Arrays.asList(actions);
            for (ClickType value : ClickType.values())
                this.actions.computeIfAbsent(value, k -> new ArrayList<>()).addAll(list);
            return this;
        }

        public InvButton build() {
            return new InvButton(this.display, this.actions);
        }
    }
}
