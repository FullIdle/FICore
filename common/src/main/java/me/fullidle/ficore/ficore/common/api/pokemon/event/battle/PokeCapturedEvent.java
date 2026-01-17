package me.fullidle.ficore.ficore.common.api.pokemon.event.battle;

import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapper;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;


/**
 * 宝可梦捕获事件
 * 在宝可梦被成功捕获时触发
 */
@Getter
public class PokeCapturedEvent extends Event {
    @Getter
    private static final HandlerList handlerList = new HandlerList();

    /**
     * 被捕获的宝可梦
     */
    private final IPokemonWrapper<?> pokemon;
    /**
     * 相关宝可梦球事件
     */
    private final PokeBallEntityWrapper<?> pokeBallEntity;

    public PokeCapturedEvent(@NotNull IPokemonWrapper<?> pokemon, @NotNull PokeBallEntityWrapper<?> pokeBallEntity) {
        this.pokemon = pokemon;
        this.pokeBallEntity = pokeBallEntity;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
