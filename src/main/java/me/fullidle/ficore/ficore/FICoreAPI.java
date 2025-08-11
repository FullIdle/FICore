package me.fullidle.ficore.ficore;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory;
import org.bukkit.plugin.Plugin;

public class FICoreAPI {
    /**
     * 注册Forge监听器
     */
    public static void registerForgeListener(Plugin plugin, Object bus, Object o) {
        FIData.V1_version.register(plugin, bus, o);
    }

    /**
     * 获取插件FICore实例
     */
    public static Main getPlugin() {
        if (FIData.plugin instanceof Main) return (Main) FIData.plugin;
        return null;
    }

    /**
     * 获取先版本的物种包装工厂
     */
    public static ISpeciesWrapperFactory<?> getSpeciesWrapperFactory() {
        return FIData.V1_version.getSpeciesWrapperFactory();
    }
}
