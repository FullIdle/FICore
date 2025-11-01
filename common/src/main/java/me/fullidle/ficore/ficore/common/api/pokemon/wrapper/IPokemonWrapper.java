package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import me.fullidle.ficore.ficore.common.api.Wrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.Gender;
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
     *
     * @return 如果不存在则会空
     */
    public abstract PokeEntityWrapper<?> getEntity();

    public abstract PokeEntityWrapper<?> spawnEntity(Location location);

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
     * 种族值 无法修改
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
     *
     * @return 宝可梦主人
     */
    @Nullable
    public abstract OfflinePlayer getOwner();

    /**
     * 非玩家也可以是主人
     */
    @Nullable
    public abstract UUID getOwnerUUID();

    /**
     * 获取所在存储 TODO暂时不实现!
     */
    public abstract IPokeStorageWrapper<?> getStorage();

    /**
     * 所在存储的位置
     */
    public abstract StoragePos getStoragePos();

    /**
     * 是否是蛋
     */
    public abstract boolean isEgg();

    /**
     * 是否是闪光
     */
    public abstract boolean isShiny();

    /**
     * 设置闪光
     */
    public abstract void setShiny(boolean shiny);

    /**
     * 获取个体值数据
     *
     * @return 返回值不应该直接修改
     */
    public abstract Map<Stats, Integer> getIVs();

    /**
     * 获取努力值数据
     *
     * @return 返回值不应该直接修改
     */
    public abstract Map<Stats, Integer> getEVs();

    /**
     * 设置努力值
     *
     * @param type  类型
     * @param value 值
     */
    public abstract void setEV(Stats type, int value);

    /**
     * 设置个体值
     *
     * @param type  类型
     * @param value 值
     */
    public abstract void setIV(Stats type, int value);

    /**
     * 获取性别
     *
     * @return 性别
     */
    public abstract Gender getGender();

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public abstract void setGender(Gender gender);

    /**
     * 该宝可梦的UUID
     *
     * @return 宝可梦UUID
     */
    public abstract UUID getUUID();

    /**
     * 设置宝可梦的UUID
     *
     * @param uuid 宝可梦UUID
     */
    public abstract void setUUID(UUID uuid);

    /**
     * 获取血量
     *
     * @return 血量
     */
    public abstract int getHealth();

    /**
     * 设置血量
     *
     * @param health 血量
     */
    public abstract void setHealth(int health);

    /**
     * 是否在牧场内 大概也就1.12.2重铸有了
     * @return 是否在牧场内
     */
    public boolean inRanch() {
        return false;
    }

    /**
     * 是否有主人
     * @return 是否有主人
     */
    public boolean hasOwner() {
        return this.getOwnerUUID() != null;
    }
}
