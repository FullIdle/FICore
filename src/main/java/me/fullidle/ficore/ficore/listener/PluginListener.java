package me.fullidle.ficore.ficore.listener;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.annotations.AnnotationRegistry;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class PluginListener implements org.bukkit.event.Listener {
    public static final PluginListener INSTANCE = new PluginListener();

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, FIData.plugin);
    }

    public void pluginEnable(PluginEnableEvent evt) {
        val plugin = evt.getPlugin();
        AnnotationRegistry.pluginEnable(plugin);
    }

    @EventHandler
    public void pluginDisable(PluginDisableEvent e) {
        Plugin plugin = e.getPlugin();
        if (FIData.V1_version == null) return;
        FIData.V1_version.unregisterAllListener(plugin);
    }
}
