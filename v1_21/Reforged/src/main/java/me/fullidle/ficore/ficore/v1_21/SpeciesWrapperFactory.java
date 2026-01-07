package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.pokemon.AbilityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.Element;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory;

import java.util.*;
import java.util.stream.Collectors;

public class SpeciesWrapperFactory implements ISpeciesWrapperFactory<Species> {
    public static final SpeciesWrapperFactory INSTANCE = new SpeciesWrapperFactory();
    private final static Map<Species, SpeciesWrapper> cache = new HashMap<>();

    public static SpeciesWrapper getCacheOrCreate(Species es) {
        return cache.computeIfAbsent(es, SpeciesWrapper::new);
    }

    @Override
    public SpeciesWrapper create(String name) throws IllegalArgumentException {
        val es = PixelmonSpecies.fromName(name).getValueUnsafe();
        if (es == null) throw new IllegalArgumentException("Unknown species name: " + name);
        return getCacheOrCreate(es);
    }

    @Override
    public SpeciesWrapper create(Species original) {
        return getCacheOrCreate(original);
    }

    @Override
    public SpeciesWrapper create(int dex) throws IllegalArgumentException {
        val es = PixelmonSpecies.fromDex(dex);
        if (es.isEmpty()) throw new IllegalArgumentException("Unknown species dex: " + dex);
        return getCacheOrCreate(es.get());
    }

    @Override
    public Collection<ISpeciesWrapper<?>> getAll() {
        if (PixelmonSpecies.getAll().size() <= cache.size()) return new ArrayList<>(cache.values());
        return PixelmonSpecies.getAll().stream().map(
                SpeciesWrapperFactory::getCacheOrCreate
        ).collect(Collectors.toList());
    }

    public static class SpeciesWrapper extends ISpeciesWrapper<Species> {
        public SpeciesWrapper(Species original) {
            super(original);
        }

        @Override
        public String getName() {
            return this.getOriginal().getName();
        }

        @Override
        public boolean isLegend() {
            return this.getOriginal().isLegendary();
        }

        /**
         * 1.12.2是没有这个东西的
         */
        @Override
        public boolean isMythical() {
            return this.getOriginal().isMythical();
        }

        @Override
        public int getGeneration() {
            return this.getOriginal().getGeneration();
        }

        @Override
        public boolean isUltra() {
            return this.getOriginal().isUltraBeast();
        }

        @Override
        public int getDex() {
            return this.getOriginal().getDex();
        }

        @Override
        public List<Element> getTypes() {
            return this.getOriginal().getDefaultForm().getTypes().stream().map(e->Element.fromString(e.value().name().getString())).collect(Collectors.toList());
        }

        @Override
        public List<AbilityWrapper<?>> getAbilities() {
            return Arrays.stream(this.getOriginal().getDefaultForm().getAbilities().getAll()).map(me.fullidle.ficore.ficore.v1_21.AbilityWrapper::new).collect(Collectors.toUnmodifiableList());
        }

        @Override
        public Class<Species> getType() {
            return Species.class;
        }
    }
}
