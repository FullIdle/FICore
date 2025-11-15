package me.fullidle.ficore.ficore.common.api.pokemon.event.battle;

import lombok.Getter;
import lombok.Setter;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.BattleResult;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * 只针对 1v1 且  pvp 对局结束时事件触发
 * 暂时没有考虑平局的情况
 */
@Getter
@Setter
public class PVPBattleEndEvent extends Event {
    @Getter
    public static final HandlerList handlerList = new HandlerList();

    private final IPokeBattle<?> battle;
    private final Map<Player, BattleResult> results;

    public PVPBattleEndEvent(IPokeBattle<?> battle, Map<Player, BattleResult> results) {
        this.battle = battle;
        this.results = Collections.unmodifiableMap(results);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
