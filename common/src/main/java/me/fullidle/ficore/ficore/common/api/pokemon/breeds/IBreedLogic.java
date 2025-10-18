package me.fullidle.ficore.ficore.common.api.pokemon.breeds;

import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;

public interface IBreedLogic {
    IPokemonWrapper<?> tryMakeEgg(IPokemonWrapper<?> parent1, IPokemonWrapper<?> parent2);

    boolean canMekeEgg(IPokemonWrapper<?> parent1, IPokemonWrapper<?> parent2);

    default IPokemonWrapper<?> makeEgg(IPokemonWrapper<?> parent1, IPokemonWrapper<?> parent2) {
        if (!canMekeEgg(parent1, parent2)) return null;
        return tryMakeEgg(parent1, parent2);
    }
}
