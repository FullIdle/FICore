package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PokeStorageManager implements IPokeStorageManager {
    public static PokeStorageManager INSTANCE = new PokeStorageManager();

    @Override
    public IPokeStorageWrapper<?> getParty(UUID uuid) {
        return new PokeStorageWrapper(Pixelmon.storageManager.getParty(uuid));
    }

    @Override
    public IPokeStorageWrapper<?> getPC(UUID uuid) {
        return new PokeStorageWrapper(Pixelmon.storageManager.getPCForPlayer(uuid));
    }

    public static class PokeStorageWrapper extends IPokeStorageWrapper<PokemonStorage> {

        public PokeStorageWrapper(PokemonStorage original) {
            super(original);
        }

        @Override
        public IPokemonWrapper<?> get(StoragePos pos) {
            val pokemon = this.getOriginal().get(asPos(pos));
            return pokemon == null ? null : ((PokemonWrapperFactory) FIData.V1_version.getPokemonWrapperFactory()).create(pokemon);
        }

        @Override
        public void set(StoragePos pos, IPokemonWrapper<?> poke) {
            this.getOriginal().set(asPos(pos), poke == null ? null : ((Pokemon) poke.getOriginal()));
        }

        @Override
        public void add(IPokemonWrapper<?> poke) {
            this.getOriginal().add(((Pokemon) poke.getOriginal()));
        }

        @Override
        public List<IPokemonWrapper<?>> all() {
            val list = new ArrayList<IPokemonWrapper<?>>();
            val factory = (PokemonWrapperFactory) FIData.V1_version.getPokemonWrapperFactory();
            for (Pokemon pokemon : this.getOriginal().getAll()) {
                if (pokemon == null) continue;
                list.add(factory.create(pokemon));
            }
            return list;
        }

        @Override
        public UUID getUUID() {
            return this.getOriginal().uuid;
        }

        @Override
        public StoragePos findEmptyPos() {
            val pos = this.getOriginal().getFirstEmptyPosition();
            if (pos == null) return null;
            return new StoragePos(pos.box, pos.order);
        }

        @Override
        public Class<PokemonStorage> getType() {
            return PokemonStorage.class;
        }
    }

    public static StoragePosition asPos(StoragePos pos) {
        return new StoragePosition(pos.getBox(), pos.getSlot());
    }
}
