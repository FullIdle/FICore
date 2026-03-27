package me.fullidle.ficore.ficore.listener;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public class PluginListener implements org.bukkit.event.Listener {
    public static final PluginListener INSTANCE = new PluginListener();

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, FIData.plugin);
    }

    @EventHandler
    public void pluginDisable(PluginDisableEvent e) {
        Plugin plugin = e.getPlugin();
        if (FIData.V1_version == null) return;
        FIData.V1_version.unregisterAllListener(plugin);
    }
}
