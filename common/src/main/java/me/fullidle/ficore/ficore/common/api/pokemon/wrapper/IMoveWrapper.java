package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import me.fullidle.ficore.ficore.common.api.Wrapper;

public abstract class IMoveWrapper<T> extends Wrapper<T> {
    public IMoveWrapper(T original) {
        super(original);
    }

    public abstract String getName();
}
