package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.breeds.IBreedLogic;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;

public class BreedLogic implements IBreedLogic {
    public static BreedLogic INSTANCE = new BreedLogic();

    @Override
    public IPokemonWrapper<?> tryMakeEgg(IPokemonWrapper<?> parent1, IPokemonWrapper<?> parent2) {
        val pokemon = com.pixelmonmod.pixelmon.util.helpers.BreedLogic.makeEgg((Pokemon) parent1.getOriginal(), (Pokemon) parent2.getOriginal());
        if (pokemon == null) return null;
        return ((PokemonWrapperFactory) FIData.V1_version.getPokemonWrapperFactory()).create(pokemon);
    }

    @Override
    public boolean canMekeEgg(IPokemonWrapper<?> parent1, IPokemonWrapper<?> parent2) {
        return com.pixelmonmod.pixelmon.util.helpers.BreedLogic.canBreed((Pokemon) parent1.getOriginal(), (Pokemon) parent2.getOriginal());
    }
}
