package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.storage.PokemonStore
import com.cobblemon.mod.common.api.storage.StorePosition
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.cobblemon.mod.common.entity.npc.NPCPlayerModelType
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapper
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapperFactory
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity
import org.bukkit.entity.Entity
import java.net.URI

object PokeNPCEntityWrapperFactoryImpl : PokeNPCEntityWrapperFactory<NPCEntity> {
    override fun create(entity: NPCEntity): PokeNPCEntityWrapper<NPCEntity> {
        return PokeNPCEntityWrapperImpl(entity)
    }

    override fun isPokeNPCEntity(entity: Entity): Boolean {
        return CraftEntity.getHandle(entity) is NPCEntity
    }

    override fun asPokeNPCEntity(entity: Entity): PokeNPCEntityWrapper<NPCEntity> {
        if (isPokeNPCEntity(entity)) return PokeNPCEntityWrapperImpl(CraftEntity.getHandle(entity) as NPCEntity)
        throw IllegalArgumentException("Entity $entity is not a PokeNPCEntity")
    }


    class PokeNPCEntityWrapperImpl(
        npc: NPCEntity
    ) : PokeNPCEntityWrapper<NPCEntity>(npc) {
        override fun getType(): Class<NPCEntity> {
            return NPCEntity::class.java
        }

        override fun getStorage(): IPokeStorageWrapper<*>? {
            return original.party?.let {
                PokeStorageManager.PokeStorageWrapper(it as PokemonStore<StorePosition>)
            }
        }

        override fun setTexture(texture: String) {
            original.loadTexture(URI.create(texture), NPCPlayerModelType.DEFAULT)
        }
    }
}
