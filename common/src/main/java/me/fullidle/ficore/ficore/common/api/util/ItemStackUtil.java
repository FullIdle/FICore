package me.fullidle.ficore.ficore.common.api.util;

import lombok.val;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;

public class ItemStackUtil {
    /**
     * 解析简单的yaml物品格式
     */
    public static ItemStack parseSimpleYaml(ConfigurationSection section) {
        val materialName = Objects.requireNonNull(Objects.requireNonNull(section).getString("material"));
        val material = Objects.requireNonNull(Material.getMaterial(materialName));
        val itemStack = new ItemStack(material);

        val durability = section.getInt("durability", -1);
        if (durability != -1) itemStack.setDurability((short) durability);
        val amount = section.getInt("amount", -1);
        if (amount != -1) itemStack.setAmount(amount);

        val itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(section.getString("name"));
        itemMeta.setLore(section.getStringList("lore"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * 只读3项
     */
    public static ItemStack parseSimpleMap(Map<String, Object> map) {
        val section = new YamlConfiguration().createSection("item", map);
        return parseSimpleYaml(section);
    }
}
