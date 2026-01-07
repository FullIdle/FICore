package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;

public class AbilityWrapper extends me.fullidle.ficore.ficore.common.api.pokemon.AbilityWrapper<Ability> {
    public AbilityWrapper(Ability original) {
        super(original);
    }

    @Override
    public String getName() {
        return getOriginal().getName();
    }

    @Override
    public Class<Ability> getType() {
        return Ability.class;
    }
}
