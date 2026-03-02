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
import org.bukkit.plugin.Plugin;

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
        try {
            ForgeEventRegistrar.registerForgeEvent();
        } catch (Exception e) {
            FIData.plugin.getLogger().warning("注册ForgeEvent失败,如果不是Forge端无需在意. 原因: " + e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public void register(Plugin plugin, Object bus, Object target) {
        try {
            ForgeEventRegistrar.register(plugin, bus, target);
        } catch (Exception e) {
            FIData.plugin.getLogger().warning("订阅Forge事件对象失败,如果不是Forge端无需在意. 原因: " + e.getMessage());
        }
    }

    @Override
    public void unregisterAllListener(Plugin plugin) {
        try {
            ForgeEventRegistrar.unregisterAllListener(plugin);
        } catch (Exception e) {
            FIData.plugin.getLogger().warning("取消订阅Forge事件对象失败,如果不是Forge端无需在意. 原因: " + e.getMessage());
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
        return V16PokeBallEntityManager.INSTANCE;
    }
}
