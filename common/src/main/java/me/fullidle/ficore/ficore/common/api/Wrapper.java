package me.fullidle.ficore.ficore.common.api;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class Wrapper<T> {
    private final T original;

    public Wrapper(T original) {
        this.original = Objects.requireNonNull(original, "Wrapper original object cannot be null");
    }

    public abstract Class<T> getType();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Wrapper) return this.getOriginal().equals(((Wrapper<?>) obj).getOriginal());
        if (getType().isInstance(obj)) return this.getOriginal().equals(obj);
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * getOriginal().hashCode() + getType().hashCode();
    }
}
