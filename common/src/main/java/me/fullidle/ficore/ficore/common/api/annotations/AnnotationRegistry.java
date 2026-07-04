package me.fullidle.ficore.ficore.common.api.annotations;

import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class AnnotationRegistry {
    @SneakyThrows
    public static void pluginEnable(Plugin plugin) {
        val clazz = plugin.getClass();
        val annotation = clazz.getAnnotation(BukkitPlugin.class);
        if (annotation == null) return;
        val pm = Bukkit.getPluginManager();
        for (Class<? extends Listener> listener : annotation.listeners())
            pm.registerEvents(listener.newInstance(), plugin);
    }
}
