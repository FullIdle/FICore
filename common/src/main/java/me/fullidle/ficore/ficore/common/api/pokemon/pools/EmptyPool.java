package me.fullidle.ficore.ficore.common.api.pokemon.pools;

import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;

import java.util.Collection;
import java.util.Collections;

public class EmptyPool implements SpeciesWrapperPool {
    public static final EmptyPool INSTANCE = new EmptyPool();
    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return Collections.emptyList();
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> wrapper) {
        return false;
    }
}
