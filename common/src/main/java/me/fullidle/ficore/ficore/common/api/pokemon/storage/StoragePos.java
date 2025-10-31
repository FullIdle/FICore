package me.fullidle.ficore.ficore.common.api.pokemon.storage;

import lombok.Getter;

@Getter
public class StoragePos {
    private final int box;
    private final int slot;

    public StoragePos(int box, int slot) {
        this.box = box;
        this.slot = slot;
    }
}
