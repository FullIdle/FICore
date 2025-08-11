package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import me.fullidle.ficore.ficore.common.api.Wrapper;
import org.bukkit.entity.Entity;

public abstract class IPokemonWrapper<T> extends Wrapper<T> {
    /*==>abstract<==*/

    /**
     * 获取宝可梦生成的实体
     * @return 如果不存在则会空
     */
    public abstract Entity getEntity();
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
}
