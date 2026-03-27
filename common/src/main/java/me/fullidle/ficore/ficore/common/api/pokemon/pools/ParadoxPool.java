package me.fullidle.ficore.ficore.common.api.pokemon.pools;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;

import java.util.Collection;
import java.util.stream.Collectors;

public class ParadoxPool implements SpeciesWrapperPool {
    public static final ParadoxPool INSTANCE = new ParadoxPool();

    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return FIData.V1_version.getSpeciesWrapperFactory().getAll().stream()
                .filter(ISpeciesWrapper::isParadox).collect(Collectors.toList());
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> speciesWrapper) {
        return speciesWrapper.isParadox();
    }
}
