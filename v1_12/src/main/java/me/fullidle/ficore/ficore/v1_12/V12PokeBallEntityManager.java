package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityManager;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityWrapper;

public class V12PokeBallEntityManager implements PokeBallEntityManager<EntityPokeBall> {
    public static final V12PokeBallEntityManager INSTANCE = new V12PokeBallEntityManager();

    @Override
    public PokeBallEntityWrapper<EntityPokeBall> wrap(EntityPokeBall pokeBallEntity) {
        return new V12PokeBallEntityWrapper(pokeBallEntity);
    }

    public static class V12PokeBallEntityWrapper extends PokeBallEntityWrapper<EntityPokeBall> {
        public V12PokeBallEntityWrapper(EntityPokeBall original) {
            super(original);
        }

        @Override
        public Class<EntityPokeBall> getType() {
            return EntityPokeBall.class;
        }
    }
}
