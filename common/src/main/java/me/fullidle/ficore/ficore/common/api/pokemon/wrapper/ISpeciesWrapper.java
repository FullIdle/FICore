package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.Wrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.Element;

import java.util.List;

@Getter
public abstract class ISpeciesWrapper<T> extends Wrapper<T> {
    public ISpeciesWrapper(T original) {
        super(original);
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

    /**
     * 获取物种的属性
     */
    public abstract List<Element> getTypes();
}
