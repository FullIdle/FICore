package me.fullidle.ficore.ficore.common.api.pokemon.battle;

import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBattleManager<T> {
    /**
     * 开始一场对局 是直接开始
     *
     * @param p1 参与者1
     * @param p2 参与者2
     */
    @NotNull
    default IPokeBattle<T> start(Player p1, Player p2) {
        val pb = create(p1, p2);
        pb.start();
        return pb;
    }

    /**
     * 创建一个对局
     * 注意:这个是创建不是包裹
     */
    @NotNull
    IPokeBattle<T> create(Player p1, Player p2);
    /**
     * 获取一场对局
     *
     * @param player 参与者
     */
    @Nullable
    IPokeBattle<T> getBattle(Player player);

    /**
     * 包裹一个环境下的对局
     * 注意:包裹有条件，必须是1v1且是pvp的对局其他对局包裹后对后续操作容易出现问题
     */
    IPokeBattle<T> wrapper(T battle);

    /**
     * 创建PVP对局查询界面 (如: 规则定义界面等，如果没有环境下不支持该方法，则该方法视作 {@link #start(Player, Player)})
     * 该方法是即刻的，一旦执行玩家将直接看到界面
     */
    default void createQuery(Player p1, Player p2) {
        start(p1, p2);
    }
}
