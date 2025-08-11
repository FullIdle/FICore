package me.fullidle.ficore.ficore.common.api.pokemon.pools;


import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;

import java.util.Collection;

public class AllPool implements SpeciesWrapperPool {
    public static final AllPool INSTANCE = new AllPool();

    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return FIData.V1_version.getSpeciesWrapperFactory().getAll();
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> wrapper) {
        return values().contains(wrapper);
    }
}
