package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.pokemon.Pokemon
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper
import org.bukkit.Bukkit
import org.bukkit.entity.Entity

object PokemonWrapperFactory : IPokemonWrapperFactory<Pokemon> {
    override fun create(original: Pokemon): IPokemonWrapper<Pokemon> {
        return PokemonWrapper(original)
    }

    override fun create(speciesWrapper: ISpeciesWrapper<*>): IPokemonWrapper<Pokemon> {
        val pokemon = (speciesWrapper as SpeciesWrapperFactory.SpeciesWrapper).original!!.create((1..50).random())
        return create(pokemon)
    }

    class PokemonWrapper(private val original: Pokemon) : IPokemonWrapper<Pokemon>() {
        override fun getEntity(): Entity? {
            return original.entity?.bukkitEntity
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

        override fun getOriginal(): Pokemon {
            return original
        }

        override fun getType(): Class<Pokemon> {
            return Pokemon::class.java
        }
    }
}