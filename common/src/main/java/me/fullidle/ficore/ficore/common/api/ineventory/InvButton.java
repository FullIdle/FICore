package me.fullidle.ficore.ficore.common.api.ineventory;

import lombok.Getter;
import lombok.Setter;
import me.fullidle.ficore.ficore.common.api.util.ItemStackUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class InvButton {
    private String id;
    private ItemStack displayItem;
    private ConfigurationSection config;

    public InvButton(
            String id,
            ItemStack displayItem,
            ConfigurationSection config
    ) {
        this.id = id;
        this.displayItem = displayItem;
        this.config = config;
    }

    public static InvButton parse(String id, ConfigurationSection section) {
        return new InvButton(id, ItemStackUtil.parseSimpleYaml(section), section);
    }
}
