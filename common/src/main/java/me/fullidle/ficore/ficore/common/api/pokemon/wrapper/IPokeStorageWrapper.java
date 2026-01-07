package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.Wrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
public abstract class IPokeStorageWrapper<T> extends Wrapper<T> {
    public IPokeStorageWrapper(T original) {
        super(original);
    }

    abstract public IPokemonWrapper<?> get(StoragePos pos);

    /**
     * @param poke 为空时将看做删除
     */
    abstract public void set(StoragePos pos, @Nullable IPokemonWrapper<?> poke);

    abstract public void add(IPokemonWrapper<?> poke);

    /**
     * 获取所有宝可梦 非空!
     */
    abstract public List<IPokemonWrapper<?>> all();

    /**
     * 获取uuid 存储主人的uuid
     */
    abstract public UUID getUUID();
}
