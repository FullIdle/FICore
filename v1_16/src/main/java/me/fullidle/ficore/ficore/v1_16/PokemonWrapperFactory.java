package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IStatStore;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.Gender;
import me.fullidle.ficore.ficore.common.api.pokemon.Stats;
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.*;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.fullidle.ficore.ficore.common.api.pokemon.Gender.*;

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
        public PokeEntityWrapper<?> getEntity() {
            val en = this.getOriginal().getPixelmonEntity();
            return en.map(entity -> ((VPokeEntityWrapperFactory) FIData.V1_version.getPokeEntityWrapperFactory()).create(entity)).orElse(null);
        }

        @Override
        public PokeEntityWrapper<?> spawnEntity(Location location) {
            val pixelmon = this.getOriginal().getOrSpawnPixelmon(null);
            val entity = CraftEntity.getEntity(pixelmon);
            entity.teleport(location);
            return ((VPokeEntityWrapperFactory) FIData.V1_version.getPokeEntityWrapperFactory()).create(pixelmon);
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
            val party = StorageProxy.getParty(player.getUniqueId());
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

        @Nullable
        @Override
        public OfflinePlayer getOwner() {
            val uuid = this.getOriginal().getOwnerPlayerUUID();
            return uuid == null ? null : Bukkit.getOfflinePlayer(uuid);
        }

        @Nullable
        @Override
        public UUID getOwnerUUID() {
            val uuid = this.getOriginal().getOwnerPlayerUUID();
            return uuid == null ? this.getOriginal().getOwnerTrainerUUID() : null;
        }

        @Override
        public IPokeStorageWrapper<?> getStorage() {
            throw new UnsupportedOperationException("Not supported yet.");
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
        public boolean isShiny() {
            return this.getOriginal().isShiny();
        }

        @Override
        public void setShiny(boolean shiny) {
            this.getOriginal().setShiny(shiny);
        }

        @Override
        public Map<Stats, Integer> getIVs() {
            return asMap(BattleStatsType.getEVIVStatValues(), this.getOriginal().getIVs());
        }

        @Override
        public Map<Stats, Integer> getEVs() {
            return asMap(BattleStatsType.getEVIVStatValues(), this.getOriginal().getEVs());
        }

        @Override
        public void setEV(Stats type, int value) {
            this.getOriginal().getEVs().setStat(BattleStatsType.getStatsEffect(type.name()), value);
        }

        @Override
        public void setIV(Stats type, int value) {
            this.getOriginal().getIVs().setStat(BattleStatsType.getStatsEffect(type.name()), value);
        }

        @Override
        public Gender getGender() {
            return asGender(this.getOriginal().getGender());
        }

        @Override
        public void setGender(Gender gender) {
            this.getOriginal().setGender(asGender(gender));
        }

        @Override
        public UUID getUUID() {
            return this.getOriginal().getUUID();
        }

        @Override
        public void setUUID(UUID uuid) {
            this.getOriginal().setUUID(uuid);
        }

        @Override
        public int getHealth() {
            return this.getOriginal().getHealth();
        }

        @Override
        public void setHealth(int health) {
            this.getOriginal().setHealth(health);
        }

        @Override
        public Class<Pokemon> getType() {
            return Pokemon.class;
        }
    }

    public static StoragePos asPos(StoragePosition pos) {
        return new StoragePos(pos.box, pos.order);
    }

    public static Map<Stats, Integer> asMap(BattleStatsType[] types, IStatStore store) {
        val map = new HashMap<Stats, Integer>();
        for (BattleStatsType type : types) map.put(Stats.fromString(type.name()), store.getStat(type));
        return Collections.unmodifiableMap(map);
    }

    public static Gender asGender(com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender gender) {
        switch (gender) {
            case FEMALE:
                return FEMALE;
            case MALE:
                return MALE;
            case NONE:
                return GENDERLESS;
        }
        return null;
    }

    public static com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender asGender(Gender gender) {
        switch (gender) {
            case FEMALE:
                return com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender.FEMALE;
            case MALE:
                return com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender.MALE;
            case GENDERLESS:
                return com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender.NONE;
        }
        return null;
    }
}
