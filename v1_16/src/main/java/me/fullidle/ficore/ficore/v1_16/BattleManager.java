package me.fullidle.ficore.ficore.v1_16;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleQuery;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.entities.npcs.NPCEntity;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.ActorManager;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.*;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BattleManager implements IBattleManager<BattleController> {
    public static BattleManager INSTANCE = new BattleManager();

    /**
     * @throws RuntimeException 玩家已经在对局时或参与的宝可梦正在进化时，会报出
     */
    @NotNull
    @Override
    public IPokeBattle<BattleController> create(Player p1, Player p2) {
        val pp1 = new PlayerParticipant(((ServerPlayerEntity) CraftEntity.getHandle(p1)), createPlayerTeam(p1.getUniqueId()), 1);
        val pp2 = new PlayerParticipant(((ServerPlayerEntity) CraftEntity.getHandle(p2)), createPlayerTeam(p2.getUniqueId()), 1);
        val bc = createBattle(new BattleParticipant[]{pp1}, new BattleParticipant[]{pp2});
        return new PokeBattle(bc);
    }

    public static List<Pokemon> createPlayerTeam(UUID uuid) {
        val list = Lists.newArrayList(StorageProxy.getParty(uuid).getAll());
        list.removeIf(Objects::isNull);
        return list;
    }

    @Override
    public IPokeBattle<BattleController> create(Actor<?>[] side1, Actor<?>[] side2) {
        val s1 = new BattleParticipant[side1.length];
        val s2 = new BattleParticipant[side2.length];
        for (int i = 0; i < side1.length; i++)
            s1[i] = side1[i] == null ? null : ((BattleParticipant) side1[i].getOriginal());
        for (int i = 0; i < side2.length; i++)
            s2[i] = side2[i] == null ? null : ((BattleParticipant) side2[i].getOriginal());
        val bc = createBattle(s1, s2);
        return new PokeBattle(bc);
    }

    @Nullable
    @Override
    public IPokeBattle<BattleController> getBattle(Player player) {
        return getBattle((ServerPlayerEntity) CraftEntity.getHandle(player));
    }

    @Override
    public @NotNull IPokeBattle<BattleController> getBattle(Actor<?> actor) {
        return wrapper(((BattleParticipant) actor.getOriginal()).bc);
    }

    @Override
    public @NotNull IPokeBattle<BattleController> wrapper(BattleController battle) {
        return new PokeBattle(battle);
    }

    public IPokeBattle<BattleController> getBattle(ServerPlayerEntity player) {
        val battle = BattleRegistry.getBattle(player);
        return battle == null ? null : new PokeBattle(battle);
    }

    @Override
    public void createQuery(Player p1, Player p2) {
        val player1 = (ServerPlayerEntity) CraftEntity.getHandle(p1);
        val player2 = (ServerPlayerEntity) CraftEntity.getHandle(p2);
        String errorT = "玩家没有可参与的宝可梦!";
        val pokemon1 = Objects.requireNonNull(StorageProxy.getParty(player1).getFirstAblePokemon(), errorT);
        val pokemon2 = Objects.requireNonNull(StorageProxy.getParty(player2).getFirstAblePokemon(), errorT);
        new BattleQuery(
                player1,
                pokemon1.getOrSpawnPixelmon(player1),
                player2,
                pokemon2.getOrSpawnPixelmon(player2)
        );
    }

    @Override
    public ActorManager<?> getActorManager() {
        return V16ActorManager.INSTANCE;
    }

    public static class V16ActorManager implements ActorManager<BattleParticipant> {
        public static final V16ActorManager INSTANCE = new V16ActorManager();

        @Override
        public @NotNull Actor<?> create(Entity entity) throws UnsupportedOperationException {
            val version = V1_version.getInstance();
            val manager = (ActorManager<BattleParticipant>) version.getBattleManager().getActorManager();
            if (entity instanceof Player)
                return manager.wrap(new PlayerParticipant((ServerPlayerEntity) CraftEntity.getHandle(entity), createPlayerTeam(entity.getUniqueId()), 1));
            val pokeFactory = (PokeEntityWrapperFactory<PixelmonEntity>) version.getPokeEntityWrapperFactory();
            if (pokeFactory.isPokeEntity(entity))
                return manager.wrap(new WildPixelmonParticipant(pokeFactory.asPokeEntity(entity).getOriginal()));
            val npcFactory = (PokeNPCEntityWrapperFactory<NPCEntity>) version.getPokeNPCEntityWrapperFactory();
            if (npcFactory.isPokeNPCEntity(entity)) {
                val npc = npcFactory.asPokeNPCEntity(entity);
                if (npc.getOriginal() instanceof NPCTrainer)
                    return manager.wrap(new TrainerParticipant(((NPCTrainer) npc.getOriginal()), 1));
            }
            throw new UnsupportedOperationException("不支持的实体类型 >>" + entity.getType().name());
        }

        @Override
        public @NotNull Actor<BattleParticipant> wrap(BattleParticipant battleParticipant) {
            return new V16Actor(battleParticipant);
        }

        @Override
        public @Nullable Actor<?> getActor(Entity entity) {
            if (entity instanceof Player) {
                val player = (ServerPlayerEntity) CraftEntity.getHandle(entity);
                return wrap(BattleRegistry.getBattle(player).getPlayer(player));
            }
            val version = V1_version.getInstance();
            val pokeFactory = (PokeEntityWrapperFactory<PixelmonEntity>) version.getPokeEntityWrapperFactory();
            val npcFactory = (PokeNPCEntityWrapperFactory<NPCEntity>) version.getPokeNPCEntityWrapperFactory();
            if (pokeFactory.isPokeEntity(entity)) {
                val bc = pokeFactory.asPokeEntity(entity).getOriginal().battleController;
                return bc == null ? null : wrap(bc.getParticipantForEntity(((LivingEntity) CraftEntity.getHandle(entity))));
            }
            if (npcFactory.isPokeNPCEntity(entity)) {
                val npc = npcFactory.asPokeNPCEntity(entity).getOriginal();
                if (npc instanceof NPCTrainer) {
                    val bc = ((NPCTrainer) npc).battleController;
                    return bc == null ? null : wrap(bc.getParticipantForEntity(((LivingEntity) CraftEntity.getHandle(entity))));
                }
            }
            return null;
        }
    }

    public static class V16Actor extends Actor<BattleParticipant> {
        public V16Actor(BattleParticipant original) {
            super(original);
        }

        @Override
        public Entity getEntity() {
            val entity = getOriginal().getEntity();
            return entity == null ? null : CraftEntity.getEntity(entity);
        }

        @Override
        public Collection<IPokemonWrapper<?>> getTeam() {
            val list = new ArrayList<IPokemonWrapper<?>>();
            val factory = (IPokemonWrapperFactory<Pokemon>) V1_version.getInstance().getPokemonWrapperFactory();
            for (PixelmonWrapper wrapper : getOriginal().allPokemon) list.add(factory.create(wrapper.pokemon));
            return list;
        }

        @Override
        public Collection<IPokemonWrapper<?>> getCurrents() {
            val list = new ArrayList<IPokemonWrapper<?>>();
            val factory = (IPokemonWrapperFactory<Pokemon>) V1_version.getInstance().getPokemonWrapperFactory();
            for (PixelmonWrapper wrapper : getOriginal().controlledPokemon) list.add(factory.create(wrapper.pokemon));
            return list;
        }

        @Override
        public Class<BattleParticipant> getType() {
            return BattleParticipant.class;
        }
    }

    @Getter
    public static class PokeBattle extends IPokeBattle<BattleController> {
        public PokeBattle(BattleController bc) {
            super(bc);
        }

        @Override
        public Collection<Player> getPlayers() {
            return this.getOriginal().getPlayers()
                    .stream()
                    .map(s -> ((Player) CraftEntity.getEntity(s.player)))
                    .collect(Collectors.toList());
        }

        @Override
        public Collection<Actor<?>> getPlayerActors() {
            val actorManager = (ActorManager<BattleParticipant>)FIData.V1_version.getBattleManager().getActorManager();
            return getOriginal().getPlayers().stream().map(actorManager::wrap).collect(Collectors.toList());
        }

        @Override
        public Collection<Actor<?>> getActors() {
            return Collections.emptyList();
        }

        @Override
        public void end() {
            this.getOriginal().endBattle();
        }

        @Override
        public void start() {
            startBattle(this.getOriginal());
        }

        @Override
        public Class<BattleController> getType() {
            return BattleController.class;
        }

    }

    /**
     * original
     *
     * @see BattleRegistry#startBattle(BattleParticipant[], BattleParticipant[], BattleRules)
     */
    @SneakyThrows
    @NotNull
    public static BattleController createBattle(BattleParticipant[] team1, BattleParticipant[] team2) {
        BattleController battle = new BattleController(team1, team2, new BattleRules(BattleType.SINGLE));
        val method = BattleRegistry.class.getDeclaredMethod("canParticipantsBattle", BattleController.class);
        method.setAccessible(true);
        if (!(boolean) method.invoke(null, battle)) {
            throw new RuntimeException("Players cannot battle");
        }
        return battle;
    }

    @SneakyThrows
    public static void startBattle(BattleController battle) {
        val pp = battle.participants.get(0);
        val method = BattleRegistry.class.getDeclaredMethod("performPreBattleChecksAndEvents", BattleController.class, BattleParticipant[].class, BattleParticipant[].class);
        method.setAccessible(true);
        val array1 = battle.getTeam(pp).toArray(new BattleParticipant[0]);
        val array2 = battle.getOpponents(pp).toArray(new BattleParticipant[0]);
        val invoke = (boolean) method.invoke(null, battle, array1, array2);
        if (!invoke) return;
        BattleRegistry.registerBattle(battle);
        Pixelmon.EVENT_BUS.post(new BattleStartedEvent.Post(battle, array1, array2));
    }
}
