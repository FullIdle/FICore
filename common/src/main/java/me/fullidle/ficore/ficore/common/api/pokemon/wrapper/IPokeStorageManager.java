package me.fullidle.ficore.ficore.common.api.pokemon.wrapper;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IPokeStorageManager {
    IPokeStorageWrapper<?> getParty(UUID uuid);
    IPokeStorageWrapper<?> getPC(UUID uuid);

    default IPokeStorageWrapper<?> getParty(Player player) {
        return getParty(player.getUniqueId());
    }

    default IPokeStorageWrapper<?> getPC(Player player) {
        return getPC(player.getUniqueId());
    }
}
