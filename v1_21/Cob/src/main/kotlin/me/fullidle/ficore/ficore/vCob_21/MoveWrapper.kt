package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.moves.Move
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IMoveWrapper

class MoveWrapper(original: Move) : IMoveWrapper<Move>(original) {
    override fun getName(): String {
        return original.name
    }

    override fun getType(): Class<Move> {
        return Move::class.java
    }
}
