package me.fullidle.ficore.ficore.v1_21;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAI;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.battles.query.BattleQuery;
import com.pixelmonmod.pixelmon.entities.npcs.NPC;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.ActorManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @see BattleController
 * @see BattleRegistry
 */
public class BattleManager implements IBattleManager<BattleController> {
    public static BattleManager INSTANCE = new BattleManager();

    /**
     * @throws RuntimeException 玩家已经在对局时或参与的宝可梦正在进化时，会报出
     */
    @NotNull
    @Override
    public IPokeBattle<BattleController> create(Player p1, Player p2) {
        val pp1 = new PlayerParticipant(((ServerPlayer) CraftEntity.getHandle(p1)), createPlayerTeam(p1.getUniqueId()));
        val pp2 = new PlayerParticipant(((ServerPlayer) CraftEntity.getHandle(p2)), createPlayerTeam(p2.getUniqueId()));
        val bc = createBattle(new BattleParticipant[]{pp1}, new BattleParticipant[]{pp2});
        return new PokeBattle(bc);
    }

    public static List<Pokemon> createPlayerTeam(UUID uuid) {
        val list = Lists.newArrayList(StorageProxy.getPartyNow(uuid).getAll());
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
        return getBattle((ServerPlayer) CraftEntity.getHandle(player));
    }

    @Override
    public @NotNull IPokeBattle<BattleController> getBattle(Actor<?> actor) {
        return wrapper(((BattleParticipant) actor.getOriginal()).bc);
    }

    @Override
    public @NotNull IPokeBattle<BattleController> wrapper(BattleController battle) {
        return new PokeBattle(battle);
    }

    public IPokeBattle<BattleController> getBattle(ServerPlayer player) {
        val battle = BattleRegistry.getBattle(player);
        return battle == null ? null : new PokeBattle(battle);
    }

    @Override
    public void createQuery(Player p1, Player p2) {
        val player1 = (ServerPlayer) CraftEntity.getHandle(p1);
        val player2 = (ServerPlayer) CraftEntity.getHandle(p2);
        String errorT = "玩家没有可参与的宝可梦!";
        val pokemon1 = Objects.requireNonNull(StorageProxy.getPartyNow(player1).getFirstAblePokemon(), errorT);
        val pokemon2 = Objects.requireNonNull(StorageProxy.getPartyNow(player2).getFirstAblePokemon(), errorT);

        BattleQuery.builder()
                .opponent(player1)
                .challenger(player2)
                .sendQuery();
    }

    @Override
    public ActorManager<?> getActorManager() {
        return V21ActorManager.INSTANCE;
    }

    public static class V21ActorManager implements ActorManager<BattleParticipant> {
        public static final V21ActorManager INSTANCE = new V21ActorManager();

        @Override
        public @NotNull Actor<?> create(Entity entity) throws UnsupportedOperationException {
            if (entity instanceof Player)
                return wrap(new PlayerParticipant(((ServerPlayer) CraftEntity.getHandle(entity)), createPlayerTeam(entity.getUniqueId())));
            val version = V1_version.getInstance();
            val pokeEntityFactory = (PokeEntityWrapperFactory<PixelmonEntity>) version.getPokeEntityWrapperFactory();

            if (pokeEntityFactory.isPokeEntity(entity))
                return wrap(new WildPixelmonParticipant(pokeEntityFactory.asPokeEntity(entity).getOriginal()));

            val npcEntityFactory = (PokeNPCEntityWrapperFactory<NPC>) version.getPokeEntityWrapperFactory();
            if (npcEntityFactory.isPokeNPCEntity(entity)) {
                val npc = npcEntityFactory.asPokeNPCEntity(entity).getOriginal();
                val party = npc.getParty();
                if (party != null)
                    return wrap(EntityParticipant.builder().entity(npc).storage(party).aiMode(BattleAI.RANDOM).canMega(true).canDynamax(true).build());
            }
            throw new UnsupportedOperationException("不支持实体的类型 >> " + entity.getType().name());
        }

        @Override
        public @NotNull Actor<BattleParticipant> wrap(BattleParticipant battleParticipant) {
            return new V21Actor(battleParticipant);
        }

        @Override
        public @Nullable Actor<?> getActor(Entity entity) {
            if (entity instanceof Player) {
                val handle = (ServerPlayer) CraftEntity.getHandle(entity);
                return wrap(BattleRegistry.getBattle(handle).getPlayer(handle));
            }
            val version = V1_version.getInstance();
            val pokeEntityFactory = (PokeEntityWrapperFactory<PixelmonEntity>) version.getPokeEntityWrapperFactory();

            if (pokeEntityFactory.isPokeEntity(entity)) {
                val original = pokeEntityFactory.asPokeEntity(entity).getOriginal();
                val bc = original.battleController;
                return bc == null ? null : wrap(bc.getParticipantForEntity(original));
            }
            val npcEntityFactory = (PokeNPCEntityWrapperFactory<NPC>) version.getPokeEntityWrapperFactory();
            if (npcEntityFactory.isPokeNPCEntity(entity)) {
                val npc = npcEntityFactory.asPokeNPCEntity(entity).getOriginal();
                try {
                    val field = BattleRegistry.class.getDeclaredField("BATTLES_BY_ID");
                    field.setAccessible(true);
                    val map = (Map<Integer, BattleController>) field.get(null);
                    for (BattleController value : map.values()) {
                        val p = value.getParticipantForEntity(npc);
                        if (p != null) return wrap(p);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
            return null;
        }
    }

    public static class V21Actor extends Actor<BattleParticipant> {
        public V21Actor(BattleParticipant original) {
            super(original);
        }

        @Override
        public Entity getEntity() {
            val entity = getOriginal().getEntity();
            return entity == null ? null : CraftEntity.getEntity(entity);
        }

        @Override
        public Collection<IPokemonWrapper<?>> getTeam() {
            val factory = (IPokemonWrapperFactory<Pokemon>) V1_version.getInstance().getPokemonWrapperFactory();
            val list = new ArrayList<IPokemonWrapper<?>>();
            for (PixelmonWrapper wrapper : getOriginal().allPokemon)
                list.add(factory.create(wrapper.getOriginalPokemon()));
            return list;
        }

        @Override
        public Collection<IPokemonWrapper<?>> getCurrents() {
            val factory = (IPokemonWrapperFactory<Pokemon>) V1_version.getInstance().getPokemonWrapperFactory();
            val list = new ArrayList<IPokemonWrapper<?>>();
            for (PixelmonWrapper wrapper : getOriginal().controlledPokemon)
                list.add(factory.create(wrapper.getOriginalPokemon()));
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
            val actorManager = (ActorManager<BattleParticipant>) V1_version.getInstance().getBattleManager().getActorManager();
            return getOriginal().getPlayers().stream().map(actorManager::wrap).collect(Collectors.toUnmodifiableList());
        }

        @Override
        public Collection<Actor<?>> getActors() {
            val actorManager = (ActorManager<BattleParticipant>) V1_version.getInstance().getBattleManager().getActorManager();
            return getOriginal().getPlayers().stream().map(actorManager::wrap).collect(Collectors.toUnmodifiableList());
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
     * @see BattleRegistry#performPreBattleChecksAndEvents(BattleController, BattleParticipant[], BattleParticipant[])
     */
    @SneakyThrows
    @NotNull
    public static BattleController createBattle(BattleParticipant[] team1, BattleParticipant[] team2) {
        BattleController battle = new BattleController(team1, team2, team1[0].getEntity().registryAccess());
        val method = BattleRegistry.class.getDeclaredMethod("canParticipantsBattle", BattleController.class);
        method.setAccessible(true);
        if (!(boolean) method.invoke(null, battle)) {
            throw new RuntimeException("Players cannot battle");
        }
        return battle;
    }

    @SneakyThrows
    public static void startBattle(BattleController battle) {
        val pp = battle.participants.getFirst();
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
