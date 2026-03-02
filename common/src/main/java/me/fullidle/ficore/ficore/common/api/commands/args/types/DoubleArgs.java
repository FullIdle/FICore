package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;
import java.util.Collections;

public class DoubleArgs extends TypeArgs<Double> {
    public static final DoubleArgs NO_COMPLETE = new DoubleArgs(Collections.emptyList());

    public DoubleArgs(Collection<Double> args) {
        super(args);
    }

    @Override
    public String serialize(Double arg) {
        return arg.toString();
    }

    @Override
    public Double parse(Context context, String arg) {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {return null;}
    }
}
