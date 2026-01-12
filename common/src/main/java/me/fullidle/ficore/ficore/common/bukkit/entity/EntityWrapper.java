package me.fullidle.ficore.ficore.common.bukkit.entity;

import me.fullidle.ficore.ficore.common.api.Wrapper;
import org.bukkit.entity.Entity;

public abstract class EntityWrapper<T> extends Wrapper<T> {
    public EntityWrapper(T original) {
        super(original);
    }

    public Entity asBukkitEntity() {
        return CraftEntity.getEntity(this.getOriginal());
    }
}
