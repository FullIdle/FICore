package me.fullidle.ficore.ficore.v1_12;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.BattleQuery;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.ActorManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BattleManager implements IBattleManager<BattleControllerBase> {
    public static BattleManager INSTANCE = new BattleManager();

    /**
     * @throws RuntimeException 玩家已经在对局时或参与的宝可梦正在进化时，会报出
     */
    @NotNull
    @Override
    public IPokeBattle<BattleControllerBase> create(Player p1, Player p2) {
        val pokemonList1 = createPlayerTeam(p1.getUniqueId());
        val pokemonList2 = createPlayerTeam(p1.getUniqueId());
        val pp1 = new PlayerParticipant(((EntityPlayerMP) CraftEntity.getHandle(p1)), pokemonList1, 1);
        val pp2 = new PlayerParticipant(((EntityPlayerMP) CraftEntity.getHandle(p2)), pokemonList2, 1);
        val bc = createBattle(pp1.getParticipantList(), pp2.getParticipantList());
        return new PokeBattle(bc);
    }

    private static List<Pokemon> createPlayerTeam(UUID uuid) {
        val list = Lists.newArrayList(Pixelmon.storageManager.getParty(uuid).getAll());
        list.removeIf(Objects::isNull);
        return list;
    }

    @Override
    public IPokeBattle<BattleControllerBase> create(Actor<?>[] side1, Actor<?>[] side2) {
        val bp1 = new BattleParticipant[side1.length];
        val bp2 = new BattleParticipant[side2.length];
        for (int i = 0; i < side1.length; i++) bp1[i] = ((BattleParticipant) side1[i].getOriginal());
        for (int i = 0; i < side2.length; i++) bp2[i] = ((BattleParticipant) side2[i].getOriginal());
        return new PokeBattle(createBattle(bp1, bp2));
    }

    @Nullable
    @Override
    public IPokeBattle<BattleControllerBase> getBattle(Player player) {
        return getBattle((EntityPlayerMP) CraftEntity.getHandle(player));
    }

    @Override
    public @NotNull IPokeBattle<BattleControllerBase> getBattle(Actor<?> actor) {
        return new PokeBattle(((BattleParticipant) actor.getOriginal()).bc);
    }

    @Override
    public @NotNull IPokeBattle<BattleControllerBase> wrapper(BattleControllerBase battle) {
        return new PokeBattle(Objects.requireNonNull(battle));
    }

    public IPokeBattle<BattleControllerBase> getBattle(EntityPlayerMP player) {
        val battle = BattleRegistry.getBattle(player);
        return battle == null ? null : new PokeBattle(battle);
    }

    @Override
    public void createQuery(Player p1, Player p2) {
        val player1 = (EntityPlayerMP) CraftEntity.getHandle(p1);
        val player2 = (EntityPlayerMP) CraftEntity.getHandle(p2);
        String errorT = "玩家没有可参与的宝可梦!";
        val pokemon1 = Objects.requireNonNull(Pixelmon.storageManager.getParty(player1).getFirstAblePokemon(), errorT);
        val pokemon2 = Objects.requireNonNull(Pixelmon.storageManager.getParty(player2).getFirstAblePokemon(), errorT);
        new BattleQuery(
                player1,
                pokemon1.getOrSpawnPixelmon(player1),
                player2,
                pokemon2.getOrSpawnPixelmon(player2)
        );
    }

    @Override
    public ActorManager<?> getActorManager() {
        return V12ActorManager.INSTANCE;
    }

    @Getter
    public static class PokeBattle extends IPokeBattle<BattleControllerBase> {
        public PokeBattle(BattleControllerBase bc) {
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
            val actorManager = ((V12ActorManager) FIData.V1_version.getBattleManager().getActorManager());
            val actors = new ArrayList<Actor<?>>();
            for (BattleParticipant p : this.getOriginal().participants)
                if (p.getType() == ParticipantType.Player) actors.add(actorManager.wrap(p));
            return actors;
        }

        @Override
        public Collection<Actor<?>> getActors() {
            val actors = new ArrayList<Actor<?>>();
            val actorManager = ((V12ActorManager) FIData.V1_version.getBattleManager().getActorManager());
            for (BattleParticipant participant : getOriginal().participants) {
                if (participant == null) continue;
                actors.add(actorManager.wrap(participant));
            }
            return actors;
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
        public Class<BattleControllerBase> getType() {
            return BattleControllerBase.class;
        }
    }

    public static class V12ActorManager implements ActorManager<BattleParticipant> {
        public static final V12ActorManager INSTANCE = new V12ActorManager();

        @Override
        public @NotNull Actor<?> create(Entity entity) throws UnsupportedOperationException {
            if (entity instanceof Player)
                return new V12Actor(new PlayerParticipant(((EntityPlayerMP) CraftEntity.getHandle(entity)), createPlayerTeam(entity.getUniqueId()), 1));
            {
                val factory = FIData.V1_version.getPokeEntityWrapperFactory();
                if (factory.isPokeEntity(entity))
                    return new V12Actor(new WildPixelmonParticipant(((EntityPixelmon) factory.asPokeEntity(entity).getOriginal())));
            }
            val factory = FIData.V1_version.getPokeNPCEntityWrapperFactory();
            if (factory.isPokeNPCEntity(entity)) {
                val npc = factory.asPokeNPCEntity(entity);
                if (npc.getOriginal() instanceof NPCTrainer)
                    return new V12Actor(new TrainerParticipant((NPCTrainer) npc.getOriginal(), 1));
            }
            throw new UnsupportedOperationException("不支持该类型的实体 >> " + entity.getType().name());
        }

        @Override
        public @NotNull Actor<BattleParticipant> wrap(BattleParticipant bp) {
            return new V12Actor(bp);
        }

        @Override
        public @Nullable Actor<?> getActor(Entity entity) {
            return null;
        }
    }

    public static class V12Actor extends Actor<BattleParticipant> {
        public V12Actor(BattleParticipant original) {
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
            val factory = ((PokemonWrapperFactory) FIData.V1_version.getPokemonWrapperFactory());
            for (PixelmonWrapper wrapper : getOriginal().allPokemon) {
                if (wrapper == null) continue;
                list.add(factory.create(wrapper.pokemon));
            }
            return list;
        }

        @Override
        public Collection<IPokemonWrapper<?>> getCurrents() {
            val list = new ArrayList<IPokemonWrapper<?>>();
            val factory = ((PokemonWrapperFactory) FIData.V1_version.getPokemonWrapperFactory());
            for (PixelmonWrapper wrapper : getOriginal().controlledPokemon) {
                if (wrapper == null) continue;
                list.add(factory.create(wrapper.pokemon));
            }
            return list;
        }

        @Override
        public Class<BattleParticipant> getType() {
            return BattleParticipant.class;
        }
    }

    /**
     * original
     *
     * @see BattleRegistry#startBattle(BattleParticipant[], BattleParticipant[], BattleRules)
     */
    @NotNull
    public static BattleControllerBase createBattle(BattleParticipant[] team1, BattleParticipant[] team2) {
        BattleControllerBase battle = new BattleControllerBase(team1, team2, new BattleRules(EnumBattleType.Single));

        for (BattleParticipant p : battle.participants) {
            if (p instanceof PlayerParticipant && BattleRegistry.getBattle(((PlayerParticipant) p).player) != null)
                throw new RuntimeException("Player is already in a battle!");

            PartyStorage party = p.getStorage();
            if (party != null && party.findOne((pokemon) -> pokemon.getPixelmonIfExists() != null && pokemon.getPixelmonIfExists().isEvolving()) != null)
                throw new RuntimeException("Player is evolving!");
        }
        return battle;
    }

    public static void startBattle(BattleControllerBase battle) {
        if (battle.participants.size() == 2) {
            BattleParticipant bp1 = battle.participants.get(0);
            BattleParticipant bp2 = battle.participants.get(1);
            if (bp1 instanceof PlayerParticipant && bp2 instanceof TrainerParticipant || bp2 instanceof PlayerParticipant && bp1 instanceof TrainerParticipant) {
                NPCEvent.StartBattle npcStartBattleEvent = new NPCEvent.StartBattle(battle.participants);
                Pixelmon.EVENT_BUS.post(npcStartBattleEvent);
                if (npcStartBattleEvent.isCanceled()) {
                    return;
                }
            }
        }

        val bp = battle.participants.get(0);

        BattleStartedEvent battleStartedEvent = new BattleStartedEvent(battle, battle.getTeam(bp).toArray(new BattleParticipant[0]), battle.getOpponents(bp).toArray(new BattleParticipant[0]));
        Pixelmon.EVENT_BUS.post(battleStartedEvent);
        if (!battleStartedEvent.isCanceled()) {
            BattleRegistry.registerBattle(battle);

            for (BattleParticipant p : battle.getPlayers()) {
                EntityPlayerMP player = (EntityPlayerMP) p.getEntity();

                for (BattleParticipant p2 : battle.participants) {
                    if (p2 != p) {
                        PixelmonWrapper pix = p2.allPokemon[0];
                        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueID());
                        if (!Pixelmon.EVENT_BUS.post(new PokedexEvent(player.getUniqueID(), pix.pokemon, EnumPokedexRegisterStatus.seen, "pokedexKey"))) {
                            storage.pokedex.set(pix.pokemon, EnumPokedexRegisterStatus.seen);
                            storage.pokedex.update();
                            storage.pokedex.update();
                        }
                    }
                }
            }
        }
    }
}
