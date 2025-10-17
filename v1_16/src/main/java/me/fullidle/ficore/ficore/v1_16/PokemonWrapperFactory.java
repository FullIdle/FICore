package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class PokemonWrapperFactory implements IPokemonWrapperFactory<Pokemon> {
    public static final PokemonWrapperFactory INSTANCE = new PokemonWrapperFactory();

    @Override
    public IPokemonWrapper<Pokemon> create(Pokemon original) {
        return new PokemonWrapper(original);
    }

    @Override
    public IPokemonWrapper<Pokemon> create(ISpeciesWrapper<?> speciesWrapper) {
        val pokemon = PokemonFactory.create(((SpeciesWrapperFactory.SpeciesWrapper) speciesWrapper).getOriginal());
        return create(pokemon);
    }

    @Getter
    public static class PokemonWrapper extends IPokemonWrapper<Pokemon> {
        private final Pokemon original;

        public PokemonWrapper(Pokemon original) {
            this.original = original;
        }

        @Override
        public Entity getEntity() {
            return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()), ((net.minecraft.server.v1_16_R3.Entity) (Object) this.getOriginal().getPixelmonEntity().orElse(null)));
        }

        @Override
        public ISpeciesWrapper<?> getSpecies() {
            return ((SpeciesWrapperFactory) FIData.V1_version.getSpeciesWrapperFactory()).create(this.getOriginal().getSpecies());
        }

        @Override
        public String getName() {
            return getOriginal().getSpecies().getName();
        }

        @Override
        public String getTranslatedName() {
            return getOriginal().getLocalizedName();
        }

        @Override
        public Class<Pokemon> getType() {
            return Pokemon.class;
        }
    }
}
