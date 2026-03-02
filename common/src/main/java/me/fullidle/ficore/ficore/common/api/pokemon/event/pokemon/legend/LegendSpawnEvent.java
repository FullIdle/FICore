package me.fullidle.ficore.ficore.common.api.pokemon.event.pokemon.legend;

import lombok.Getter;
import lombok.Setter;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 生成器生成神兽的时候触发 重铸约等于是转发时间，方暂时没有
 * 普通生成和自然生成还有手动生成都不会触发该事件
 */
@Getter
@Setter
public class LegendSpawnEvent extends Event implements Cancellable {
    @Getter
    public static final HandlerList handlerList = new HandlerList();

    private boolean cancelled = false;

    private final ISpeciesWrapper<?> species;

    public LegendSpawnEvent(ISpeciesWrapper<?> species) {
        this.species = species;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
