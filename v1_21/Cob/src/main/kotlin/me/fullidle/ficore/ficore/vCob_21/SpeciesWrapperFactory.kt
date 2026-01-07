package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies.getByName
import com.cobblemon.mod.common.api.pokemon.labels.CobblemonPokemonLabels
import com.cobblemon.mod.common.pokemon.Species
import me.fullidle.ficore.ficore.common.api.pokemon.AbilityWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.Element
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory

object SpeciesWrapperFactory : ISpeciesWrapperFactory<Species> {
    @Throws(IllegalArgumentException::class)
    override fun create(name: String): SpeciesWrapper {
        val species = getByName(name)
        requireNotNull(species) { "Unknown species name: $name" }
        return getCacheOrCreate(species)
    }

    override fun create(original: Species): SpeciesWrapper {
        return getCacheOrCreate(original)
    }

    @Throws(IllegalArgumentException::class)
    override fun create(dex: Int): SpeciesWrapper {
        val species = PokemonSpecies.getByPokedexNumber(dex)
        requireNotNull(species) { "Unknown species dex: $dex" }
        return getCacheOrCreate(species)
    }

    override fun getAll(): Collection<SpeciesWrapper> {
        if (PokemonSpecies.species.size <= cache.size) return ArrayList<SpeciesWrapper>(cache.values)
        return PokemonSpecies.species.map { getCacheOrCreate(it) }
    }

    private val cache: MutableMap<Species, SpeciesWrapper> = HashMap<Species, SpeciesWrapper>()

    fun getCacheOrCreate(es: Species): SpeciesWrapper {
        return cache.computeIfAbsent(es) { original: Species -> SpeciesWrapper(original) }
    }

    class SpeciesWrapper(original: Species) : ISpeciesWrapper<Species>(original) {
        override fun getName(): String {
            return this.original.name
        }

        override fun isLegend(): Boolean {
            return this.original.labels.contains(CobblemonPokemonLabels.LEGENDARY)
        }

        override fun isMythical(): Boolean {
            return this.original.labels.contains(CobblemonPokemonLabels.MYTHICAL)
        }

        /**
         * 如果世代为0证明cob没有配置
         */
        override fun getGeneration(): Int {
            val gen = this.original.labels.find {
                it.startsWith("gen")
            }
            return gen?.let {
                if (it.last().isDigit()) {
                    it.substring(3).toInt()
                } else {
                    it.substring(3, it.length - 1).toInt()
                }
            } ?: 0
        }

        override fun isUltra(): Boolean {
            return this.original.labels.contains(CobblemonPokemonLabels.ULTRA_BEAST)
        }

        override fun getDex(): Int {
            return this.original.nationalPokedexNumber
        }

        override fun getTypes(): List<Element> {
            return this.original.types.map {
                Element.fromString(it.name)
            }
        }

        override fun getAbilities(): List<AbilityWrapper<*>> {
            return this.original.abilities.map {
                me.fullidle.ficore.ficore.vCob_21.AbilityWrapper(it.template)
            }
        }

        override fun getType(): Class<Species> {
            return Species::class.java
        }
    }
}
