package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import me.fullidle.ficore.ficore.common.api.Wrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.Stats;
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

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

    /**
     * 种族值
     */
    public abstract Map<Stats, Integer> getStats();

    /**
     * 是否是神兽 不包含幻兽
     * 1.12.2重铸则包含
     */
    public abstract boolean isLegend();
    /**
     * 是否是幻兽
     */
    public abstract boolean isMythical();
    /**
     * 是否是究极异兽
     */
    public abstract boolean isUltra();

    /**
     * 当主人不是玩家时，该值应该为null
     * @return 宝可梦主人
     */
    @Nullable
    public abstract OfflinePlayer getOwner();

    /**
     * 非玩家也可以是主人
     */
    @Nullable
    public abstract UUID getUniqueId();

    /**
     * 获取所在存储 TODO暂时不实现!
     */
    public abstract IPokeStorageWrapper<?> getStorage();

    /**
     * 所在存储的位置
     */
    public abstract StoragePos getStoragePos();

    public abstract boolean isEgg();
}
