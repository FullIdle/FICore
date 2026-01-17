package me.fullidle.ficore.ficore.common.api.pokemon.pokeball;

import me.fullidle.ficore.ficore.common.bukkit.entity.EntityWrapper;

public abstract class PokeBallEntityWrapper<T> extends EntityWrapper<T> {
    public PokeBallEntityWrapper(T original) {
        super(original);
    }
}
