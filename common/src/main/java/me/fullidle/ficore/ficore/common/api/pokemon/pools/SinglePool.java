package me.fullidle.ficore.ficore.common.api.pokemon.pools;


import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;

import java.util.Collection;
import java.util.Collections;

@Getter
public class SinglePool implements SpeciesWrapperPool {
    private final ISpeciesWrapper<?> species;

    public SinglePool(ISpeciesWrapper<?> species) {
        this.species = species;
    }

    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return Collections.singleton(species);
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> wrapper) {
        return this.species.equals(wrapper);
    }
}
