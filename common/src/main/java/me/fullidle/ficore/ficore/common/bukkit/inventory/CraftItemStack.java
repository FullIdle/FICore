package me.fullidle.ficore.ficore.common.bukkit.inventory;

import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.util.VersionUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CraftItemStack {
    @Getter
    private static final Class<?> v1_CraftItemStack_Class;
    private static final Map<String, List<Method>> methods = new HashMap<>();
    public static final Object v1_itemStack_Grass;

    private static Object invoke(Object... objects) {
        val methodList = methods.get(Thread.currentThread().getStackTrace()[2].getMethodName());
        val logger = FIData.plugin.getLogger();
        val iterator = methodList.iterator();
        if (objects.length != 0) {
            while (iterator.hasNext()) {
                val method = iterator.next();
                if (method.getParameterTypes().length != 0) try {
                    val invoke = method.invoke(v1_CraftItemStack_Class, objects);
                    methodList.removeIf(t -> !t.equals(method));
                    return invoke;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.warning("调用方法" + method.getName() + "时出现异常已将其排除,并自适应. 异常信息: " + e.getMessage());
                    iterator.remove();
                }
            }
            throw new RuntimeException("没有合适的方法可以调用");
        }
        while (iterator.hasNext()) {
            val method = iterator.next();
            if (method.getParameterTypes().length == 0) try {
                val invoke = method.invoke(v1_CraftItemStack_Class);
                methodList.removeIf(t -> !t.equals(method));
                return invoke;
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.warning("调用方法" + method.getName() + "时出现异常已将其排除,并自适应. 异常信息: " + e.getMessage());
                iterator.remove();
            }
        }
        throw new RuntimeException("没有合适的方法可以调用");
    }

    public static Object asNMSCopy(ItemStack itemStack) {
        return invoke(itemStack);
    }

    public static Object asCraftCopy(ItemStack itemStack) {
        return invoke(itemStack);
    }

    public static Object asCraftMirror(Object v1_itemStack) {
        return invoke(v1_itemStack);
    }

    public static ItemStack asBukkitCopy(Object v1_itemStack) {
        return (ItemStack) invoke(v1_itemStack);
    }

    public static Object copyNMSStack(Object v1_itemStack, int amount) {
        return invoke(v1_itemStack, amount);
    }

    static {
        try {
            v1_CraftItemStack_Class = Class.forName(VersionUtil.formatCraftBukkitClass("inventory.CraftItemStack"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> collect = Arrays.stream(CraftItemStack.class.getDeclaredMethods()).
                map(Method::getName).collect(Collectors.toCollection(ArrayList::new));
        for (Method method : v1_CraftItemStack_Class.getDeclaredMethods()) {
            String name = method.getName();
            if (collect.contains(name)) {
                method.setAccessible(true);
                methods.computeIfAbsent(name, k->new ArrayList<>()).add(method);
            }
        }
        v1_itemStack_Grass = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.GRASS));
    }
}
