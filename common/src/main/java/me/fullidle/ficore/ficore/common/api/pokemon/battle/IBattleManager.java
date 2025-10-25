package me.fullidle.ficore.ficore.common.api.pokemon.battle;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBattleManager {
    /**
     * 开始一场对局
     * @param p1 参与者1
     * @param p2 参与者2
     */
    @NotNull
    IPokeBattle start(Player p1, Player p2);

    /**
     * 获取一场对局
     * @param player 参与者
     */
    @Nullable
    IPokeBattle getBattle(Player player);
}
