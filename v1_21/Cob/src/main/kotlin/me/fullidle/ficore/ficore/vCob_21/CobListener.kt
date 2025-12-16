package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.battle.BattleResult
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleEndEvent
import me.fullidle.ficore.ficore.common.api.pokemon.event.battle.PVPBattleStartEvent
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object CobListener {
    fun register() {
        CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.NORMAL,::battleStartedPre)
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL,::battleEnd)
    }

    fun battleStartedPre(event: BattleStartedEvent.Pre) {
        if (!event.battle.isPvP || event.battle.players.size < 2) return
        val wrapper = (FIData.V1_version.battleManager as BattleManager).wrapper(event.battle)
        val e = PVPBattleStartEvent(wrapper, castPlayer(event.battle.players[0]), castPlayer(event.battle.players[1]))
        Bukkit.getPluginManager().callEvent(e)
        if (e.isCancelled) event.cancel()
    }

    fun battleEnd(event: BattleVictoryEvent) {
        if (!event.battle.isPvP || event.battle.players.size < 2 || event.wasWildCapture) return
        val wrapper = (FIData.V1_version.battleManager as BattleManager).wrapper(event.battle)
        Bukkit.getPluginManager().callEvent(PVPBattleEndEvent(wrapper, mapOf(
            Bukkit.getPlayer(event.winners.first().uuid) to BattleResult.VICTORY,
            Bukkit.getPlayer(event.losers.first().uuid) to BattleResult.DEFEAT
        )))
    }

    private fun castPlayer(player: ServerPlayer): Player {
        return CraftEntity.getEntity(player) as Player
    }
}
