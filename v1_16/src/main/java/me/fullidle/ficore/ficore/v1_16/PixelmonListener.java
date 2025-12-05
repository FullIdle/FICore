package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.api.battles.BattleEndCause;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.BattleResult;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleEndEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleStartEvent;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PixelmonListener {
    public static void on(ForgeEvent event) {
        if (event.getForgeEvent() instanceof BattleStartedEvent.Pre) {
            val e = (BattleStartedEvent.Pre) event.getForgeEvent();
            val bc = e.getBattleController();
            if (bc.isPvP() && e.getTeamOne().length == 1 && e.getTeamTwo().length == 1) {
                val manager = ((BattleManager) FIData.V1_version.getBattleManager());
                val ev = new PVPBattleStartEvent(
                        manager.wrapper(bc),
                        castPlayer(((PlayerParticipant) e.getTeamOne()[0]).player),
                        castPlayer(((PlayerParticipant) e.getTeamTwo()[0]).player)
                );
                Bukkit.getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    e.setCanceled(true);
                    return;
                }
            }
        }
        if (event.getForgeEvent() instanceof BattleEndEvent) {
            val e = (BattleEndEvent) event.getForgeEvent();
            if (e.getBattleController().isPvP() && e.getBattleController().playerNumber == 2 && !e.isAbnormal() && !e.getCause().equals(BattleEndCause.FORCE)) {
                val battleManager = (BattleManager)FIData.V1_version.getBattleManager();
                val map = new HashMap<Player, BattleResult>();
                for (Map.Entry<BattleParticipant, BattleResults> entry : e.getResults().entrySet())
                    map.put(castPlayer(((PlayerParticipant) entry.getKey()).player), BattleResult.valueOf(entry.getValue().name()));
                Bukkit.getPluginManager().callEvent(new PVPBattleEndEvent(battleManager.wrapper(e.getBattleController()), map));
            }
        }
    }
    private static Player castPlayer(ServerPlayerEntity spe) {
        return ((Player) CraftEntity.getEntity(spe));
    }
}
