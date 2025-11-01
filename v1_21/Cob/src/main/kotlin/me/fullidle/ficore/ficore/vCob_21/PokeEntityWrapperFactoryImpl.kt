package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity
import org.bukkit.entity.Entity

object PokeEntityWrapperFactoryImpl : PokeEntityWrapperFactory<PokemonEntity> {
    override fun create(entity: PokemonEntity): PokeEntityWrapper<PokemonEntity> {
        return PokeEntityWrapperImpl(entity)
    }

    override fun isPokeEntity(entity: Entity): Boolean {
        return CraftEntity.getHandle(entity) is PokemonEntity
    }

    override fun asPokeEntity(entity: Entity): PokeEntityWrapper<PokemonEntity> {
        if (!isPokeEntity(entity)) throw IllegalArgumentException("Entity is not a PokeEntity")
        return create(CraftEntity.getHandle(entity) as PokemonEntity)
    }

    class PokeEntityWrapperImpl(
        entity: PokemonEntity
    ) : PokeEntityWrapper<PokemonEntity>(
        entity
    ) {
        override fun isBoss(): Boolean {
            return false
        }

        override fun getPokemon(): IPokemonWrapper<*> {
            return (FIData.V1_version.pokemonWrapperFactory as PokemonWrapperFactory).create(this.original.pokemon)
        }

        override fun canDespawn(): Boolean {
            return true
        }

        override fun inBattle(): Boolean {
            return this.original.battleId != null
        }

    }
}
