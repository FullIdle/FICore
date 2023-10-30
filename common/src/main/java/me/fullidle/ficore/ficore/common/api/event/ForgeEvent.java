package me.fullidle.ficore.ficore.common.api.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

@Getter
public class ForgeEvent extends org.bukkit.event.Event {
    private static final HandlerList handlers = new HandlerList();
    private final Object forgeEvent;

    public ForgeEvent(Object forgeEvent) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.forgeEvent = forgeEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
