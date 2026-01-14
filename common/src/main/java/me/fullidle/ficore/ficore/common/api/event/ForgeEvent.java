package me.fullidle.ficore.ficore.common.api.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 只支持到Forge(1.12.2/1.16.5) 之后的版本不支持(以后不一定兼容)
 */
@Getter
public class ForgeEvent extends org.bukkit.event.Event {
    private static final HandlerList handlers = new HandlerList();
    private final Object forgeEvent;

    public ForgeEvent(Object forgeEvent) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.forgeEvent = forgeEvent;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
