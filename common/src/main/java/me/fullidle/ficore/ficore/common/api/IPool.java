package me.fullidle.ficore.ficore.common.api;

import java.util.Collection;

/**
 * 池子接口
 */
public interface IPool<T> {
    Collection<T> values();
    boolean contains(T t);
}
