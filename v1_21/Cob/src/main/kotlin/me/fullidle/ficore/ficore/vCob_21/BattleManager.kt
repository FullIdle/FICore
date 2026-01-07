package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent
import com.cobblemon.mod.common.battles.BattleFormat
import com.cobblemon.mod.common.battles.BattleRegistry
import com.cobblemon.mod.common.battles.BattleSide
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

object BattleManager : IBattleManager<PokemonBattle> {
    override fun create(
        p1: Player,
        p2: Player
    ): IPokeBattle<PokemonBattle> {
        val uuid1 = p1.uniqueId
        val uuid2 = p2.uniqueId
        val team1 = uuid1.getPlayer()!!.party().toBattleTeam()
        val team2 = uuid2.getPlayer()!!.party().toBattleTeam()
        val pb = PokemonBattle(
            BattleFormat.GEN_9_SINGLES,
            BattleSide(PlayerBattleActor(uuid1, team1)),
            BattleSide(PlayerBattleActor(uuid2, team2))
        )
        return PokeBattle(pb)
    }

    override fun getBattle(player: Player): IPokeBattle<PokemonBattle>? {
        return BattleRegistry.getBattleByParticipatingPlayerId(player.uniqueId)?.let {
            PokeBattle(it)
        }
    }

    override fun wrapper(battle: PokemonBattle): IPokeBattle<PokemonBattle> {
        return PokeBattle(battle)
    }


    class PokeBattle(
        original: PokemonBattle
    ) : IPokeBattle<PokemonBattle>(original) {
        override fun getPlayers(): Collection<Player>? {
            return original.sides.flatMap { side ->
                side.actors
                    .filterIsInstance<PlayerBattleActor>()
                    .map { actor -> Bukkit.getPlayer(actor.uuid)!! }
            }
        }

        override fun end() {
            original.end()
        }

        override fun start() {
            val start: () -> Unit = {
                (BattleRegistry::class.declaredMemberProperties.find {
                    it.name == "battleMap" && it.returnType == ConcurrentHashMap::class
                }!! as KProperty1<BattleRegistry, ConcurrentHashMap<UUID, PokemonBattle>>).apply {
                    this.isAccessible = true
                }.get(BattleRegistry)[original.battleId] = original
                BattleRegistry::class.declaredMemberFunctions.find {
                    it.name == "startShowdown"
                }!!.call(original)
            }

            val preBattleEvent = BattleStartedEvent.Pre(original)
            CobblemonEvents.BATTLE_STARTED_PRE.postThen(preBattleEvent) {
                start()
                CobblemonEvents.BATTLE_STARTED_POST.post(BattleStartedEvent.Post(original))
                return
            }
            return
        }

        override fun getOriginal(): PokemonBattle {
            return original
        }

        override fun getType(): Class<PokemonBattle> {
            return PokemonBattle::class.java
        }
    }
}
