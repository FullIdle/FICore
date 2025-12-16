package me.fullidle.ficore.ficore.v1_21;

import lombok.SneakyThrows;
import lombok.val;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.breeds.IBreedLogic;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.*;
import org.bukkit.plugin.Plugin;

public class V1_21 extends V1_version {
    public boolean hasPokemon;

    @SneakyThrows
    public V1_21() {
        FIData.V1_version = this;

        try {
            Class.forName("com.pixelmonmod.pixelmon.Pixelmon");
            hasPokemon = true;
            //注册一个
            val pixelmonEventBus = com.pixelmonmod.pixelmon.Pixelmon.EVENT_BUS;
            pixelmonEventBus.addListener(com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent.Pre.class, PixelmonListener::onBattleStarted);
            pixelmonEventBus.addListener(com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent.class, PixelmonListener::onBattleEnd);
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
        FIData.plugin.getLogger().warning("1.21.1的ForgeEvent只支持neoforge的!");
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
}
