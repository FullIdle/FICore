package me.fullidle.ficore.ficore.v1_21;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IStatStore;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.AbilityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.Element;
import me.fullidle.ficore.ficore.common.api.pokemon.Gender;
import me.fullidle.ficore.ficore.common.api.pokemon.NatureWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.Stats;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.*;
import me.fullidle.ficore.ficore.common.bukkit.CraftWorld;
import me.fullidle.ficore.ficore.common.bukkit.inventory.CraftItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public IPokemonWrapper<Pokemon> create(String context) {
        val registryAccess = ((Level) CraftWorld.getHandle(Bukkit.getWorlds().getFirst())).registryAccess();
        try {
            return create(PokemonFactory.create(TagParser.parseTag(context), registryAccess));
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("上下文必须是NBT");
        }
    }

    @Getter
    public static class PokemonWrapper extends IPokemonWrapper<Pokemon> {

        public PokemonWrapper(Pokemon original) {
            super(original);
        }

        @Override
        public PokeEntityWrapper<?> getEntity() {
            val en = this.getOriginal().getPixelmonEntity();
            return en.map(p-> ((VPokeEntityWrapperFactory) FIData.V1_version.getPokeEntityWrapperFactory()).create(p)).orElse(null);
        }

        @Override
        public PokeEntityWrapper<?> spawnEntity(Location location) {
            val pixelmon = this.getOriginal().getOrSpawnPixelmon((Level) CraftWorld.getHandle(location.getWorld()), location.getX(),location.getY(),location.getZ());
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
        public @Nullable UUID getOwnerUUID() {
            val uuid = this.getOriginal().getOwnerPlayerUUID();
            return uuid == null ? this.getOriginal().getOriginalTrainerUUID() : uuid;
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
        public int getLevel() {
            return this.getOriginal().getPokemonLevel();
        }

        @Override
        public void setLevel(int level) {
            this.getOriginal().setLevel(level);
        }

        @Override
        public ItemStack createPhotoItem() {
            return CraftItemStack.asBukkitCopy(SpriteItemHelper.getPhoto(this.getOriginal()));
        }

        @Override
        public List<Element> getTypes() {
            return this.getOriginal().getTypes().stream().map(e->Element.fromString(e.value().name().getString())).collect(Collectors.toList());
        }

        @Override
        public AbilityWrapper<?> getAbility() {
            return new me.fullidle.ficore.ficore.v1_21.AbilityWrapper(this.getOriginal().getAbility());
        }

        @Override
        public NatureWrapper<?> getNature() {
            return new me.fullidle.ficore.ficore.v1_21.NatureWrapper(this.getOriginal().getNature());
        }

        @Override
        public Class<Pokemon> getType() {
            return Pokemon.class;
        }
    }

    public static StoragePos asPos(StoragePosition pos) {
        return new StoragePos(pos.box, pos.order);
    }

    public static Gender asGender(com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender gender) {
        return switch (gender) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
            case NONE -> Gender.GENDERLESS;
        };
    }

    public static com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender asGender(Gender gender) {
        return switch (gender) {
            case MALE -> com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender.MALE;
            case FEMALE -> com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender.FEMALE;
            case GENDERLESS -> com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender.NONE;
        };
    }

    public static Map<Stats, Integer> asMap(BattleStatsType[] types, IStatStore store) {
        val map = new HashMap<Stats, Integer>();
        for (BattleStatsType type : types) map.put(Stats.fromString(type.name()), store.getStat(type));
        return Collections.unmodifiableMap(map);
    }
}
