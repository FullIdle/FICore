package me.fullidle.ficore.ficore.v1_21;

import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.breeds.IBreedLogic;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
import org.bukkit.plugin.Plugin;

public class V1_21 extends V1_version {
    public V1_21() {
        FIData.V1_version = this;
    }

    @Override
    public String getVersion() {
        return "1.21.1";
    }

    @SneakyThrows
    @Override
    public void registerForgeEvent() {
        FIData.plugin.getLogger().warning("1.21.1");
    }

    @Override
    public void register(Plugin plugin, Object bus, Object target) {
        //TODO
    }

    @Override
    public void unregisterAllListener(Plugin plugin) {
        //TODO
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
    public IBattleManager getBattleManager() {
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
}
