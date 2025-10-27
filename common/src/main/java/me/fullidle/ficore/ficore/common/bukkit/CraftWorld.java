package me.fullidle.ficore.ficore.common.bukkit;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.util.VersionUtil;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CraftWorld {
    public static final Class<?> NMS_CRAFT_WORLD_CLASS;
    public static final Method GET_HANDLE_METHOD;

    public static Object getHandle(World world) {
        try {
            return GET_HANDLE_METHOD.invoke(world);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            NMS_CRAFT_WORLD_CLASS = Class.forName(VersionUtil.formatCraftBukkitClass("CraftWorld"));
            val collect = Arrays.stream(NMS_CRAFT_WORLD_CLASS.getDeclaredMethods()).collect(Collectors.toMap(Method::getName, method -> method,(a, b)-> a));
            GET_HANDLE_METHOD = collect.get("getHandle");

            if (GET_HANDLE_METHOD == null)
                FIData.plugin.getLogger().warning("CraftWorld may not work properly!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
