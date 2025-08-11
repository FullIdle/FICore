package me.fullidle.ficore.ficore.common.api.pokemon.pools;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;

import java.util.Collection;
import java.util.stream.Collectors;

public class LegendPool implements SpeciesWrapperPool {
    public static LegendPool INSTANCE = new LegendPool();

    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return FIData.V1_version.getSpeciesWrapperFactory().getAll()
                .stream().filter(ISpeciesWrapper::isLegend).collect(Collectors.toList());
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> wrapper) {
        return wrapper.isLegend();
    }
}
