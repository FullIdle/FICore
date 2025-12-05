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
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleEndEvent;
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleStartEvent;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class ForgeEventListener implements IEventListener {
    public ForgeEventListener() {
        HandlerList.unregisterAll(ForgeEventInternalHandles.INSTANCE);
        Bukkit.getPluginManager().registerEvents(ForgeEventInternalHandles.INSTANCE, FIData.plugin);
    }

    @Override
    public void invoke(Event event) {
        try {
            ForgeEvent forgeEvent = new ForgeEvent(event);
            Bukkit.getPluginManager().callEvent(forgeEvent);
        } catch (Exception ignored) {
        }
    }

    public static class ForgeEventInternalHandles implements Listener {
        public static ForgeEventInternalHandles INSTANCE = new ForgeEventInternalHandles();

        @EventHandler
        public void on(ForgeEvent event) {
            if (FIData.V1_version.hasPokemon()) {
                PixelmonListener.on(event);
            }
        }
    }
}
