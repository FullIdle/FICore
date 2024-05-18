package me.fullidle.ficore.ficore.common.bukkit.nbt;

import lombok.Getter;
import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Getter
public class NBTTagCompound {
    public static final Class<?> v1_NBTTagCompound_Class;
    private static final Constructor<?> v1_NBTTagCompound_ConStructor;
    public static final Map<String,Method> methods = new HashMap<>();

    private final Object nbtTagCompound;

    @SneakyThrows
    public NBTTagCompound(Object nbtTagCompound) {
        if (nbtTagCompound == null) nbtTagCompound = v1_NBTTagCompound_ConStructor.newInstance();
        this.nbtTagCompound = nbtTagCompound;
    }
    @SneakyThrows
    public NBTTagCompound(){
        this.nbtTagCompound = v1_NBTTagCompound_ConStructor.newInstance();
    }

    @SneakyThrows
    private Object invoke(Object... objects){
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        Method method = methods.get(name);
        if (objects == null || objects.length == 0) return method.invoke(this.nbtTagCompound);
        return method.invoke(this.nbtTagCompound,objects);
    }

    //set
    public void set(String key,Object nbtBase){
        this.invoke(key,nbtBase);
    }
    public void setString(String key,String value){
        this.invoke(key,value);
    }
    public void setBoolean(String key,boolean value){
        this.invoke(key,value);
    }
    public void setByte(String key,byte value){
        this.invoke(key,value);
    }
    public void setDouble(String key,double value){
        this.invoke(key,value);
    }
    public void setByteArray(String key,byte[] value){
        this.invoke(key,value);
    }
    public void setFloat(String key,float value){
        this.invoke(key,value);
    }
    public void setInt(String key,int value){
        this.invoke(key,value);
    }
    public void setIntArray(String key,int[] value){
        this.invoke(key,value);
    }
    public void setLong(String key,long value){
        this.invoke(key,value);
    }
    public void setShort(String key,short value){
        this.invoke(key,value);
    }
    //get
    public String getString(String key){
        return (String) this.invoke(key);
    }
    public boolean getBoolean(String key){
        return (boolean) this.invoke(key);
    }
    public byte getByte(String key){
        return (byte) this.invoke(key);
    }
    public double getDouble(String key){
        return (double) this.invoke(key);
    }
    public byte[] getByteArray(String key){
        return (byte[]) this.invoke(key);
    }
    public float getFloat(String key){
        return (float) this.invoke(key);
    }
    public int getInt(String key){
        return (int) this.invoke(key);
    }
    public int[] getIntArray(String key){
        return (int[]) this.invoke(key);
    }
    public long getLong(String key){
        return (long) this.invoke(key);
    }
    public short getShort(String key){
        return (short) this.invoke(key);
    }
    public Object getCompound(String key){
        return this.invoke(key);
    }
    

    static {
        v1_NBTTagCompound_Class =  ItemStack.tagV_field.getType();
        try {
            v1_NBTTagCompound_ConStructor = v1_NBTTagCompound_Class.getDeclaredConstructor();
            v1_NBTTagCompound_ConStructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method method : v1_NBTTagCompound_Class.getDeclaredMethods()) {
            int i = method.getModifiers();
            Class<?> returnType = method.getReturnType();
            if (Modifier.isStatic(i) && !Modifier.isPublic(i)) continue;
            method.setAccessible(true);

            /*set TYPE*/
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (returnType.equals(Void.TYPE)&&
                    parameterTypes.length == 2
            ){
                Class<?> type = parameterTypes[1];
                if (parameterTypes[0].equals(String.class)){
                    if (type.equals(String.class))
                        methods.put("setString",method);
                    if (type.equals(boolean.class)||type.equals(Boolean.class))
                        methods.put("setBoolean",method);
                    if (type.equals(byte.class)||type.equals(Byte.class))
                        methods.put("setByte",method);
                    if (type.equals(double.class)||type.equals(Double.class))
                        methods.put("setDouble",method);
                    if (type.equals(byte[].class)||type.equals(Byte[].class))
                        methods.put("setByteArray",method);
                    if (type.equals(float.class)||type.equals(Float.class))
                        methods.put("setFloat",method);
                    if (type.equals(int.class)||type.equals(Integer.class))
                        methods.put("setInt",method);
                    if (type.equals(int[].class)||type.equals(Integer[].class))
                        methods.put("setIntArray",method);
                    if (type.equals(long.class)||type.equals(Long.class))
                        methods.put("setLong",method);
                    if (type.equals(short.class)||type.equals(Short.class))
                        methods.put("setShort",method);
                    //ss
                    Class<?> nbtBase = v1_NBTTagCompound_Class.getSuperclass();
                    if (type.equals(nbtBase))
                        methods.put("set",method);
                }
            }
            if (parameterTypes.length == 1 && parameterTypes[0].equals(String.class)){
                if (returnType.equals(String.class))
                    methods.put("getString",method);
                if (returnType.equals(boolean.class)||returnType.equals(Boolean.class))
                    methods.put("getBoolean",method);
                if (returnType.equals(byte.class)||returnType.equals(Byte.class))
                    methods.put("getByte",method);
                if (returnType.equals(double.class)||returnType.equals(Double.class))
                    methods.put("getDouble",method);
                if (returnType.equals(byte[].class)||returnType.equals(Byte[].class))
                    methods.put("getByteArray",method);
                if (returnType.equals(float.class)||returnType.equals(Float.class))
                    methods.put("getFloat",method);
                if (returnType.equals(int.class)||returnType.equals(Integer.class))
                    methods.put("getInt",method);
                if (returnType.equals(int[].class)||returnType.equals(Integer[].class))
                    methods.put("getIntArray",method);
                if (returnType.equals(long.class)||returnType.equals(Long.class))
                    methods.put("getLong",method);
                if (returnType.equals(short.class)||returnType.equals(Short.class))
                    methods.put("getShort",method);
                //ss
                if (returnType.equals(v1_NBTTagCompound_Class))
                    methods.put("getCompound",method);
            }
        }
    }
}
