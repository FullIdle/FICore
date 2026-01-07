package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.pokemon.Element;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory;

import java.util.*;
import java.util.stream.Collectors;

public class SpeciesWrapperFactory implements ISpeciesWrapperFactory<EnumSpecies> {
    public static final SpeciesWrapperFactory INSTANCE = new SpeciesWrapperFactory();
    private final static Map<EnumSpecies, SpeciesWrapper> cache = new HashMap<>();

    public static SpeciesWrapper getCacheOrCreate(EnumSpecies es) {
        return cache.computeIfAbsent(es, SpeciesWrapper::new);
    }

    @Override
    public SpeciesWrapper create(String name) throws IllegalArgumentException {
        val es = EnumSpecies.getFromNameAnyCase(name);
        if (es == null) throw new IllegalArgumentException("Unknown species name: " + name);
        return getCacheOrCreate(es);
    }

    @Override
    public SpeciesWrapper create(EnumSpecies original) {
        return getCacheOrCreate(original);
    }

    @Override
    public SpeciesWrapper create(int dex) throws IllegalArgumentException {
        val es = EnumSpecies.getFromDex(dex);
        if (es == null) throw new IllegalArgumentException("Unknown species dex: " + dex);
        return getCacheOrCreate(es);
    }

    @Override
    public Collection<ISpeciesWrapper<?>> getAll() {
        if (EnumSpecies.values().length <= cache.size()) return new ArrayList<>(cache.values());
        return Arrays.stream(EnumSpecies.values()).map(
                SpeciesWrapperFactory::getCacheOrCreate
        ).collect(Collectors.toList());
    }

    public static class SpeciesWrapper extends ISpeciesWrapper<EnumSpecies> {
        public SpeciesWrapper(EnumSpecies original) {
            super(original);
        }

        @Override
        public String getName() {
            return this.getOriginal().getPokemonName();
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
            return this.getOriginal().isLegendary();
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
            return this.getOriginal().getNationalPokedexInteger();
        }

        @Override
        public List<Element> getTypes() {
            return this.getOriginal().getBaseStats().getTypeList().stream().map(e->Element.fromString(e.name())).collect(Collectors.toList());
        }

        @Override
        public Class<EnumSpecies> getType() {
            return EnumSpecies.class;
        }
    }
}
