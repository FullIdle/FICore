package me.fullidle.ficore.ficore.v1_16;

import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventListener;
import org.bukkit.Bukkit;

public class ForgeEventListener implements IEventListener {
    @Override
    public void invoke(Event event) {
        ForgeEvent forgeEvent = new ForgeEvent(event);
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}
