package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import java.util.Collection;

public interface ISpeciesWrapperFactory<T> {
    /**
     * 通过名字创建
     */
    ISpeciesWrapper<T> create(String name) throws IllegalArgumentException;
    /**
     * 直接通过原对象创建
     */
    ISpeciesWrapper<T> create(T original);
    /**
     * 通过全国编号创建
     */
    ISpeciesWrapper<T> create(int dex) throws IllegalArgumentException;
    /**
     * 获取所有物种
     */
    Collection<ISpeciesWrapper<?>> getAll();
}
