package me.fullidle.ficore.ficore.common.api.pokemon.pools;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class CompositePool implements SpeciesWrapperPool {
    private final Collection<SpeciesWrapperPool> pools;

    public CompositePool(SpeciesWrapperPool... pools) {
        this.pools = Lists.newArrayList(pools);
    }
    public CompositePool(Collection<SpeciesWrapperPool> pools) {
        this.pools = new ArrayList<>(pools);
    }

    @Override
    public Collection<ISpeciesWrapper<?>> values() {
        return pools.stream().flatMap(pool -> pool.values().stream()).collect(Collectors.toList());
    }

    @Override
    public boolean contains(ISpeciesWrapper<?> wrapper) {
        for (SpeciesWrapperPool pool : pools) if (pool.contains(wrapper)) return true;
        return false;
    }
}
