package me.fullidle.ficore.ficore.common.bukkit.nbt;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Getter
public class NBTList {
    private final Object originalNBTList;

    public NBTList(Object originalNBTList) {
        this.originalNBTList = originalNBTList;
    }

    @SneakyThrows
    public NBTList() {
        this.originalNBTList = v1_NBTTagList_Constructor.newInstance();
    }

    @SneakyThrows
    public void add(Object o) {
        if (o instanceof NBTTagCompound) o = ((NBTTagCompound) o).getNbtTagCompound();
        Class<?> superclass = NBTTagCompound.v1_NBTTagCompound_Class.getSuperclass();
        Class<?> aClass = o.getClass();
        if (!superclass.isAssignableFrom(aClass))
            throw new IllegalArgumentException("NBTList.add() only accepts " + superclass);
        method_Add.invoke(this.originalNBTList, o);
    }

    @SneakyThrows
    public Object get(int index) {
        return method_Get.invoke(index);
    }

    @SneakyThrows
    public int size() {
        return (int) method_Size.invoke(originalNBTList);
    }

    @Override
    public String toString() {
        return this.originalNBTList.toString();
    }

    public static final Class<?> v1_NBTTagList_Class;
    public static Method method_Add;
    public static Method method_Get;
    public static Method method_Size;
    public static Constructor<?> v1_NBTTagList_Constructor;

    static {
        v1_NBTTagList_Class = NBTTagCompound.methods.get("getList").getReturnType();

        try {
            v1_NBTTagList_Constructor = v1_NBTTagList_Class.getDeclaredConstructor();
            v1_NBTTagList_Constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method method : v1_NBTTagList_Class.getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getReturnType().equals(Void.TYPE)
                    && method.getParameterTypes().length == 1
                    && method.getParameterTypes()[0] == NBTTagCompound.v1_NBTTagCompound_Class.getSuperclass()) {
                method_Add = method;
                continue;
            }
            if (method.getReturnType() == NBTTagCompound.v1_NBTTagCompound_Class.getSuperclass()
                    && method.getParameterTypes().length == 1
                    && (method.getParameterTypes()[0] == int.class || method.getParameterTypes()[0] == Integer.class)
            ) method_Get = method;
        }
        try {
            method_Size = v1_NBTTagList_Class.getDeclaredMethod("size");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
