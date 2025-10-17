package me.fullidle.ficore.ficore.vCob_21

import me.fullidle.ficore.ficore.common.V1_version
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory

object VCob_21 : V1_version() {
    init {
        FIData.V1_version = this
    }

    override fun getVersion(): String? {
        return "1.21.1"
    }

    override fun registerForgeEvent() {}

    override fun register(plugin: org.bukkit.plugin.Plugin, bus: Any?, target: Any?) {}

    override fun unregisterAllListener(plugin: org.bukkit.plugin.Plugin) {}

    override fun getSpeciesWrapperFactory(): ISpeciesWrapperFactory<*>? {
        return SpeciesWrapperFactory
    }

    override fun getPokemonWrapperFactory(): IPokemonWrapperFactory<*>? {
        return PokemonWrapperFactory
    }
}
