package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.Cobblemon
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonConfigManager

object PokemonConfigManager : IPokemonConfigManager {
    override fun getPokeMaxLevel(): Int {
        return Cobblemon.config.maxPokemonLevel
    }
}
