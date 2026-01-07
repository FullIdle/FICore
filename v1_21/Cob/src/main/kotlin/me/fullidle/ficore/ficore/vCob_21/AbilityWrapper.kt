package me.fullidle.ficore.ficore.vCob_21

import com.cobblemon.mod.common.api.abilities.AbilityTemplate
import me.fullidle.ficore.ficore.common.api.pokemon.AbilityWrapper

class AbilityWrapper(original: AbilityTemplate) : AbilityWrapper<AbilityTemplate>(original) {
    override fun getName(): String {
        return this.original.name
    }

    override fun getType(): Class<AbilityTemplate> {
        return AbilityTemplate::class.java
    }
}
