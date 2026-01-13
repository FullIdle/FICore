package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import me.fullidle.ficore.ficore.common.V1_version
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.battle.BattleResult
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.ActorManager
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleEndEvent
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleStartEvent
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PokeBattleEndEvent
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PokeBattleStartEvent
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object CobListener {
    fun register() {
        CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.NORMAL, ::battleStartedPre)
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, ::battleEnd)
    }

    fun battleStartedPre(event: BattleStartedEvent.Pre) {
        val wrapper = (FIData.V1_version.battleManager as BattleManager).wrapper(event.battle)
        val pluginManager = Bukkit.getPluginManager()
        val actorManager = V1_version.getInstance().battleManager.actorManager as ActorManager<BattleActor>

        val pokeStartEvent = PokeBattleStartEvent(
            wrapper,
            event.getBattleSide(1)!!.actors.map {
                actorManager.wrap(it)
            }.toTypedArray(),
            event.getBattleSide(2)!!.actors.map {
                actorManager.wrap(it)
            }.toTypedArray()
        )

        pluginManager.callEvent(pokeStartEvent)
        if (pokeStartEvent.isCancelled) {
            event.cancel()
            return
        }

        if (!event.battle.isPvP || event.battle.players.size < 2) return
        val e = PVPBattleStartEvent(wrapper, castPlayer(event.battle.players[0]), castPlayer(event.battle.players[1]))
        pluginManager.callEvent(e)
        if (e.isCancelled) {
            event.cancel()
            return
        }
    }

    fun battleEnd(event: BattleVictoryEvent) {
        val wrapper = (FIData.V1_version.battleManager as BattleManager).wrapper(event.battle)
        val pluginManager = Bukkit.getPluginManager()
        val manager = V1_version.getInstance().battleManager.actorManager as ActorManager<BattleActor>

        pluginManager.callEvent(PokeBattleEndEvent(
            wrapper,
            event.winners.map {
                manager.wrap(it)
            },
            event.losers.map {
                manager.wrap(it)
            }
        ))

        if (!event.battle.isPvP || event.battle.players.size < 2 || event.wasWildCapture) return
        pluginManager.callEvent(
            PVPBattleEndEvent(
                wrapper, mapOf(
                    Bukkit.getPlayer(event.winners.first().uuid) to BattleResult.VICTORY,
                    Bukkit.getPlayer(event.losers.first().uuid) to BattleResult.DEFEAT
                )
            )
        )
    }

    private fun castPlayer(player: ServerPlayer): Player {
        return CraftEntity.getEntity(player) as Player
    }
}
