package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleStartEvent;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

public class ForgeEventListener implements IEventListener, Consumer<Event> {

    public ForgeEventListener() {
        HandlerList.unregisterAll(ForgeEventHandlers.INSTANCE);
        Bukkit.getPluginManager().registerEvents(ForgeEventHandlers.INSTANCE, FIData.plugin);
    }

    @Override
    public void invoke(Event event) {
        post(event);
    }

    @Override
    public void accept(Event event) {
        post(event);
    }

    public void post(Event event) {
        ForgeEvent forgeEvent = new ForgeEvent(event);
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }

    public static class ForgeEventHandlers implements Listener {
        public static ForgeEventHandlers INSTANCE = new ForgeEventHandlers();

        @EventHandler
        public void on(ForgeEvent event) {
            if (event.getForgeEvent() instanceof BattleStartedEvent.Pre) {
                val e = (BattleStartedEvent.Pre) event.getForgeEvent();
                val bc = e.getBattleController();
                if (bc.isPvP() && e.getTeamOne().length == 1 && e.getTeamTwo().length == 1) {
                    val manager = ((V1_16) FIData.V1_version).getBattleManager();
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
        }

        private static Player castPlayer(ServerPlayerEntity spe) {
            return ((Player) CraftEntity.getEntity(spe));
        }
    }
}
