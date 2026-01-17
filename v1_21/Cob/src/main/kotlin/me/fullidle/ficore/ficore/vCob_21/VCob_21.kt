package me.fullidle.ficore.ficore.vCob_21

import me.fullidle.ficore.ficore.common.V1_version
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.IPokemonConfigManager
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager
import me.fullidle.ficore.ficore.common.api.pokemon.breeds.IBreedLogic
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapperFactory
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityManager
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.*

object VCob_21 : V1_version() {
    override fun hasPokemon(): Boolean {
        return true
    }

    init {
        FIData.V1_version = this
    }

    override fun getVersion(): String? {
        return "1.21.1"
    }

    override fun registerForgeEvent() {
        if (hasPokemon()) CobListener.register()
    }

    override fun register(plugin: org.bukkit.plugin.Plugin, bus: Any, target: Any) {
    }

    override fun unregisterAllListener(plugin: org.bukkit.plugin.Plugin) {
        if (FIData.plugin.equals(plugin)) CobListener.unregister()
    }

    override fun getSpeciesWrapperFactory(): ISpeciesWrapperFactory<*>? {
        return SpeciesWrapperFactory
    }

    override fun getPokemonWrapperFactory(): IPokemonWrapperFactory<*>? {
        return PokemonWrapperFactory
    }

    override fun getBreedLogic(): IBreedLogic? {
        throw UnsupportedOperationException()
    }

    override fun getBattleManager(): IBattleManager<*> {
        return BattleManager
    }

    override fun getPokeStorageManager(): IPokeStorageManager? {
        return PokeStorageManager
    }

    override fun getPokeEntityWrapperFactory(): PokeEntityWrapperFactory<*>? {
        return PokeEntityWrapperFactoryImpl
    }

    override fun getPokemonConfigManager(): IPokemonConfigManager? {
        return PokemonConfigManager
    }

    override fun getPokeNPCEntityWrapperFactory(): PokeNPCEntityWrapperFactory<*> {
        return PokeNPCEntityWrapperFactoryImpl
    }

    override fun getPokeBallEntityManager(): PokeBallEntityManager<*> {
        return VCobPokeBallEntityManager
    }
}
