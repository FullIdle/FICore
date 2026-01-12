package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import me.fullidle.ficore.ficore.common.bukkit.entity.EntityWrapper;

/**
 * @param <T> 类型一般情况下一改是继承 minecraft 的 Entity 而非 {@link org.bukkit.entity.Entity}
 */
public abstract class PokeEntityWrapper<T> extends EntityWrapper<T> {
    public PokeEntityWrapper(T wrapped) {
        super(wrapped);
    }

    /**
     * 是否是Boss
     * @return 是否是Boss
     */
    public abstract boolean isBoss();

    /**
     * 获取宝可梦包裹
     * @return 宝可梦包裹
     */
    public abstract IPokemonWrapper<?> getPokemon();

    /**
     * 可销毁
     * @return 是否可销毁
     */
    public abstract boolean canDespawn();

    public abstract boolean inBattle();
}
