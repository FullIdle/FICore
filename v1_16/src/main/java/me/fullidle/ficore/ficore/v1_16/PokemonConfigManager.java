package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import me.fullidle.ficore.ficore.common.api.pokemon.IPokemonConfigManager;

public class PokemonConfigManager implements IPokemonConfigManager {
    public static PokemonConfigManager INSTANCE = new PokemonConfigManager();

    @Override
    public int getPokeMaxLevel() {
        return PixelmonConfigProxy.getGeneral().getMaxLevel();
    }

    @Override
    public int getComputerBoxes() {
        return PixelmonConfigProxy.getStorage().getComputerBoxes();
    }
}
