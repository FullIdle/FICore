package me.fullidle.ficore.ficore.common.bukkit.inventory;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Getter
public class ItemStack {
    public final static Class<?> v1_ItemStack_Class;
    public static Field tagV_field;

/*========================================*/
    private final Object v1_itemStack;

    public ItemStack(Object v1_itemStack){
        this.v1_itemStack = v1_itemStack;
    }

    @SneakyThrows
    public Object getNBTTag(){
        return tagV_field.get(this.v1_itemStack);
    }

    @SneakyThrows
    public void setNBTTag(Object nbt){
        tagV_field.set(this.v1_itemStack,nbt);
    }

    static {
        v1_ItemStack_Class = CraftItemStack.v1_itemStack_Grass.getClass();
        for (Field field : v1_ItemStack_Class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType().getTypeName().startsWith("net.minecraft.nbt.")&&
                    !field.getName().equalsIgnoreCase("capNBT")
            ){
                field.setAccessible(true);
                tagV_field = field;
                break;
            }
        }
    }
}
