package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper
import me.fullidle.ficore.ficore.common.bukkit.CraftWorld
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object PokemonWrapperFactory : IPokemonWrapperFactory<Pokemon> {
    override fun create(original: Pokemon): IPokemonWrapper<Pokemon> {
        return PokemonWrapper(original)
    }

    override fun create(speciesWrapper: ISpeciesWrapper<*>): IPokemonWrapper<Pokemon> {
        val pokemon = (speciesWrapper as SpeciesWrapperFactory.SpeciesWrapper).original!!.create((1..50).random())
        return create(pokemon)
    }

    class PokemonWrapper(original: Pokemon) : IPokemonWrapper<Pokemon>(original) {
        override fun getEntity(): Entity? {
            return original.entity?.let {
                CraftEntity.getEntity(it)
            }
        }

        override fun spawnEntity(location: Location): Entity {
            return this.original.sendOut(
                CraftWorld.getHandle(location.world) as ServerLevel,
                Vec3(location.x, location.y, location.z),
                null
            )!!.let {
                CraftEntity.getEntity(it)
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

        override fun getType(): Class<Pokemon> {
            return Pokemon::class.java
        }
    }
}