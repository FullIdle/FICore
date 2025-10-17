package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import me.fullidle.ficore.ficore.common.api.Wrapper;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class IPokemonWrapper<T> extends Wrapper<T> {
    private final T original;

    public IPokemonWrapper(T original) {
        this.original = original;
    }

    @Override
    public T getOriginal() {
        return original;
    }

    /*==>abstract<==*/
    /**
     * 获取宝可梦生成的实体
     * @return 如果不存在则会空
     */
    public abstract Entity getEntity();

    public abstract Entity spawnEntity(Location location);

    /**
     * 获取指定物种包装
     * {@code FICoreAPI.createSpeciesWrapper(species)}
     */
    public abstract ISpeciesWrapper<?> getSpecies();
    /**
     * 获取宝可梦的名字(非中文)
     */
    public abstract String getName();

    /**
     * 获取宝可梦翻译名
     */
    public abstract String getTranslatedName();

    public abstract void givePlayer(Player player);
}
