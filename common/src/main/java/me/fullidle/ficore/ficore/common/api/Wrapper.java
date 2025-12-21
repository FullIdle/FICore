package me.fullidle.ficore.ficore.common.api;

public abstract class Wrapper<T> {
    /**
     * 获取包裹器原对象
     */
    public abstract T getOriginal();

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
