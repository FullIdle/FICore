package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;

public class DoubleArgs extends TypeArgs<Double> {
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
