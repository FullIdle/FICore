package me.fullidle.ficore.ficore;

import me.fullidle.ficore.ficore.common.SomeMethod;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.v1_12.V1_12;
import me.fullidle.ficore.ficore.v1_16.V1_16;
import org.bukkit.plugin.Plugin;
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
        if (version.equalsIgnoreCase("1.12.2")) {
            new V1_12();
        }else
        if (version.equalsIgnoreCase("1.16.5")) {
            new V1_16();
        }else{
            getLogger().info("§c该版本不受到支持!");
        }
        try {
            if (FIData.V1_version != null){
                FIData.V1_version.registerForgeEvent();
            }
        }catch (Exception e){
            getLogger().info("§c这个端没有ForgeApi,不涉及ForgeApi的功能均不可用");
            e.printStackTrace();
        }
        getLogger().info("§aPlugin loaded!");
        getServer().getPluginManager().registerEvents(new PluginListener(),this);
    }

    @Override
    public void onDisable() {
        FIData.V1_version.unregisterAllListener(this);
    }
}