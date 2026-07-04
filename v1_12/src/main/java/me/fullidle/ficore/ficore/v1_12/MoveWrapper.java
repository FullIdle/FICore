package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IMoveWrapper;

public class MoveWrapper extends IMoveWrapper<AttackBase> {
    public MoveWrapper(AttackBase original) {
        super(original);
    }

    @Override
    public String getName() {
        return getOriginal().getAttackName();
    }

    @Override
    public Class<AttackBase> getType() {
        return AttackBase.class;
    }
}
