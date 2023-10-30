package me.fullidle.ficore.ficore.common;

import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

public abstract class V1_version {
    abstract public String getVersion();
    abstract public void registerForgeEvent();
    abstract public void register(Plugin plugin, Object bus, Object target);
    abstract public void unregisterAllListener(Plugin plugin);
}
