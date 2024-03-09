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
        getLogger().info("§3你的版本是"+version);
        int shortVer = Integer.parseInt(version.split("\\.")[1]);
        V1_version v1Version = shortVer > 12 ? new V1_16() : new V1_12();
        try {
            v1Version.registerForgeEvent();
        }catch (Exception e){
            getLogger().info("§c这个版本的forge不被支持,以放弃Forge功能,保留基础功能!");
            e.printStackTrace();
        }
        getLogger().info("§aPlugin loaded!");
        getServer().getPluginManager().registerEvents(new PluginListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerListener(),this);
    }

    @Override
    public void onDisable() {
        FIData.V1_version.unregisterAllListener(this);
    }
}