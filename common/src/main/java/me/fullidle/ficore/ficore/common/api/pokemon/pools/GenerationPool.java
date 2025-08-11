package me.fullidle.ficore.ficore.common.api.pokemon.pools;


import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class GenerationPool implements SpeciesWrapperPool {
    private final int gen;

    public GenerationPool(int gen) {
        this.gen = gen;
    }

    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return FIData.V1_version.getSpeciesWrapperFactory().getAll().stream()
                .filter(wrapper -> wrapper.getGeneration() == this.gen)
                .collect(Collectors.toList());
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> wrapper) {
        return wrapper.getGeneration() == this.gen;
    }
}
