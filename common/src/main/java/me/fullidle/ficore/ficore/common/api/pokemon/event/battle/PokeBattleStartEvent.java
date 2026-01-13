package me.fullidle.ficore.ficore.common.api.pokemon.event.battle;

import lombok.Getter;
import lombok.Setter;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 宝可梦对战事件
 * 开始前触发
 */
@Getter
@Setter
public class PokeBattleStartEvent extends Event implements Cancellable {
    @Getter
    public static final HandlerList handlerList = new HandlerList();

    private boolean cancelled = false;

    /**
     * 对战的战斗
     */
    private final IPokeBattle<?> battle;
    /**
     * 对战的方1
     */
    private final Actor<?>[] side1;
    /**
     * 对战的方2
     */
    private final Actor<?>[] side2;

    public PokeBattleStartEvent(IPokeBattle<?> battle, Actor<?>[] side1, Actor<?>[] side2) {
        this.battle = battle;
        this.side1 = side1;
        this.side2 = side2;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
