package me.fullidle.ficore.ficore.v1_16;

import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventListener;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class ForgeEventListener implements IEventListener, Consumer<Event> {
    @Override
    public void invoke(Event event) {
        post(event);
    }

    @Override
    public void accept(Event event) {
        post(event);
    }

    public void post(Event event){
        ForgeEvent forgeEvent = new ForgeEvent(event);
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}
