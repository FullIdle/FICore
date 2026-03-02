package me.fullidle.ficore.ficore.v1_12;

import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.*;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgeEventRegistrar {
    @SneakyThrows
    public static void registerForgeEvent() {
        Field field = ListenerList.class.getDeclaredField("lists");
        field.setAccessible(true);
        Object[] lists = (Object[]) field.get(listenerList());
        Method method = null;
        ForgeEventListener listener = new ForgeEventListener();
        for (int i = 0; i < lists.length; i++) {
            Object list = lists[i];
            if (method != null) {
                method.invoke(list, EventPriority.NORMAL, listener);
                Map<Integer, List<Object>> objects = FIData.listenerList.computeIfAbsent(FIData.plugin, k -> new HashMap<>());
                List<Object> objects1 = objects.computeIfAbsent(i, k -> new ArrayList<>());
                objects1.add(listener);
            } else {
                method = list.getClass().getDeclaredMethod("register", EventPriority.class, IEventListener.class);
                method.setAccessible(true);
            }
        }
        //总线上需要单独注册
        register(FIData.plugin, MinecraftForge.EVENT_BUS, EventPriority.NORMAL, listener);
    }

    @SneakyThrows
    private static ListenerList listenerList() {
        Field listeners = Event.class.getDeclaredField("listeners");
        listeners.setAccessible(true);
        return (ListenerList) listeners.get(Event.class);
    }

    @SneakyThrows
    private static void register(Plugin plugin, EventBus bus, EventPriority eventPriority, IEventListener listener) {
        {
            Field busIDF = EventBus.class.getDeclaredField("busID");
            busIDF.setAccessible(true);
            int busID = (int) busIDF.get(bus);
            listenerList().register(busID, eventPriority, listener);
            Map<Integer, List<Object>> objects = FIData.listenerList.computeIfAbsent(plugin, k -> new HashMap<>());
            List<Object> objects1 = objects.computeIfAbsent(busID, k -> new ArrayList<>());
            objects1.add(listener);
        }
    }

    @SneakyThrows
    public static void register(Plugin plugin, Object bus, Object target) {
        boolean isStatic = target.getClass() == Class.class;
        Class<?> cas = (isStatic ? ((Class<?>) target) : target.getClass());
        Method[] methods = cas.getDeclaredMethods();
        for (Method method : methods) {
            if ((!isStatic || Modifier.isStatic(method.getModifiers())) && (isStatic || !Modifier.isStatic(method.getModifiers()))) {
                if (method.isAnnotationPresent(SubscribeEvent.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods must require a single argument.");
                    }

                    Class<?> eventType = parameterTypes[0];
                    if (!Event.class.isAssignableFrom(eventType)) {
                        throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but takes a argument that is not an Event " + eventType);
                    }

                    SubscribeEvent annotation = method.getDeclaredAnnotation(SubscribeEvent.class);
                    EventPriority priority = annotation.priority();

                    register(plugin, (EventBus) bus, priority, event -> {
                        if (eventType.isInstance(event)) {
                            try {
                                method.invoke(target);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        }
    }

    @SneakyThrows
    public static void unregisterAllListener(Plugin plugin) {
        Map<Integer, List<Object>> list = FIData.listenerList.get(plugin);
        if (list == null) {
            return;
        }
        for (Map.Entry<Integer, List<Object>> entry : list.entrySet()) {
            for (Object o : entry.getValue()) {
                listenerList().unregister(entry.getKey(), (IEventListener) o);
            }
        }
    }
}
