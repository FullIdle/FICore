package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.BattleResult;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.ActorManager;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleEndEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleStartEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PokeBattleStartEvent;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PixelmonListener {
    public static void on(ForgeEvent event) {
        if (event.getForgeEvent() instanceof BattleStartedEvent) {
            val e = (BattleStartedEvent) event.getForgeEvent();
            val battleManager = ((BattleManager) FIData.V1_version.getBattleManager());
            val bc = battleManager.wrapper(e.bc);
            val side1 = new Actor<?>[e.participant1.length];
            val side2 = new Actor<?>[e.participant1.length];
            for (int i = 0; i < e.participant1.length; i++)
                side1[i] = ((ActorManager<BattleParticipant>) battleManager.getActorManager()).wrap(e.participant1[i]);
            for (int i = 0; i < e.participant2.length; i++)
                side2[i] = ((ActorManager<BattleParticipant>) battleManager.getActorManager()).wrap(e.participant2[i]);
            val startEvent = new PokeBattleStartEvent(bc, side1, side2);
            Bukkit.getPluginManager().callEvent(startEvent);
            if (startEvent.isCancelled()) {
                e.setCanceled(true);
                return;
            }

            if (e.bc.isPvP() && e.participant1.length == 1 && e.participant2.length == 1) {
                val bEvent = new PVPBattleStartEvent(
                        bc,
                        castPlayer(((PlayerParticipant) e.participant1[0]).player),
                        castPlayer(((PlayerParticipant) e.participant2[0]).player)
                );
                Bukkit.getPluginManager().callEvent(bEvent);
                if (bEvent.isCancelled()) {
                    e.setCanceled(true);
                    return;
                }
            }
        }
        if (event.getForgeEvent() instanceof BattleEndEvent) {
            val e = (BattleEndEvent) event.getForgeEvent();
            if (e.bc.isPvP() && e.bc.playerNumber == 2 && !e.abnormal && !e.cause.equals(EnumBattleEndCause.FORCE)) {
                val battleManager = ((BattleManager) FIData.V1_version.getBattleManager());
                val map = new HashMap<Player, BattleResult>();
                for (Map.Entry<BattleParticipant, BattleResults> entry : e.results.entrySet())
                    map.put(castPlayer(((PlayerParticipant) entry.getKey()).player), BattleResult.valueOf(entry.getValue().name()));
                Bukkit.getPluginManager().callEvent(new PVPBattleEndEvent(battleManager.wrapper(e.bc), map));
            }
        }
    }
    private static Player castPlayer(EntityPlayerMP pl) {
        return (Player) CraftEntity.getEntity(pl);
    }
}
