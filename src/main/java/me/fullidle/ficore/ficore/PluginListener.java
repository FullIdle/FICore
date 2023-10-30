package me.fullidle.ficore.ficore;

import me.fullidle.ficore.ficore.common.FIData;
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

    @EventHandler
    public void onTest(ForgeEvent e){
        String simpleName = e.getForgeEvent().getClass().getSimpleName();
        if (simpleName.toLowerCase().contains("drop")){
            System.out.println(simpleName);
        }
    }
}
