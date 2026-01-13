package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent
import com.cobblemon.mod.common.battles.BattleFormat
import com.cobblemon.mod.common.battles.BattleRegistry
import com.cobblemon.mod.common.battles.BattleSide
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.cobblemon.mod.common.battles.actor.PokemonBattleActor
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import com.cobblemon.mod.common.entity.npc.NPCBattleActor
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import me.fullidle.ficore.ficore.common.V1_version
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IPokeBattle
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.Actor
import me.fullidle.ficore.ficore.common.api.pokemon.battle.actor.ActorManager
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeNPCEntityWrapperFactory
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity
import net.minecraft.world.entity.LivingEntity
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
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

    override fun create(
        side1: Array<out Actor<*>>,
        side2: Array<out Actor<*>>
    ): IPokeBattle<PokemonBattle> {
        val pb = PokemonBattle(
            BattleFormat.GEN_9_SINGLES,
            BattleSide(*side1.map { it.original as BattleActor }.toTypedArray()),
            BattleSide(*side2.map { it.original as BattleActor }.toTypedArray())
        )
        return PokeBattle(pb)
    }

    override fun getBattle(player: Player): IPokeBattle<PokemonBattle>? {
        return BattleRegistry.getBattleByParticipatingPlayerId(player.uniqueId)?.let {
            PokeBattle(it)
        }
    }

    override fun getBattle(actor: Actor<*>): IPokeBattle<PokemonBattle> {
        return wrapper((actor.original as BattleActor).battle)
    }

    override fun wrapper(battle: PokemonBattle): IPokeBattle<PokemonBattle> {
        return PokeBattle(battle)
    }

    override fun getActorManager(): ActorManager<*> {
        return CobActorManager
    }

    object CobActorManager : ActorManager<BattleActor> {
        override fun create(entity: Entity): Actor<*> {
            val pokemonFactory =
                V1_version.getInstance().pokemonWrapperFactory as PokeEntityWrapperFactory<PokemonEntity>
            val npcFactory =
                V1_version.getInstance().pokeNPCEntityWrapperFactory as PokeNPCEntityWrapperFactory<NPCEntity>

            return CobActor(
                when {
                    entity is Player -> {
                        entity.uniqueId.let {
                            PlayerBattleActor(it, it.getPlayer()!!.party().toBattleTeam())
                        }
                    }

                    pokemonFactory.isPokeEntity(entity) -> {
                        PokemonBattleActor(
                            entity.uniqueId,
                            BattlePokemon.safeCopyOf((pokemonFactory.asPokeEntity(entity).original as PokemonEntity).pokemon),
                            16.0f
                        )
                    }

                    npcFactory.isPokeNPCEntity(entity) -> {
                        (npcFactory.asPokeNPCEntity(entity).original as NPCEntity).let {
                            NPCBattleActor(it, it.party ?: throw UnsupportedOperationException("不支持的npc 无队伍"), it.skill ?: 1)
                        }
                    }

                    else -> throw UnsupportedOperationException("不支持的实体类型 >> ${entity.type.name}")
                }
            )
        }

        override fun wrap(t: BattleActor): Actor<BattleActor> {
            return CobActor(t)
        }

        override fun getActor(entity: Entity): Actor<*>? {
            val pokemonFactory =
                V1_version.getInstance().pokemonWrapperFactory as PokeEntityWrapperFactory<PokemonEntity>
            val npcFactory =
                V1_version.getInstance().pokeNPCEntityWrapperFactory as PokeNPCEntityWrapperFactory<NPCEntity>
            return when {
                entity is Player -> {
                    entity.uniqueId.let {
                        BattleRegistry.getBattleByParticipatingPlayerId(it)?.getActor(it)
                    }
                }

                pokemonFactory.isPokeEntity(entity) -> {
                    (pokemonFactory.asPokeEntity(entity).original as PokemonEntity).let { pe ->
                        pe.battle?.getActor(pe.uuid)
                    }
                }

                npcFactory.isPokeNPCEntity(entity) -> {
                    (npcFactory.asPokeNPCEntity(entity).original as NPCEntity).let { npc->
                        npc.battleIds.firstOrNull()?.let {
                            BattleRegistry.getBattle(it)?.getActor(npc.uuid)
                        }
                    }
                }

                else -> null
            }?.let {
                wrap(it)
            }
        }
    }

    class CobActor(original: BattleActor) : Actor<BattleActor>(original) {
        override fun getEntity(): Entity? {
            return (original as? EntityBackedBattleActor<LivingEntity>)?.entity?.let {
                CraftEntity.getEntity(it)
            }
        }

        override fun getTeam(): Collection<IPokemonWrapper<*>> {
            return (V1_version.getInstance().pokemonWrapperFactory as IPokemonWrapperFactory<Pokemon>).let { factory ->
                original.pokemonList.map {
                    factory.create(it.originalPokemon)
                }
            }
        }

        override fun getCurrents(): Collection<IPokemonWrapper<*>> {
            return (V1_version.getInstance().pokemonWrapperFactory as IPokemonWrapperFactory<Pokemon>).let { factory ->
                original.activePokemon.mapNotNull { ap ->
                    ap.battlePokemon?.let {
                        factory.create(it.originalPokemon);
                    }
                }
            }
        }

        override fun getType(): Class<BattleActor> {
            return BattleActor::class.java
        }
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

        override fun getPlayerActors(): Collection<Actor<*>> {
            return (V1_version.getInstance().battleManager.actorManager as ActorManager<BattleActor>).let { manager ->
                original.players.mapNotNull { player ->
                    original.getActor(player)?.let {
                        manager.wrap(it)
                    }
                }
            }
        }

        override fun getActors(): Collection<Actor<*>> {
            return (V1_version.getInstance().battleManager.actorManager as ActorManager<BattleActor>).let { manager ->
                original.actors.map {
                    manager.wrap(it)
                }
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
