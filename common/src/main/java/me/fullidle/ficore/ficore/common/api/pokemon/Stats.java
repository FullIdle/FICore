package me.fullidle.ficore.ficore.common.api.pokemon;

import lombok.Getter;

public enum Stats {
    HP("hp"),
    ATTACK("atk"),
    DEFENCE("def"),
    SPECIAL_ATTACK("spa"),
    SPECIAL_DEFENCE("spd"),
    SPEED("spe");

    @Getter
    private final String id;

    Stats(String id) {
        this.id = id;
    }

    public static Stats fromString(String s) {
        try {
            return valueOf(s.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }
        for (Stats value : values()) if (value.id.equals(s)) return value;
        return null;
    }
}
