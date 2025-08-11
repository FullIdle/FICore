package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import me.fullidle.ficore.ficore.common.api.Wrapper;

public abstract class ISpeciesWrapper<T> extends Wrapper<T> {
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
}
