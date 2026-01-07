package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import me.fullidle.ficore.ficore.common.api.pokemon.IPokemonConfigManager;

import java.util.UUID;

public class PokemonConfigManager implements IPokemonConfigManager {
    public static PokemonConfigManager INSTANCE = new PokemonConfigManager();
    public static final UUID uuid = UUID.randomUUID();

    @Override
    public int getPokeMaxLevel() {
        return PixelmonConfigProxy.getGeneral().getMaxLevel();
    }

    @Override
    public int getComputerBoxes() {
        return PixelmonConfigProxy.getStorage().getComputerBoxes(uuid);
    }
}
