package me.fullidle.ficore.ficore.v1_12;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

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
