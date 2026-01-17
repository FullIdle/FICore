package me.fullidle.ficore.ficore.common.api.pokemon.pokeball;

public interface PokeBallEntityManager<T> {
    PokeBallEntityWrapper<T> wrap(T pokeBallEntity);
}