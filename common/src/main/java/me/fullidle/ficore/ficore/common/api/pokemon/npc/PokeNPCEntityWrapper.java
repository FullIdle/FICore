package me.fullidle.ficore.ficore.common.api.pokemon.npc;

import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper;
import me.fullidle.ficore.ficore.common.bukkit.entity.EntityWrapper;

public abstract class PokeNPCEntityWrapper<T> extends EntityWrapper<T> {
    public PokeNPCEntityWrapper(T wrapped) {
        super(wrapped);
    }

    /**
     * 获取NPC的存储
     * 不一定有
     */
    public abstract IPokeStorageWrapper<?> getStorage();

    /**
     * 给NPC设置材质
     */
    public abstract void setTexture(String texture);
}
