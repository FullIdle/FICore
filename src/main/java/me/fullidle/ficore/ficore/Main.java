package me.fullidle.ficore.ficore;

import lombok.val;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.util.VersionUtil;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import me.fullidle.ficore.ficore.listener.PlayerListener;
import me.fullidle.ficore.ficore.listener.PluginListener;
import me.fullidle.ficore.ficore.v1_12.V1_12;
import me.fullidle.ficore.ficore.v1_16.V1_16;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class Main extends JavaPlugin {
    @Override
    public void onLoad() {
        FIData.plugin = this;
    }

    @Override
    public void onEnable() {
        System.out.println(VersionUtil.class);
        System.out.println(VersionUtil.getMinecraftVersion());
        String version = VersionUtil.getMinecraftVersion();
        getLogger().info("§3Your version: " + version);
        int shortVer = Integer.parseInt(version.split("\\.")[1]);


        getServer().getPluginManager().registerEvents(new PluginListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        try {
            try {
                Class.forName("com.cobblemon.mod.common.Cobblemon");
                val field = Class.forName("me.fullidle.ficore.ficore.vCob_21.VCob_21").getDeclaredField("INSTANCE");
                field.setAccessible(true);
                ((V1_version) field.get(field)).registerForgeEvent();
            } catch (ClassNotFoundException e) {
                V1_version v1Version;
                if (shortVer > 12) {
                    if (shortVer > 21) {
                        try {
                            val clazz = Class.forName("me.fullidle.ficore.ficore.v1_21.V1_21");
                            v1Version = (V1_version) clazz.getDeclaredConstructor().newInstance();
                        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                                 IllegalAccessException | NoSuchMethodException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        v1Version = new V1_16();
                    }
                } else {
                    v1Version = new V1_12();
                }
                v1Version.registerForgeEvent();
            }
        } catch (NoClassDefFoundError | NoSuchFieldException | IllegalAccessException e) {
            getLogger().info("§cThe server-side core version is not supported or supported by Forge, and the basic functionality of Forge features has been abandoned!");
        }
        getLogger().info("§aPlugin loaded!");
        val world = Bukkit.getWorlds().get(0);
        val entity = world.spawnEntity(new Location(world, 0, 0, 0), EntityType.DROPPED_ITEM);
        val handle = CraftEntity.getHandle(entity);
        System.out.println(handle.getClass());
        System.out.println(CraftEntity.getEntity(handle).getUniqueId());
    }

    @Override
    public void onDisable() {
        if (FIData.V1_version == null) return;
        FIData.V1_version.unregisterAllListener(this);
    }
}