package me.fullidle.ficore.ficore.common.api.pokemon;

import me.fullidle.ficore.ficore.common.api.data.FIData;
import org.jetbrains.annotations.NotNull;

public enum Element {
    NORMAL,
    FIRE,
    WATER,
    GRASS,
    ELECTRIC,
    ICE,
    FIGHTING,
    POISON,
    GROUND,
    FLYING,
    PSYCHIC,
    BUG,
    ROCK,
    GHOST,
    DRAGON,
    DARK,
    STEEL,
    FAIRY;

    public static Element fromString(@NotNull String name) {
        if (name.equalsIgnoreCase("mystery")) {
            FIData.plugin.getLogger().warning("Mystery element is not supported yet. Returning null");
            return null;
        }
        try {
            return valueOf(name);
        } catch (IllegalStateException e) {
            for (Element value : values()) if (value.name().equalsIgnoreCase(name)) return value;
        }
        return null;
    }
}
