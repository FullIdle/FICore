package me.fullidle.ficore.ficore.common.api.pokemon.battle.actor;

import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.Wrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import org.bukkit.entity.Entity;

import java.util.Collection;

/**
 * 对局内角色
 */
@Getter
public abstract class Actor<T> extends Wrapper<T> {
    public Actor(T original) {
        super(original);
    }

    /**
     * 角色实体
     */
    public abstract Entity getEntity();

    /**
     * 宝可梦队伍
     */
    public abstract Collection<IPokemonWrapper<?>> getTeam();

    /**
     * 当前活跃的宝可梦
     */
    public abstract Collection<IPokemonWrapper<?>> getCurrents();
}
