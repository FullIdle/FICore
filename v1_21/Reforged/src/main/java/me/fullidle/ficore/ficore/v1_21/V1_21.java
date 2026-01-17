package me.fullidle.ficore.ficore.v1_21;

import lombok.SneakyThrows;
import lombok.val;
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
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class V1_21 extends V1_version {
    public boolean hasPokemon;

    @SneakyThrows
    public V1_21() {
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
        return hasPokemon;
    }

    @Override
    public String getVersion() {
        return "1.21.1";
    }

    @SneakyThrows
    @Override
    public void registerForgeEvent() {
        FIData.plugin.getLogger().warning("1.21.1的没有ForgeEvent只支持neoforge的!");
        if (hasPokemon()) PixelmonListener.register();
    }

    @Override
    public void register(Plugin plugin, Object bus, Object target) {
        if (bus instanceof net.neoforged.bus.api.IEventBus eventBus) {
            val list = FIData.listenerList.computeIfAbsent(plugin, k -> new HashMap<>()).computeIfAbsent(0, k -> new ArrayList<>());
            eventBus.register(target);
            list.add(Map.entry(bus, target));
        }
    }

    @Override
    public void unregisterAllListener(Plugin plugin) {
        if (FIData.plugin.equals(plugin)) PixelmonListener.unregister();
        val remove = FIData.listenerList.remove(plugin);
        if (remove != null) {
            val list = remove.get(0);
            if (list == null) return;
            for (Object o : list)
                if (o instanceof Map.Entry<?, ?> entry)
                    if (entry.getKey() instanceof net.neoforged.bus.api.IEventBus bus) bus.unregister(entry.getValue());
        }
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
        return V21PokeBallEntityManager.INSTANCE;
    }
}
