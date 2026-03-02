package me.fullidle.ficore.ficore.common.api.pokemon.npc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface PokeNPCEntityWrapperFactory<T> {
    PokeNPCEntityWrapper<T> create(T entity);
    /**
     * 判断宝可梦是否是宝可梦NPC实体
     */
    boolean isPokeNPCEntity(Entity entity);

    /**
     * {@link #isPokeNPCEntity(Entity)} 为 true 时，宝可梦NPC实体才能转
     * @return 宝可梦转后的实体，非空
     * @throws IllegalArgumentException 如果不是宝可梦NPC实体
     */
    PokeNPCEntityWrapper<T> asPokeNPCEntity(Entity entity);

    /**
     * 直接创建一个NPC实体报错，会顺带创建NPC实体
     */
    PokeNPCEntityWrapper<T> create(Location location);
}
