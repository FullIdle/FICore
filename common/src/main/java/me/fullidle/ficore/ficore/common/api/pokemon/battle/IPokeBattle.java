package me.fullidle.ficore.ficore.common.api.pokemon.battle;

import me.fullidle.ficore.ficore.common.api.Wrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class IPokeBattle<T> extends Wrapper<T> {
    public IPokeBattle(T original) {
        super(original);
    }

    /**
     * 参与的玩家
     * @return 参与的玩家
     */
    public abstract Collection<Player> getPlayers();

    /**
     * 对局内所有玩家角色
     */
    public abstract Collection<Actor<?>> getPlayerActors();

    /**
     * 对局内所有角色
     */
    public abstract Collection<Actor<?>> getActors();

    /**
     * 停止对局
     */
    public abstract void end();

    /**
     * 开始对战
     */
    public abstract void start();
}
