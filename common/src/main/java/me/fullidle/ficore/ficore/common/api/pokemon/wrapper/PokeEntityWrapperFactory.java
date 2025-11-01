package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import org.bukkit.entity.Entity;

public interface PokeEntityWrapperFactory<T> {
    PokeEntityWrapper<T> create(T entity);

    /**
     * 判断宝可梦是否是宝可梦实体
     */
    boolean isPokeEntity(Entity entity);

    /**
     * {@link #isPokeEntity(Entity)} 为 true 时，返回宝可梦实体才能转
     * @return 返回值不为空
     * @throws IllegalArgumentException 如果不是宝可梦实体
     */
    PokeEntityWrapper<T> asPokeEntity(Entity entity);
}
