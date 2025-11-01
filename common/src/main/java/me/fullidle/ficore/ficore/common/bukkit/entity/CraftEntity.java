package me.fullidle.ficore.ficore.common.bukkit.entity;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CraftEntity {
    public static final Class<?> NMS_CRAFT_ENTITY_CLASS;
    public static final Method GET_ENTITY_METHOD;
    public static final Method GET_HANDLE_METHOD;

    public static Object getHandle(Entity entity) {
        if (entity == null) return null;
        try {
            return GET_HANDLE_METHOD.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Entity getEntity(Object nmsEntity) {
        if (nmsEntity == null) return null;
        try {
            return (Entity) GET_ENTITY_METHOD.invoke(null, Bukkit.getServer(), nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            NMS_CRAFT_ENTITY_CLASS = Class.forName(VersionUtil.formatCraftBukkitClass("entity.CraftEntity"));
            val collect = Arrays.stream(NMS_CRAFT_ENTITY_CLASS.getDeclaredMethods()).collect(Collectors.toMap(Method::getName, method -> method, (a, b) -> a));
            GET_ENTITY_METHOD = collect.get("getEntity");
            GET_HANDLE_METHOD = collect.get("getHandle");

            if (GET_ENTITY_METHOD == null || GET_HANDLE_METHOD == null)
                FIData.plugin.getLogger().warning("CraftEntity may not work properly!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
