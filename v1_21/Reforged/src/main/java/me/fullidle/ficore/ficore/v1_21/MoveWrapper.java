package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.battles.attacks.ImmutableAttack;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IMoveWrapper;

public class MoveWrapper extends IMoveWrapper<ImmutableAttack> {
    public MoveWrapper(ImmutableAttack original) {
        super(original);
    }

    @Override
    public String getName() {
        return getOriginal().getAttackName();
    }

    @Override
    public Class<ImmutableAttack> getType() {
        return ImmutableAttack.class;
    }
}
