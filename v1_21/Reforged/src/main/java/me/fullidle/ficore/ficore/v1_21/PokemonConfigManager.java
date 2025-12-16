package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonConfigManager;

public class PokemonConfigManager implements IPokemonConfigManager {
    public static PokemonConfigManager INSTANCE = new PokemonConfigManager();

    @Override
    public int getPokeMaxLevel() {
        return PixelmonConfigProxy.getGeneral().getMaxLevel();
    }
}
