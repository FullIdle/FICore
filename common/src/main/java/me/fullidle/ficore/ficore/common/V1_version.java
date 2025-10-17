package me.fullidle.ficore.ficore.common;

import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory;
import org.bukkit.plugin.Plugin;

public abstract class V1_version {
    /**获取版本*/
    abstract public String getVersion();
    /**将 {@link me.fullidle.ficore.ficore.common.api.event.ForgeEvent} 注册到事件系统中*/
    abstract public void registerForgeEvent();
    /**通过插件注册一个事件到Forge 不理解建议不用*/
    abstract public void register(Plugin plugin, Object bus, Object target);
    /**注销指定插件所有已注册的监听器*/
    abstract public void unregisterAllListener(Plugin plugin);
    /**获取所有物种包装(这应该直接将所有物种进行缓存了)*/
    abstract public ISpeciesWrapperFactory<?> getSpeciesWrapperFactory();

    abstract public IPokemonWrapperFactory<?> getPokemonWrapperFactory();
}
