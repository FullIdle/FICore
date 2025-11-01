package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.Wrapper;

@Getter
public abstract class ISpeciesWrapper<T> extends Wrapper<T> {
    private final T original;

    public ISpeciesWrapper(T original) {
        this.original = original;
    }

    /**
     * 宝可梦物种名
     */
    public abstract String getName();

    /**
     * 是否是神兽
     */
    public abstract boolean isLegend();

    /**
     * 是否是幻兽
     */
    public abstract boolean isMythical();

    /**
     * 获取其是第几世代
     */
    public abstract int getGeneration();

    /**
     * 是否是究极异兽
     */
    public abstract boolean isUltra();

    /**
     * 宝可梦编号
     */
    public abstract int getDex();
}
