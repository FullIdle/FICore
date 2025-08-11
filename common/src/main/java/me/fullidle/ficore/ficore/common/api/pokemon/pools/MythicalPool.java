package me.fullidle.ficore.ficore.common.api.pokemon.pools;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;

import java.util.Collection;
import java.util.stream.Collectors;

public class MythicalPool implements SpeciesWrapperPool {
    public static final MythicalPool INSTANCE = new MythicalPool();

    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return FIData.V1_version.getSpeciesWrapperFactory().getAll().stream()
                .filter(ISpeciesWrapper::isMythical).collect(Collectors.toList());
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> wrapper) {
        return wrapper.isMythical();
    }
}
