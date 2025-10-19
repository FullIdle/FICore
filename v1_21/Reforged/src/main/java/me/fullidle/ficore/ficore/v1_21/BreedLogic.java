package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.egg.BreedingLogicProxy;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.breeds.IBreedLogic;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;

public class BreedLogic implements IBreedLogic {
    public static BreedLogic INSTANCE = new BreedLogic();

    @Override
    public IPokemonWrapper<?> tryMakeEgg(IPokemonWrapper<?> parent1, IPokemonWrapper<?> parent2) {
        val opt = BreedingLogicProxy.makeEgg((Pokemon) parent1.getOriginal(), (Pokemon) parent2.getOriginal());
        return opt.map(pokemon -> ((PokemonWrapperFactory) FIData.V1_version.getPokemonWrapperFactory()).create(pokemon)).orElse(null);
    }

    @Override
    public boolean canMekeEgg(IPokemonWrapper<?> parent1, IPokemonWrapper<?> parent2) {
        return BreedingLogicProxy.canBreed((Pokemon) parent1.getOriginal(), (Pokemon) parent2.getOriginal());
    }
}
