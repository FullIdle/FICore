package me.fullidle.ficore.ficore.v1_12;

import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import org.bukkit.Bukkit;

public class ForgeEventListener implements IEventListener {
    @Override
    public void invoke(Event event) {
        try {
            ForgeEvent forgeEvent = new ForgeEvent(event);
            Bukkit.getPluginManager().callEvent(forgeEvent);
        } catch (Exception ignored) {
        }
    }
}
