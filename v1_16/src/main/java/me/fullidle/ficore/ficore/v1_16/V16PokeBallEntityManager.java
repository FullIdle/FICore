package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.entities.pokeballs.PokeBallEntity;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityManager;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityWrapper;

public class V16PokeBallEntityManager implements PokeBallEntityManager<PokeBallEntity> {
    public static final V16PokeBallEntityManager INSTANCE = new V16PokeBallEntityManager();

    @Override
    public PokeBallEntityWrapper<PokeBallEntity> wrap(PokeBallEntity pokeBallEntity) {
        return new V16PokeBallEntityWrapper(pokeBallEntity);
    }

    public static class V16PokeBallEntityWrapper extends PokeBallEntityWrapper<PokeBallEntity> {
        public V16PokeBallEntityWrapper(PokeBallEntity original) {
            super(original);
        }

        @Override
        public Class<PokeBallEntity> getType() {
            return PokeBallEntity.class;
        }
    }
}
