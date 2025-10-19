package me.fullidle.ficore.ficore.common.bukkit.inventory;

import lombok.Getter;
import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.SomeMethod;
import me.fullidle.ficore.ficore.common.api.util.VersionUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CraftItemStack {
    @Getter
    private static final Class<?> v1_CraftItemStack_Class;
    private static final Map<String, Method> methods = new HashMap<>();
    public static final Object v1_itemStack_Grass;

    @SneakyThrows
    private static Object invoke(Object... objects){
        Method method = methods.get(Thread.currentThread().getStackTrace()[2].getMethodName());
        if (objects.length == 0) return method.invoke(v1_CraftItemStack_Class);
        return method.invoke(v1_CraftItemStack_Class,objects);
    }

    public static Object asNMSCopy(ItemStack itemStack){
        return invoke(itemStack);
    }
    public static Object asCraftCopy(ItemStack itemStack){
        return invoke(itemStack);
    }
    public static Object asCraftMirror(Object v1_itemStack){
        return invoke(v1_itemStack);
    }
    public static ItemStack asBukkitCopy(Object v1_itemStack){
        return (ItemStack) invoke(v1_itemStack);
    }
    public static Object copyNMSStack(Object v1_itemStack,int amount){
        return invoke(v1_itemStack,amount);
    }

    static {
        try {
            v1_CraftItemStack_Class = Class.forName("inventory.CraftItemStack");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> collect = Arrays.stream(CraftItemStack.class.getDeclaredMethods()).
                map(Method::getName).collect(Collectors.toCollection(ArrayList::new));
        for (Method method : v1_CraftItemStack_Class.getDeclaredMethods()) {
            String name = method.getName();
            if (collect.contains(name)) {
                method.setAccessible(true);
                methods.put(name,method);
            }
        }
        v1_itemStack_Grass = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.GRASS));
    }
}
