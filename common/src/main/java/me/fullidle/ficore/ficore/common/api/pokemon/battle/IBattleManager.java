package me.fullidle.ficore.ficore.common.api.pokemon.battle;

import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBattleManager {
    /**
     * 开始一场对局 是直接开始
     *
     * @param p1 参与者1
     * @param p2 参与者2
     */
    @NotNull
    default IPokeBattle start(Player p1, Player p2) {
        val pb = create(p1, p2);
        pb.start();
        return pb;
    }

    /**
     * 创建一个对局
     */
    @NotNull
    IPokeBattle create(Player p1, Player p2);

    /**
     * 获取一场对局
     *
     * @param player 参与者
     */
    @Nullable
    IPokeBattle getBattle(Player player);
}
