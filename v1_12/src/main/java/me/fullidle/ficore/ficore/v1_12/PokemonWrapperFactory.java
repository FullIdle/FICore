package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IStatStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.Gender;
import me.fullidle.ficore.ficore.common.api.pokemon.Stats;
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.*;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import me.fullidle.ficore.ficore.common.bukkit.inventory.CraftItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PokemonWrapperFactory implements IPokemonWrapperFactory<Pokemon> {
    public static final PokemonWrapperFactory INSTANCE = new PokemonWrapperFactory();

    @Override
    public IPokemonWrapper<Pokemon> create(Pokemon original) {
        return new PokemonWrapper(original);
    }

    @Override
    public IPokemonWrapper<Pokemon> create(ISpeciesWrapper<?> speciesWrapper) {
        val pokemon = Pixelmon.pokemonFactory.create(((SpeciesWrapperFactory.SpeciesWrapper) speciesWrapper).getOriginal());
        return create(pokemon);
    }

    @Getter
    public static class PokemonWrapper extends IPokemonWrapper<Pokemon> {
        public PokemonWrapper(Pokemon original) {
            super(original);
        }

        @Override
        public PokeEntityWrapper<?> getEntity() {
            val exists = this.getOriginal().getPixelmonIfExists();
            return exists == null ? null : ((VPokeEntityWrapperFactory) FIData.V1_version.getPokeEntityWrapperFactory()).create(exists);
        }

        @Override
        public PokeEntityWrapper<?> spawnEntity(Location location) {
            val entity = this.getOriginal().getOrSpawnPixelmon(null);
            CraftEntity.getEntity(entity).teleport(location);
            return ((VPokeEntityWrapperFactory) FIData.V1_version.getPokeEntityWrapperFactory()).create(entity);
        }

        @Override
        public ISpeciesWrapper<?> getSpecies() {
            return ((SpeciesWrapperFactory) FIData.V1_version.getSpeciesWrapperFactory()).create(this.getOriginal().getSpecies());
        }

        @Override
        public String getName() {
            return getOriginal().getSpecies().getPokemonName();
        }

        @Override
        public String getTranslatedName() {
            return getOriginal().getLocalizedName();
        }

        @Override
        public void givePlayer(Player player) {
            Pixelmon.storageManager.getParty(player.getUniqueId()).add(this.getOriginal());
        }

        @Override
        public Map<Stats, Integer> getStats() {
            val map = new HashMap<Stats, Integer>();
            for (StatsType type : StatsType.getStatValues()) {
                val stat = this.getOriginal().getBaseStats().getStat(type);
                map.put(Stats.fromString(type.name()), stat);
            }
            return Collections.unmodifiableMap(map);
        }

        @Override
        public boolean isLegend() {
            return this.getOriginal().isLegendary();
        }

        @Override
        public boolean isMythical() {
            return this.getOriginal().isLegendary();
        }

        @Override
        public boolean isUltra() {
            return this.getOriginal().getSpecies().isUltraBeast();
        }

        @Nullable
        @Override
        public OfflinePlayer getOwner() {
            val ownerPlayerUUID = this.getOriginal().getOwnerPlayerUUID();
            return ownerPlayerUUID == null ? null : Bukkit.getOfflinePlayer(ownerPlayerUUID);
        }

        @Nullable
        @Override
        public UUID getOwnerUUID() {
            val uuid = this.getOriginal().getOwnerPlayerUUID();
            return uuid == null ? this.getOriginal().getOwnerTrainerUUID() : uuid;
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
            return Collections.unmodifiableMap(asMap(StatsType.getStatValues(), this.getOriginal().getIVs()));
        }

        @Override
        public Map<Stats, Integer> getEVs() {
            return Collections.unmodifiableMap(asMap(StatsType.getStatValues(), this.getOriginal().getIVs()));
        }

        @Override
        public void setEV(Stats type, int value) {
            this.getOriginal().getEVs().setStat(Objects.requireNonNull(StatsType.getStatsEffect(type.name().replace("_", ""))), value);
        }

        @Override
        public void setIV(Stats type, int value) {
            this.getOriginal().getIVs().setStat(Objects.requireNonNull(StatsType.getStatsEffect(type.name().replace("_", ""))), value);
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
        public boolean inRanch() {
            return this.getOriginal().isInRanch();
        }

        @Override
        public int getLevel() {
            return this.getOriginal().getLevel();
        }

        @Override
        public void setLevel(int level) {
            this.getOriginal().setLevel(level);
        }

        @Override
        public ItemStack createPhotoItem() {
            return CraftItemStack.asBukkitCopy(ItemPixelmonSprite.getPhoto(this.getOriginal()));
        }

        @Override
        public Class<Pokemon> getType() {
            return Pokemon.class;
        }
    }

    public static StoragePos asPos(StoragePosition pos) {
        return new StoragePos(pos.box, pos.order);
    }

    public static Map<Stats, Integer> asMap(StatsType[] types, IStatStore store) {
        val map = new HashMap<Stats, Integer>();
        for (StatsType type : types) map.put(Stats.fromString(type.name()), store.getStat(type));
        return map;
    }

    public static Gender asGender(com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender gender) {
        switch (gender) {
            case Male:
                return Gender.MALE;
            case Female:
                return Gender.FEMALE;
            case None:
                return Gender.GENDERLESS;
        }
        return null;
    }

    public static com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender asGender(Gender gender) {
        switch (gender) {
            case MALE:
                return com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender.Male;
            case FEMALE:
                return com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender.Female;
            case GENDERLESS:
                return com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender.None;
        }
        return null;
    }
}
