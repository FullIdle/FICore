package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;

public class FloatArgs extends TypeArgs<Float>{
    public FloatArgs(Collection<Float> args) {
        super(args);
    }

    @Override
    public String serialize(Float arg) {
        return String.valueOf(arg);
    }

    @Override
    public Float parse(Context tempContext, String arg) {
        try {
            return Float.parseFloat(arg);
        } catch (NumberFormatException e) {return null;}
    }
}
