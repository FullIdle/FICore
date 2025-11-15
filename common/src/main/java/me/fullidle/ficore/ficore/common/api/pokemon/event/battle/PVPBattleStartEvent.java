package me.fullidle.ficore.ficore.common.api.pokemon.event.battle;

import lombok.Getter;
import lombok.Setter;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 只针对 1v1 且  pvp 对局开始前事件触发
 */
@Getter
@Setter
public class PVPBattleStartEvent extends Event implements Cancellable {
    @Getter
    public static final HandlerList handlerList = new HandlerList();


    private boolean cancelled = false;
    private final IPokeBattle<?> battle;
    private final Player player1;
    private final Player player2;

    public PVPBattleStartEvent(IPokeBattle<?> battle, Player player1, Player player2) {
        this.battle = battle;
        this.player1 = player1;
        this.player2 = player2;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
