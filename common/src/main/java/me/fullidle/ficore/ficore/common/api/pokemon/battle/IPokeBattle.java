package me.fullidle.ficore.ficore.common.api.pokemon.battle;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface IPokeBattle {
    Collection<Player> getPlayers();

    /**
     * 停止对局
     */
    void end();
}
