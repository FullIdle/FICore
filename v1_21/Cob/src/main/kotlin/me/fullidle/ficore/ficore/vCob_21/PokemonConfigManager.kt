package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.Cobblemon
import me.fullidle.ficore.ficore.common.api.pokemon.IPokemonConfigManager

object PokemonConfigManager : IPokemonConfigManager {
    override fun getPokeMaxLevel(): Int {
        return Cobblemon.config.maxPokemonLevel
    }

    override fun getComputerBoxes(): Int {
        return Cobblemon.config.defaultBoxCount
    }
}
