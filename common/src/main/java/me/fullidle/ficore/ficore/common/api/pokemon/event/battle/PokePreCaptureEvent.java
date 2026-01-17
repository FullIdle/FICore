package me.fullidle.ficore.ficore.common.api.pokemon.event.battle;

import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapper;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * 宝可梦预捕获事件
 */
public class PokePreCaptureEvent extends Event {
    @Getter
    private static final HandlerList handlerList = new HandlerList();

    /**
     * 被捕获的宝可梦
     */
    @Getter
    private final PokeEntityWrapper<?> pokeEntity;
    /**
     * 相关宝可梦球实体
     */
    @Getter
    private final PokeBallEntityWrapper<?> pokeBallEntity;
    /**
     * 结果提供器
     * {@link Supplier#get()} 的结果如果为空则返回值为 {@code false}
     */
    private final Supplier<Boolean> resultSupplier;
    /**
     * 修改结果的消费器
     */
    private final Consumer<Boolean> setResultConsumer;

    public PokePreCaptureEvent(@NotNull PokeEntityWrapper<?> pokeEntity, @NotNull PokeBallEntityWrapper<?> pokeBallEntity, Supplier<Boolean> resultSupplier, Consumer<Boolean> setResultConsumer) {
        this.pokeEntity = pokeEntity;
        this.pokeBallEntity = pokeBallEntity;
        this.resultSupplier = resultSupplier;
        this.setResultConsumer = setResultConsumer;
    }

    public boolean isSuccessful() {
        val b = resultSupplier.get();
        return b != null && b;
    }

    public void setResult(boolean result) {
        setResultConsumer.accept(result);
    }

    public void setSuccess() {
        setResult(true);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
