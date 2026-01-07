package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.pokemon.Nature
import me.fullidle.ficore.ficore.common.api.pokemon.NatureWrapper

class NatureWrapper(original: Nature) : NatureWrapper<Nature>(original) {
    override fun getName(): String {
        return original.displayName
    }

    override fun getType(): Class<Nature> {
        return Nature::class.java
    }
}
