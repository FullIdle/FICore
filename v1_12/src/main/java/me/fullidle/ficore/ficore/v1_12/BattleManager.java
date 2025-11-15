package me.fullidle.ficore.ficore.v1_12;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.Wrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class BattleManager implements IBattleManager<BattleControllerBase> {
    public static BattleManager INSTANCE = new BattleManager();

    /**
     * @throws RuntimeException 玩家已经在对局时或参与的宝可梦正在进化时，会报出
     */
    @NotNull
    @Override
    public IPokeBattle<BattleControllerBase> create(Player p1, Player p2) {
        val pokemonList1 = Lists.newArrayList(Pixelmon.storageManager.getParty(p1.getUniqueId()).getAll());
        val pokemonList2 = Lists.newArrayList(Pixelmon.storageManager.getParty(p2.getUniqueId()).getAll());
        pokemonList1.removeIf(Objects::isNull);
        pokemonList2.removeIf(Objects::isNull);
        val pp1 = new PlayerParticipant(((EntityPlayerMP) CraftEntity.getHandle(p1)), pokemonList1, 1);
        val pp2 = new PlayerParticipant(((EntityPlayerMP) CraftEntity.getHandle(p2)), pokemonList2, 1);
        val bc = createBattle(pp1.getParticipantList(), pp2.getParticipantList());
        return new PokeBattle(bc);
    }

    @Nullable
    @Override
    public IPokeBattle<BattleControllerBase> getBattle(Player player) {
        return getBattle((EntityPlayerMP) CraftEntity.getHandle(player));
    }

    @Override
    public IPokeBattle<BattleControllerBase> wrapper(BattleControllerBase battle) {
        return new PokeBattle(Objects.requireNonNull(battle));
    }

    public IPokeBattle<BattleControllerBase> getBattle(EntityPlayerMP player) {
        val battle = BattleRegistry.getBattle(player);
        return battle == null ? null : new PokeBattle(battle);
    }

    @Getter
    public static class PokeBattle extends IPokeBattle<BattleControllerBase> {
        private final BattleControllerBase original;

        public PokeBattle(BattleControllerBase bc) {
            this.original = bc;
        }

        @Override
        public Collection<Player> getPlayers() {
            return this.original.getPlayers()
                    .stream()
                    .map(s -> ((Player) CraftEntity.getEntity(s.player)))
                    .collect(Collectors.toList());
        }

        @Override
        public void end() {
            this.original.endBattle();
        }

        @Override
        public void start() {
            startBattle(this.original);
        }

        @Override
        public Class<BattleControllerBase> getType() {
            return BattleControllerBase.class;
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
