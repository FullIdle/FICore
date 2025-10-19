package me.fullidle.ficore.ficore.common.bukkit.entity;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CraftEntity {
    public static final Class<?> NMS_CRAFT_ENTITY_CLASS;
    public static final Method GET_ENTITY_METHOD;

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
            NMS_CRAFT_ENTITY_CLASS = Class.forName(String.format("%s.%s.entity.CraftEntity", VersionUtil.CRAFT_BUKKIT_HEAD_TAG, VersionUtil.getNmsVersion()));
            val method = Arrays.stream(NMS_CRAFT_ENTITY_CLASS.getDeclaredMethods()).filter(m -> m.getName().equals("getEntity")).findFirst();
            if (!method.isPresent()) throw new RuntimeException("Could not find getHandle method in CraftEntity class");
            GET_ENTITY_METHOD = method.get();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
