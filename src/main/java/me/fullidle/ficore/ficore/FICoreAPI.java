package me.fullidle.ficore.ficore;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import org.bukkit.plugin.Plugin;

public class FICoreAPI {
    public static void registerForgeListener(Plugin plugin, Object bus, Object o){
        FIData.V1_version.register(plugin,bus,o);
    }
}
