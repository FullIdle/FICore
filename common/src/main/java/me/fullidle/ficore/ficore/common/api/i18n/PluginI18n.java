package me.fullidle.ficore.ficore.common.api.i18n;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public class PluginI18n {
    private final Plugin plugin;

    public PluginI18n(Plugin plugin) {
        this.plugin = plugin;
    }
}
