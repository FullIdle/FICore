package me.fullidle.ficore.ficore.common.api.ineventory;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class InvConfig {
    private ConfigurationSection config;
    private String title;
    private final List<String> layout;
    private final Map<String, InvButton> buttonMap = new HashMap<>();

    public InvConfig(
            ConfigurationSection config,
            String title,
            List<String> layout,
            List<InvButton> buttons
    ) {
        this.config = config == null ? new YamlConfiguration() : config;
        this.title = title;
        this.layout = layout;
        buttons.forEach(b -> this.buttonMap.put(b.getId(), b));
    }

    public Inventory createInv(InventoryHolder holder) {
        return createInv(holder, null, null);
    }

    public Inventory createInv(InventoryHolder holder, Function<String, String> titleFun) {
        return createInv(holder, titleFun, null);
    }

    public Inventory createInv(InventoryHolder holder, BiFunction<InvButton, String, String> buttonStrFun) {
        return createInv(holder, null, buttonStrFun);
    }

    public Inventory createInv(InventoryHolder holder, Function<String, String> titleFun, BiFunction<InvButton, String, String> buttonStrFun) {
        val inv = Bukkit.createInventory(
                holder,
                this.layout.size() * 9,
                titleFun == null ? this.title : titleFun.apply(this.title)
        );
        for (int y = 0; y < this.layout.size(); y++) {
            val array = this.layout.get(y).toCharArray();
            for (int x = 0; x < array.length; x++) {
                val id = array[x] + "";
                if (this.buttonMap.containsKey(id)) {
                    val invButton = this.buttonMap.get(id);
                    val clone = invButton.getDisplayItem().clone();
                    if (buttonStrFun != null) {
                        val itemMeta = clone.getItemMeta();
                        assert itemMeta != null;
                        if (itemMeta.hasDisplayName())
                            itemMeta.setDisplayName(buttonStrFun.apply(invButton, itemMeta.getDisplayName()));
                        if (itemMeta.hasLore()) {
                            val lore = itemMeta.getLore();
                            assert lore != null;
                            itemMeta.setLore(lore.stream().map(s -> buttonStrFun.apply(invButton, s)).collect(Collectors.toList()));
                        }
                        clone.setItemMeta(itemMeta);
                    }
                    inv.setItem(x + y * 9, clone);
                }
            }
        }
        return inv;
    }

    public static InvConfig parse(ConfigurationSection section) {
        val title = section.getString("title");
        val layout = section.getStringList("layout");
        val invButtons = new ArrayList<InvButton>();
        val buttons = section.getConfigurationSection("buttons");
        if (buttons != null) for (String key : buttons.getKeys(false))
            invButtons.add(InvButton.parse(key, buttons.getConfigurationSection(key)));
        return new InvConfig(section, title, layout, invButtons);
    }

    public static InvConfig parse(Map<String, Object> data) {
        return parse(new YamlConfiguration().createSection("data", data));
    }
}
