package me.fullidle.ficore.ficore.v1_16;

import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.FIData;
import me.fullidle.ficore.ficore.common.V1_version;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.ListenerList;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventListenerHelper;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventListener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class V1_16 extends V1_version {
    public V1_16(){
        FIData.V1_version = this;
    }

    @Override
    public String getVersion() {
        return "1.16.5";
    }

    @SneakyThrows
    @Override
    public void registerForgeEvent() {
        ForgeEventListener listener = new ForgeEventListener();
        ListenerList listenerList = EventListenerHelper.getListenerList(Event.class);
        Field field = listenerList.getClass().getDeclaredField("lists");
        field.setAccessible(true);
        Object[] lists = (Object[]) field.get(listenerList);
        Method method = null;
        for (int i = 0; i < lists.length; i++) {
            Object list = lists[i];
            if (method != null) {
                method.invoke(list,EventPriority.NORMAL, listener);
                Map<Integer, ArrayList<Object>> objects = FIData.listenerList.computeIfAbsent(FIData.plugin, k -> new HashMap<>());
                ArrayList<Object> objects1 = objects.computeIfAbsent(i, k -> new ArrayList<>());
                objects1.add(listener);
            }else{
                method = list.getClass().getDeclaredMethod("register", EventPriority.class, IEventListener.class);
                method.setAccessible(true);
            }
        }
    }

    @SneakyThrows
    @Override
    public void register(Plugin plugin, Object bus, Object target) {
        EventBus eventBus = (EventBus) bus;
        eventBus.register(target);
        Map<Integer, ArrayList<Object>> listMap = FIData.listenerList.computeIfAbsent(plugin, k -> new HashMap<>());
        Field field = EventBus.class.getDeclaredField("busID");
        field.setAccessible(true);
        int busID = (int) field.get(bus);
        ArrayList<Object> objects = listMap.computeIfAbsent(busID, k -> new ArrayList<>());
        objects.add(target);
    }

    @Override
    public void unregisterAllListener(Plugin plugin) {
        Map<Integer, ArrayList<Object>> listMap = FIData.listenerList.get(plugin);
        if (listMap == null) {
            return;
        }
        for (Map.Entry<Integer, ArrayList<Object>> entry : listMap.entrySet()) {
            ListenerList.unregisterAll(entry.getKey(), (IEventListener) entry.getValue());
        }
    }
}
