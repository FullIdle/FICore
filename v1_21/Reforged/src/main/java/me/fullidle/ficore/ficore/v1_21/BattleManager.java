package me.fullidle.ficore.ficore.v1_21;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.Wrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class BattleManager implements IBattleManager {
    public static BattleManager INSTANCE = new BattleManager();

    /**
     * @throws RuntimeException 玩家已经在对局时或参与的宝可梦正在进化时，会报出
     */
    @NotNull
    @Override
    public IPokeBattle create(Player p1, Player p2) {
        val pokemonList1 = Lists.newArrayList(StorageProxy.getPartyNow(p1.getUniqueId()).getAll());
        val pokemonList2 = Lists.newArrayList(StorageProxy.getPartyNow(p2.getUniqueId()).getAll());
        pokemonList1.removeIf(Objects::isNull);
        pokemonList2.removeIf(Objects::isNull);
        val pp1 = new PlayerParticipant(((ServerPlayer) CraftEntity.getHandle(p1)), pokemonList1, 1);
        val pp2 = new PlayerParticipant(((ServerPlayer) CraftEntity.getHandle(p2)), pokemonList2, 2);
        val bc = createBattle(new BattleParticipant[]{pp1}, new BattleParticipant[]{pp2});
        return new PokeBattle(bc);
    }

    @Nullable
    @Override
    public IPokeBattle getBattle(Player player) {
        return getBattle((ServerPlayer) CraftEntity.getHandle(player));
    }

    public IPokeBattle getBattle(ServerPlayer player) {
        val battle = BattleRegistry.getBattle(player);
        return battle == null ? null : new PokeBattle(battle);
    }

    @Getter
    public static class PokeBattle extends Wrapper<BattleController> implements IPokeBattle {
        private final BattleController original;

        public PokeBattle(BattleController bc) {
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
        public Class<BattleController> getType() {
            return BattleController.class;
        }

    }

    /**
     * original
     *
     * @see BattleRegistry#startBattle(BattleParticipant[], BattleParticipant[], BattleRules, HolderLookup.Provider)
     */
    @SneakyThrows
    @NotNull
    public static BattleController createBattle(BattleParticipant[] team1, BattleParticipant[] team2) {
        BattleController battle = new BattleController(team1, team2, new BattleRules(BattleType.SINGLE), team1[0].getEntity().registryAccess());
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
        val array2 = pp.getOpponents().toArray(new BattleParticipant[0]);
        val invoke = (boolean) method.invoke(null, battle, array1, array2);
        if (!invoke) return;
        BattleRegistry.registerBattle(battle);
        Pixelmon.EVENT_BUS.post(new BattleStartedEvent.Post(battle, array1, array2));
    }
}
