package me.fullidle.ficore.ficore.common.api.util;

import lombok.val;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
        if (itemMeta != null) {
            itemMeta.setDisplayName(section.getString("name"));
            itemMeta.setLore(section.getStringList("lore"));
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * 只读3项
     */
    public static ItemStack parseSimpleMap(Map<String, Object> map) {
        val section = new YamlConfiguration().createSection("item", map);
        return parseSimpleYaml(section);
    }

    /**
     * 检查玩家物品
     * @param itemStacks 需要查找的物品集合
     * @param needCheck 需要被找到的物品数量集合
     * @param consumer 消费者
     * @return 是否满足所有物品
     */
    public static boolean checkNeedItem(Collection<ItemStack> itemStacks, List<Map.Entry<Predicate<ItemStack>, Integer>> needCheck, Consumer<List<Map.Entry<ItemStack, Integer>>> consumer) {
        if (itemStacks.isEmpty() || needCheck.isEmpty()) {
            consumer.accept(Collections.emptyList());
            return true;
        }

        //克隆玩家背包
        val tempInv = new ArrayList<Map.Entry<ItemStack, Integer>>();
        for (val itemStack : itemStacks) {
            if (itemStack == null || itemStack.getType().equals(Material.AIR) || itemStack.getAmount() == 0) continue;
            tempInv.add(new AbstractMap.SimpleEntry<>(itemStack, itemStack.getAmount()));
        }
        if (tempInv.isEmpty()) return false;

        for (val needItem : needCheck) {
            //查找匹配的物品解决数量问题
            int need = needItem.getValue();
            Map.Entry<org.bukkit.inventory.ItemStack, Integer> match;
            while (need > 0 && (match = findHasAmountItem(tempInv, needItem.getKey())) != null) {
                val old = match.getValue();
                val take = Math.min(need, old);
                need -= take;
                match.setValue(Math.max(0, old - take));
            }
            if (need > 0) return false;
        }

        consumer.accept(tempInv);
        return true;
    }

    /**
     * 查找可匹配的物品
     */
    public static Map.Entry<org.bukkit.inventory.ItemStack, Integer> findHasAmountItem(List<Map.Entry<org.bukkit.inventory.ItemStack, Integer>> itemStacks, Predicate<org.bukkit.inventory.ItemStack> predicate) {
        for (Map.Entry<org.bukkit.inventory.ItemStack, Integer> entry : itemStacks)
            if (predicate.test(entry.getKey()) && entry.getValue() > 0) return entry;
        return null;
    }
}
