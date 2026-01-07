package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import me.fullidle.ficore.ficore.common.api.pokemon.IPokemonConfigManager;

public class PokemonConfigManager implements IPokemonConfigManager {
    public static PokemonConfigManager INSTANCE = new PokemonConfigManager();

    @Override
    public int getPokeMaxLevel() {
        return PixelmonConfig.maxLevel;
    }

    @Override
    public int getComputerBoxes() {
        return PixelmonConfig.computerBoxes;
    }
}
