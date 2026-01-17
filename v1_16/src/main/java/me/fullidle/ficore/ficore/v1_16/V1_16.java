package me.fullidle.ficore.ficore.v1_16;

import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.IPokemonConfigManager;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.breeds.IBreedLogic;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.*;
import net.minecraftforge.common.MinecraftForge;
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
import java.util.List;
import java.util.Map;

public class V1_16 extends V1_version {
    public boolean hasPokemon;

    public V1_16() {
        FIData.V1_version = this;
        try {
            Class.forName("com.pixelmonmod.pixelmon.Pixelmon");
            hasPokemon = true;
        } catch (ClassNotFoundException e) {
            hasPokemon = false;
        }
    }

    @Override
    public boolean hasPokemon() {
        return false;
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
                method.invoke(list, EventPriority.NORMAL, listener);
                Map<Integer, List<Object>> objects = FIData.listenerList.computeIfAbsent(FIData.plugin, k -> new HashMap<>());
                List<Object> objects1 = objects.computeIfAbsent(i, k -> new ArrayList<>());
                objects1.add(listener);
            } else {
                method = list.getClass().getDeclaredMethod("register", EventPriority.class, IEventListener.class);
                method.setAccessible(true);
            }
        }
        //总线注册
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, listener);
    }

    @SneakyThrows
    @Override
    public void register(Plugin plugin, Object bus, Object target) {
        EventBus eventBus = (EventBus) bus;
        eventBus.register(target);
        Map<Integer, List<Object>> listMap = FIData.listenerList.computeIfAbsent(plugin, k -> new HashMap<>());
        Field field = EventBus.class.getDeclaredField("busID");
        field.setAccessible(true);
        int busID = (int) field.get(bus);
        List<Object> objects = listMap.computeIfAbsent(busID, k -> new ArrayList<>());
        objects.add(target);
    }

    @Override
    public void unregisterAllListener(Plugin plugin) {
        Map<Integer, List<Object>> listMap = FIData.listenerList.get(plugin);
        if (listMap == null) {
            return;
        }
        for (Map.Entry<Integer, List<Object>> entry : listMap.entrySet()) {
            int busID = entry.getKey();
            List<Object> listeners = entry.getValue();
            for (Object obj : listeners) {
                if (obj instanceof IEventListener) {
                    ListenerList.unregisterAll(busID, (IEventListener) obj);
                } else {
                    // 处理不是 IEventListener 的情况（可选）
                    System.err.println("Warning: Listener is not an instance of IEventListener");
                }
            }
        }
        // 清空插件的监听器列表，避免重复注销
        FIData.listenerList.remove(plugin);
    }

    @Override
    public ISpeciesWrapperFactory<?> getSpeciesWrapperFactory() {
        return SpeciesWrapperFactory.INSTANCE;
    }

    @Override
    public IPokemonWrapperFactory<?> getPokemonWrapperFactory() {
        return PokemonWrapperFactory.INSTANCE;
    }

    @Override
    public IBreedLogic getBreedLogic() {
        return BreedLogic.INSTANCE;
    }

    @Override
    public IBattleManager<?> getBattleManager() {
        return BattleManager.INSTANCE;
    }

    @Override
    public IPokeStorageManager getPokeStorageManager() {
        return PokeStorageManager.INSTANCE;
    }

    @Override
    public PokeEntityWrapperFactory<?> getPokeEntityWrapperFactory() {
        return VPokeEntityWrapperFactory.INSTANCE;
    }

    @Override
    public IPokemonConfigManager getPokemonConfigManager() {
        return PokemonConfigManager.INSTANCE;
    }

    @Override
    public PokeNPCEntityWrapperFactory<?> getPokeNPCEntityWrapperFactory() {
        return PokeNPCEntityWrapperFactoryImpl.INSTANCE;
    }

    @Override
    public PokeBallEntityManager<?> getPokeBallEntityManager() {
        return V16PokeBallEntityManager.INSTANCE;
    }
}
