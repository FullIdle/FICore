package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityManager
import me.fullidle.ficore.ficore.common.api.pokemon.pokeball.PokeBallEntityWrapper

object VCobPokeBallEntityManager: PokeBallEntityManager<EmptyPokeBallEntity> {
    override fun wrap(pokeBallEntity: EmptyPokeBallEntity): PokeBallEntityWrapper<EmptyPokeBallEntity> {
        return VCobPokeBallEntityWrapper(pokeBallEntity)
    }

    class VCobPokeBallEntityWrapper(original: EmptyPokeBallEntity) : PokeBallEntityWrapper<EmptyPokeBallEntity>(original) {
        override fun getType(): Class<EmptyPokeBallEntity> {
            return EmptyPokeBallEntity::class.java
        }
    }
}