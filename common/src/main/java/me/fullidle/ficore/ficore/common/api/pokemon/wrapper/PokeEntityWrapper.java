package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import me.fullidle.ficore.ficore.common.api.AbstractWrapper;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

/**
 * @param <T> 类型一般情况下一改是继承 minecraft 的 Entity 而非 {@link org.bukkit.entity.Entity}
 */
public abstract class PokeEntityWrapper<T> extends AbstractWrapper<T> {
    public PokeEntityWrapper(T wrapped) {
        super(wrapped);
    }

    public Entity asBukkitEntity() {
        return CraftEntity.getEntity(this.getOriginal());
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
