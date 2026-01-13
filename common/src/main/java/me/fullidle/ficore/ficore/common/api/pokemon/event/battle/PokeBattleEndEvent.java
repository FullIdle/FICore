package me.fullidle.ficore.ficore.common.api.pokemon.event.battle;

import lombok.Getter;
import lombok.Setter;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * 宝可梦对战事件
 * 结束时触发
 */
@Getter
@Setter
public class PokeBattleEndEvent extends Event {
    @Getter
    public static final HandlerList handlerList = new HandlerList();

    /**
     * 对战的战斗
     */
    private final IPokeBattle<?> battle;
    /**
     * 胜利者
     */
    private final Collection<Actor<?>> winners;
    /**
     * 失败者
     */
    private final Collection<Actor<?>> losers;

    public PokeBattleEndEvent(IPokeBattle<?> battle, Collection<Actor<?>> winners, Collection<Actor<?>> losers) {
        this.battle = battle;
        this.winners = winners;
        this.losers = losers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
