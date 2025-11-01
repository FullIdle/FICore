package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class VPokeEntityWrapperFactory implements PokeEntityWrapperFactory<EntityPixelmon> {
    public static VPokeEntityWrapperFactory INSTANCE = new VPokeEntityWrapperFactory();

    @Override
    public PokeEntityWrapper<EntityPixelmon> create(EntityPixelmon entity) {
        return new VPokeEntityWrapper(entity);
    }

    @Override
    public boolean isPokeEntity(Entity entity) {
        return CraftEntity.getHandle(entity) instanceof EntityPixelmon;
    }

    @Override
    public PokeEntityWrapper<EntityPixelmon> asPokeEntity(Entity entity) {
        if (!isPokeEntity(entity)) throw new IllegalArgumentException("Entity is not a PokeEntity");
        return create(((EntityPixelmon) CraftEntity.getHandle(entity)));
    }

    public static class VPokeEntityWrapper extends PokeEntityWrapper<EntityPixelmon> {
        public VPokeEntityWrapper(EntityPixelmon wrapped) {
            super(wrapped);
        }

        @Override
        public boolean isBoss() {
            return this.getOriginal().isBossPokemon();
        }

        @Override
        public IPokemonWrapper<?> getPokemon() {
            return ((PokemonWrapperFactory) FIData.V1_version.getPokemonWrapperFactory()).create(this.getOriginal().getPokemonData());
        }

        @Override
        public boolean canDespawn() {
            return this.getOriginal().canDespawn();
        }

        @Override
        public boolean inBattle() {
            return this.getOriginal().battleController != null;
        }
    }
}
