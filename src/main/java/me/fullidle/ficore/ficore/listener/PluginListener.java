package me.fullidle.ficore.ficore.listener;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public class PluginListener implements org.bukkit.event.Listener {
    @EventHandler
    public void pluginDisable(PluginDisableEvent e){
        Plugin plugin = e.getPlugin();
        FIData.V1_version.unregisterAllListener(plugin);
    }
}
