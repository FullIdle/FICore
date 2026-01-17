package me.fullidle.ficore.ficore.v1_21;

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
import me.fullidle.ficore.ficore.common.api.pokemon.battle.BattleResult;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleEndEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleStartEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PokeCapturedEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PokePreCaptureEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PixelmonListener {
    public static void onBattleStarted(BattleStartedEvent.Pre e) {
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
            }
        }
    }

    public static void onCapture(CaptureEvent e) {
        val version = V1_version.getInstance();
        val pokemon = ((IPokemonWrapperFactory<Pokemon>) version.getPokemonWrapperFactory()).create(e.getPokemon());
        val pokeBallEntity = ((PokeBallEntityManager<PokeBallEntity>) version.getPokeBallEntityManager()).wrap(e.getPokeBallEntity());
        val pluginManager = Bukkit.getPluginManager();

        if (e instanceof CaptureEvent.StartCapture event) {
            val captureEvent = new PokePreCaptureEvent(pokemon.getEntity(), pokeBallEntity, () -> !event.isCanceled(), r -> event.setCanceled(!r));
            pluginManager.callEvent(captureEvent);
            return;
        }

        if (e instanceof CaptureEvent.SuccessfulCapture)
            pluginManager.callEvent(new PokeCapturedEvent(pokemon, pokeBallEntity));
    }

    public static void onBattleEnd(BattleEndEvent e) {
        if (e.getBattleController().isPvP() && e.getBattleController().playerNumber == 2 && !e.isAbnormal() && !e.getCause().equals(BattleEndCause.FORCE)) {
            val battleManager = ((BattleManager) FIData.V1_version.getBattleManager());
            val map = new HashMap<Player, BattleResult>();
            for (Map.Entry<BattleParticipant, BattleResults> entry : e.getResults().entrySet())
                map.put(castPlayer(((PlayerParticipant) entry.getKey()).player), BattleResult.valueOf(entry.getValue().name()));
            Bukkit.getPluginManager().callEvent(new PVPBattleEndEvent(battleManager.wrapper(e.getBattleController()), map));
        }
    }

    public static Player castPlayer(ServerPlayer player) {
        return ((Player) CraftEntity.getEntity(player));
    }

    private static final List<Consumer<?>> listeners = new ArrayList<>();

    private static <T extends Event>Consumer<T> add(Consumer<T> t) {
        listeners.add(t);
        return t;
    }

    public static void register() {
        val pixelmonEventBus = com.pixelmonmod.pixelmon.Pixelmon.EVENT_BUS;
        pixelmonEventBus.addListener(com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent.Pre.class, add(PixelmonListener::onBattleStarted));
        pixelmonEventBus.addListener(com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent.class, add(PixelmonListener::onBattleEnd));
        pixelmonEventBus.addListener(CaptureEvent.class, add(PixelmonListener::onCapture));
    }

    public static void unregister() {
        val pixelmonEventBus = com.pixelmonmod.pixelmon.Pixelmon.EVENT_BUS;
        listeners.forEach(pixelmonEventBus::unregister);
        listeners.clear();
    }
}
