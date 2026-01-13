package me.fullidle.ficore.ficore.common.api.pokemon.battle.actor;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * 管理器由 {@link me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager} 来提供
 */
public interface ActorManager<T> {
    /**
     * 通过实体创建一个对局角色
     * @param entity 实体对象
     * @return 新创建的角色
     * @throws IllegalArgumentException 如果实体不是被支持的实体类型时报错
     */
    @NotNull
    Actor<?> create(Entity entity) throws UnsupportedOperationException;

    /**
     * 包裹一层
     * @param t 实体对象
     * @return 新创建的角色
     */
    @NotNull
    Actor<T> wrap(T t);

    /**
     * 获取一个已在某场对局中的角色
     * @param entity 实体对象
     * @return 已在某场对局中的角色
     */
    @Nullable
    Actor<?> getActor(Entity entity);

    /**
     * @see #getActor(Entity)
     */
    @Nullable
    default Actor<?> getActor(UUID uuid) {
        val entity = Bukkit.getEntity(uuid);
        if (entity == null) return null;
        return getActor(entity);
    }
}
