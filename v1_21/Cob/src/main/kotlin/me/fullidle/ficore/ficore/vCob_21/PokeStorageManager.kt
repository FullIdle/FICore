package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.storage.PokemonStore
import com.cobblemon.mod.common.api.storage.StorePosition
import com.cobblemon.mod.common.api.storage.party.PartyPosition
import com.cobblemon.mod.common.api.storage.pc.PCPosition
import com.cobblemon.mod.common.api.storage.pc.PCStore
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.pc
import me.fullidle.ficore.ficore.common.api.data.FIData
import me.fullidle.ficore.ficore.common.api.pokemon.storage.StoragePos
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageManager
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper
import java.util.*

object PokeStorageManager : IPokeStorageManager {
    override fun getParty(uuid: UUID): IPokeStorageWrapper<*> {
        return PokeStorageWrapper(uuid.getPlayer()!!.party() as PokemonStore<StorePosition>)
    }

    override fun getPC(uuid: UUID): IPokeStorageWrapper<*> {
        return PokeStorageWrapper(uuid.getPlayer()!!.pc() as PokemonStore<StorePosition>)
    }

    class PokeStorageWrapper(
        original: PokemonStore<StorePosition>
    ): IPokeStorageWrapper<PokemonStore<StorePosition>>(original) {
        override fun get(pos: StoragePos): IPokemonWrapper<*>? {
            return this.original[asPos(pos, this.original)]?.let {
                (FIData.V1_version.pokemonWrapperFactory as PokemonWrapperFactory).create(it)
            }
        }

        override fun set(
            pos: StoragePos,
            poke: IPokemonWrapper<*>?
        ) {
            if (poke == null) {
                this.original.remove(asPos(pos, this.original))
                return
            }
            this.original[asPos(pos, this.original)] = poke.original as Pokemon
        }

        override fun add(poke: IPokemonWrapper<*>) {
            this.original.add(poke.original as Pokemon)
        }

        override fun all(): List<IPokemonWrapper<*>?>? {
            val factory = FIData.V1_version.pokemonWrapperFactory as PokemonWrapperFactory
            return this.original.map {
                factory.create(it)
            }.toMutableList()
        }

        override fun getType(): Class<PokemonStore<StorePosition>> {
            return PokemonStore::class.java as Class<PokemonStore<StorePosition>>
        }

        companion object {
            fun asPos(pos: StoragePos, store: PokemonStore<StorePosition>): StorePosition {
                return if (store is PCStore) PCPosition(pos.box, pos.slot) else PartyPosition(pos.slot)
            }
        }
    }
}
