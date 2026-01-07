package me.fullidle.ficore.ficore.common.api.pokemon;

import me.fullidle.ficore.ficore.common.api.Wrapper;

public abstract class NatureWrapper<T> extends Wrapper<T> {
    public NatureWrapper(T original) {
        super(original);
    }

    public abstract String getName();
}
