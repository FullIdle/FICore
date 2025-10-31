package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.Stats;
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PokemonWrapperFactory implements IPokemonWrapperFactory<Pokemon> {
    public static final PokemonWrapperFactory INSTANCE = new PokemonWrapperFactory();

    @Override
    public IPokemonWrapper<Pokemon> create(Pokemon original) {
        return new PokemonWrapper(original);
    }

    @Override
    public IPokemonWrapper<Pokemon> create(ISpeciesWrapper<?> speciesWrapper) {
        val pokemon = PokemonFactory.create(((SpeciesWrapperFactory.SpeciesWrapper) speciesWrapper).getOriginal());
        return create(pokemon);
    }

    @Getter
    public static class PokemonWrapper extends IPokemonWrapper<Pokemon> {

        public PokemonWrapper(Pokemon original) {
            super(original);
        }

        @Override
        public Entity getEntity() {
            return CraftEntity.getEntity(this.getOriginal().getPixelmonEntity().orElse(null));
        }

        @Override
        public Entity spawnEntity(Location location) {
            val entity = CraftEntity.getEntity(this.getOriginal().getOrSpawnPixelmon(null));
            entity.teleport(location);
            return entity;
        }

        @Override
        public ISpeciesWrapper<?> getSpecies() {
            return ((SpeciesWrapperFactory) FIData.V1_version.getSpeciesWrapperFactory()).create(this.getOriginal().getSpecies());
        }

        @Override
        public String getName() {
            return getOriginal().getSpecies().getName();
        }

        @Override
        public String getTranslatedName() {
            return getOriginal().getLocalizedName();
        }

        @Override
        public void givePlayer(Player player) {
            val party = StorageProxy.getPartyNow(player.getUniqueId());
            party.add(this.getOriginal());
        }

        @Override
        public Map<Stats, Integer> getStats() {
            val map = new HashMap<Stats, Integer>();
            for (BattleStatsType type : BattleStatsType.getEVIVStatValues()) {
                val stat = this.getOriginal().getStats().get(type);
                map.put(Stats.fromString(type.name()), stat);
            }
            return map;
        }

        @Override
        public boolean isLegend() {
            return this.getOriginal().isLegendary(true);
        }

        @Override
        public boolean isMythical() {
            return this.getOriginal().isMythical();
        }

        @Override
        public boolean isUltra() {
            return this.getOriginal().isUltraBeast();
        }

        @Override
        public @Nullable OfflinePlayer getOwner() {
            val uuid = this.getOriginal().getOwnerPlayerUUID();
            return uuid == null ? null : Bukkit.getOfflinePlayer(uuid);
        }

        @Override
        public @Nullable UUID getUniqueId() {
            val uuid = this.getOriginal().getOwnerPlayerUUID();
            return uuid == null ? this.getOriginal().getOriginalTrainerUUID() : null;
        }

        @Override
        public IPokeStorageWrapper<?> getStorage() {
            throw new UnsupportedOperationException();
        }

        @Override
        public StoragePos getStoragePos() {
            val pos = this.getOriginal().getPosition();
            return pos == null ? null : asPos(pos);
        }

        @Override
        public boolean isEgg() {
            return this.getOriginal().isEgg();
        }

        @Override
        public Class<Pokemon> getType() {
            return Pokemon.class;
        }
    }

    public static StoragePos asPos(StoragePosition pos) {
        return new StoragePos(pos.box, pos.order);
    }
}
