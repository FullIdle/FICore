package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;

public class AbilityWrapper extends me.fullidle.ficore.ficore.common.api.pokemon.AbilityWrapper<AbilityBase> {
    public AbilityWrapper(AbilityBase original) {
        super(original);
    }

    @Override
    public String getName() {
        return this.getOriginal().getName();
    }

    @Override
    public Class<AbilityBase> getType() {
        return AbilityBase.class;
    }
}
