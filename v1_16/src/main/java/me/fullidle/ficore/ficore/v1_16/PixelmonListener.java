package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.api.battles.BattleEndCause;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pokeballs.PokeBallEntity;
import lombok.val;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.BattleResult;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.ActorManager;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.*;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PixelmonListener {
    public static void on(ForgeEvent event) {
        if (event.getForgeEvent() instanceof BattleStartedEvent.Pre) {
            val e = (BattleStartedEvent.Pre) event.getForgeEvent();
            val bc = e.getBattleController();
            val manager = ((BattleManager) FIData.V1_version.getBattleManager());
            val actorManager = (ActorManager<BattleParticipant>) manager.getActorManager();
            val wrapper = manager.wrapper(bc);
            val pluginManager = Bukkit.getPluginManager();
            val teamOne = e.getTeamOne();
            val teamTwo = e.getTeamTwo();
            val side1 = new Actor<?>[teamOne.length];
            val side2 = new Actor<?>[teamTwo.length];
            for (int i = 0; i < teamOne.length; i++) side1[i] = actorManager.wrap(teamOne[i]);
            for (int i = 0; i < teamTwo.length; i++) side2[i] = actorManager.wrap(teamTwo[i]);

            val startEvent = new PokeBattleStartEvent(wrapper, side1, side2);
            pluginManager.callEvent(startEvent);
            if (startEvent.isCancelled()) {
                e.setCanceled(true);
                return;
            }

            if (bc.isPvP() && teamOne.length == 1 && e.getTeamTwo().length == 1) {
                val ev = new PVPBattleStartEvent(
                        wrapper,
                        castPlayer(((PlayerParticipant) teamOne[0]).player),
                        castPlayer(((PlayerParticipant) e.getTeamTwo()[0]).player)
                );
                pluginManager.callEvent(ev);
                if (ev.isCancelled()) {
                    e.setCanceled(true);
                    return;
                }
            }
        }
        if (event.getForgeEvent() instanceof BattleEndEvent) {
            val e = (BattleEndEvent) event.getForgeEvent();
            val pluginManager = Bukkit.getPluginManager();
            val battleManager = (BattleManager)FIData.V1_version.getBattleManager();
            val actorManager = (ActorManager<BattleParticipant>) battleManager.getActorManager();
            val wrapper = battleManager.wrapper(e.getBattleController());

            val winners = new ArrayList<Actor<?>>();
            val losers = new ArrayList<Actor<?>>();

            e.getResults().forEach((participant,results)-> {
                if (results.equals(BattleResults.VICTORY)) {
                    winners.add(actorManager.wrap(participant));
                    return;
                }
                losers.add(actorManager.wrap(participant));
            });

            val startEvent = new PokeBattleEndEvent(wrapper, winners, losers);
            pluginManager.callEvent(startEvent);

            if (e.getBattleController().isPvP() && e.getBattleController().playerNumber == 2 && !e.isAbnormal() && !e.getCause().equals(BattleEndCause.FORCE)) {
                val map = new HashMap<Player, BattleResult>();
                for (Map.Entry<BattleParticipant, BattleResults> entry : e.getResults().entrySet())
                    map.put(castPlayer(((PlayerParticipant) entry.getKey()).player), BattleResult.valueOf(entry.getValue().name()));
                pluginManager.callEvent(new PVPBattleEndEvent(wrapper, map));
            }
        }
        if (event.getForgeEvent() instanceof CaptureEvent) {
            val e = ((CaptureEvent) event.getForgeEvent());
            val version = V1_version.getInstance();
            val pokeBallEntity = ((PokeBallEntityManager<PokeBallEntity>) version.getPokeBallEntityManager()).wrap(e.getPokeBall());
            val pluginManager = Bukkit.getPluginManager();

            if (e instanceof CaptureEvent.StartCapture) {
                val captureEvent = new PokePreCaptureEvent(((PokeEntityWrapperFactory<PixelmonEntity>) version.getPokeEntityWrapperFactory()).create(e.getPokemon()), pokeBallEntity, () -> !e.isCanceled(), r -> e.setCanceled(!r));
                pluginManager.callEvent(captureEvent);
                return;
            }

            if (e instanceof CaptureEvent.SuccessfulCapture) {
                pluginManager.callEvent(new PokeCapturedEvent(((IPokemonWrapperFactory<Pokemon>) version.getPokemonWrapperFactory()).create(e.getPokemon().getPokemon()), pokeBallEntity));
                return;
            }
        }
    }
    private static Player castPlayer(ServerPlayerEntity spe) {
        return ((Player) CraftEntity.getEntity(spe));
    }
}
