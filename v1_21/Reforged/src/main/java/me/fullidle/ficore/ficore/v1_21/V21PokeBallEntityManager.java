package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.entities.pokeballs.PokeBallEntity;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityManager;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityWrapper;

public class V21PokeBallEntityManager implements PokeBallEntityManager<PokeBallEntity> {
    public static final V21PokeBallEntityManager INSTANCE = new V21PokeBallEntityManager();

    @Override
    public PokeBallEntityWrapper<PokeBallEntity> wrap(PokeBallEntity pokeBallEntity) {
        return new V21PokeBallEntityWrapper(pokeBallEntity);
    }

    public static class V21PokeBallEntityWrapper extends PokeBallEntityWrapper<PokeBallEntity> {
        public V21PokeBallEntityWrapper(PokeBallEntity original) {
            super(original);
        }

        @Override
        public Class<PokeBallEntity> getType() {
            return PokeBallEntity.class;
        }
    }
}
