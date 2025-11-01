package me.fullidle.ficore.ficore.common.api;

import lombok.Getter;

@Getter
public abstract class AbstractWrapper<T> extends Wrapper<T> {
    private final T original;
    private final Class<T> type;

    public AbstractWrapper(T wrapped) {
        this.original = wrapped;
        this.type = (Class<T>) wrapped.getClass();
    }
}
