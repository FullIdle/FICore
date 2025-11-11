package me.fullidle.ficore.ficore;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
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

    /**
     * 宝可梦包裹工厂
     */
    public static IPokemonWrapperFactory<?> getPokemonWrapperFactory() {
        return FIData.V1_version.getPokemonWrapperFactory();
    }

    /**
     * 存储管理器
     */
    public static IPokeStorageManager getPokeStorageManager() {
        return FIData.V1_version.getPokeStorageManager();
    }

    /**
     * 宝可梦实体包裹工厂
     */
    public static PokeEntityWrapperFactory<?> getPokeEntityWrapperFactory() {
        return FIData.V1_version.getPokeEntityWrapperFactory();
    }

    /**
     * 获取环境下的对局管理器
     */
    public static IBattleManager getBattleManager() {
        return FIData.V1_version.getBattleManager();
    }
}
