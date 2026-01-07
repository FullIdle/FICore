package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.api.pokemon.Nature;

public class NatureWrapper extends me.fullidle.ficore.ficore.common.api.pokemon.NatureWrapper<Nature> {
    public NatureWrapper(Nature original) {
        super(original);
    }

    @Override
    public String getName() {
        return getOriginal().name();
    }

    @Override
    public Class<Nature> getType() {
        return Nature.class;
    }
}
