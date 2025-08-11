package me.fullidle.ficore.ficore;

import me.fullidle.ficore.ficore.common.SomeMethod;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.listener.PlayerListener;
import me.fullidle.ficore.ficore.listener.PluginListener;
import me.fullidle.ficore.ficore.v1_12.V1_12;
import me.fullidle.ficore.ficore.v1_16.V1_16;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onLoad() {
        FIData.plugin = this;
    }

    @Override
    public void onEnable() {
        String version = SomeMethod.getMinecraftVersion();
        getLogger().info("§3Your version: " + version);
        int shortVer = Integer.parseInt(version.split("\\.")[1]);


        getServer().getPluginManager().registerEvents(new PluginListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        try {
            V1_version v1Version = shortVer > 12 ? new V1_16() : new V1_12();
            v1Version.registerForgeEvent();
        } catch (NoClassDefFoundError e) {
            getLogger().info("§cThe server-side core version is not supported or supported by Forge, and the basic functionality of Forge features has been abandoned!");
        }
        getLogger().info("§aPlugin loaded!");
    }

    @Override
    public void onDisable() {
        if (FIData.V1_version == null) return;
        FIData.V1_version.unregisterAllListener(this);
    }
}