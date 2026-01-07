package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.enums.EnumNature;

public class NatureWrapper extends me.fullidle.ficore.ficore.common.api.pokemon.NatureWrapper<EnumNature> {
    public NatureWrapper(EnumNature original) {
        super(original);
    }

    @Override
    public String getName() {
        return getOriginal().name();
    }

    @Override
    public Class<EnumNature> getType() {
        return EnumNature.class;
    }
}
