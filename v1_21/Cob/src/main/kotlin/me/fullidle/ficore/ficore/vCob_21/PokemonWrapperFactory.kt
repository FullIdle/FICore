package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.storage.PokemonStore
import com.cobblemon.mod.common.api.storage.StorePosition
import com.cobblemon.mod.common.api.storage.party.PartyPosition
import com.cobblemon.mod.common.api.storage.pc.PCPosition
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.PokemonStats
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.Gender
import me.fullidle.ficore.ficore.common.api.pokemon.Stats
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapper
import me.fullidle.ficore.ficore.common.bukkit.CraftWorld
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

object PokemonWrapperFactory : IPokemonWrapperFactory<Pokemon> {
    override fun create(original: Pokemon): IPokemonWrapper<Pokemon> {
        return PokemonWrapper(original)
    }

    override fun create(speciesWrapper: ISpeciesWrapper<*>): IPokemonWrapper<Pokemon> {
        val pokemon = (speciesWrapper as SpeciesWrapperFactory.SpeciesWrapper).original!!.create((1..50).random())
        return create(pokemon)
    }

    class PokemonWrapper(original: Pokemon) : IPokemonWrapper<Pokemon>(original) {
        override fun getEntity(): PokeEntityWrapper<*>? {
            return original.entity?.let {
                (FIData.V1_version.pokeEntityWrapperFactory as PokeEntityWrapperFactoryImpl).create(it)
            }
        }

        override fun spawnEntity(location: Location): PokeEntityWrapper<*>? {
            return this.original.sendOut(
                CraftWorld.getHandle(location.world) as ServerLevel,
                Vec3(location.x, location.y, location.z),
                null
            )!!.let {
                (FIData.V1_version.pokeEntityWrapperFactory as PokeEntityWrapperFactoryImpl).create(it)
            }
        }

        override fun getSpecies(): ISpeciesWrapper<*> {
            return (FIData.V1_version.speciesWrapperFactory as SpeciesWrapperFactory).create(original.species)
        }

        override fun getName(): String {
            return original.species.name
        }

        override fun getTranslatedName(): String {
            return original.species.translatedName.string
        }

        override fun givePlayer(player: Player) {
            player.uniqueId.getPlayer()!!.party().add(this.original)
        }

        override fun getStats(): Map<Stats?, Int?>? {
            return com.cobblemon.mod.common.api.pokemon.stats.Stats.PERMANENT.associate {
                Stats.fromString(it.showdownId) to this.original.getStat(it)
            }
        }

        override fun isLegend(): Boolean {
            return this.original.isLegendary()
        }

        override fun isMythical(): Boolean {
            return this.original.isMythical()
        }

        override fun isUltra(): Boolean {
            return this.original.isUltraBeast()
        }

        override fun getOwner(): OfflinePlayer? {
            return Bukkit.getOfflinePlayers().find { p -> p.uniqueId == this.original.getOwnerUUID() }
        }

        override fun getOwnerUUID(): UUID? {
            return this.original.getOwnerUUID()
        }

        override fun getStorage(): IPokeStorageWrapper<*>? {
            return this.original.storeCoordinates.get()?.store?.let {
                PokeStorageManager.PokeStorageWrapper(it as PokemonStore<StorePosition>)
            }
        }

        override fun getStoragePos(): StoragePos? {
            return this.original.storeCoordinates.get()?.let {
                asPos(it.position)
            }
        }

        /**
         * 我接触的版本还没有实现这个功能呢
         */
        override fun isEgg(): Boolean {
            return false
        }

        override fun isShiny(): Boolean {
            return this.original.shiny
        }

        override fun setShiny(shiny: Boolean) {
            this.original.shiny = shiny
        }

        override fun getIVs(): Map<Stats, Int> {
            return asMap(this.original.ivs)
        }

        override fun getEVs(): Map<Stats, Int> {
            return asMap(this.original.evs)
        }

        override fun setEV(type: Stats, value: Int) {
            this.original.ivs[com.cobblemon.mod.common.api.pokemon.stats.Stats.getStat(type.id)] = value
        }

        override fun setIV(type: Stats, value: Int) {
            this.original.evs[com.cobblemon.mod.common.api.pokemon.stats.Stats.getStat(type.id)] = value
        }

        override fun getGender(): Gender? {
            return asGender(this.original.gender)
        }

        override fun setGender(gender: Gender) {
            this.original.gender = asGender(gender)
        }

        override fun getUUID(): UUID? {
            return this.original.uuid
        }

        override fun setUUID(uuid: UUID?) {
            this.original.uuid = uuid
        }

        override fun getHealth(): Int {
            return this.original.currentHealth
        }

        override fun setHealth(health: Int) {
            this.original.currentHealth = health
        }

        override fun getType(): Class<Pokemon> {
            return Pokemon::class.java
        }

        companion object {
            fun asPos(pos: StorePosition): StoragePos {
                return when (pos) {
                    is PartyPosition -> StoragePos(-1, pos.slot)
                    is PCPosition -> StoragePos(pos.box, pos.slot)
                    else -> throw IllegalArgumentException("Unknown position type: $pos")
                }
            }

            fun asGender(gender: com.cobblemon.mod.common.pokemon.Gender): Gender {
                return when (gender) {
                    com.cobblemon.mod.common.pokemon.Gender.MALE -> Gender.MALE
                    com.cobblemon.mod.common.pokemon.Gender.FEMALE -> Gender.FEMALE
                    com.cobblemon.mod.common.pokemon.Gender.GENDERLESS -> Gender.GENDERLESS
                }
            }

            fun asGender(gender: Gender): com.cobblemon.mod.common.pokemon.Gender {
                return when (gender) {
                    Gender.MALE -> com.cobblemon.mod.common.pokemon.Gender.MALE
                    Gender.FEMALE -> com.cobblemon.mod.common.pokemon.Gender.FEMALE
                    Gender.GENDERLESS -> com.cobblemon.mod.common.pokemon.Gender.GENDERLESS
                }
            }

            fun asMap(stats: PokemonStats): Map<Stats, Int> {
                return stats.mapNotNull { entry ->
                    Stats.fromString(entry.key.showdownId)?.let {
                        it to entry.value
                    }
                }.toMap()
            }
        }
    }
}