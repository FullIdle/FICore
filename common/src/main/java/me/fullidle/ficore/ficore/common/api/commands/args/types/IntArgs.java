package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;

public class IntArgs extends TypeArgs<Integer> {
    public IntArgs(Collection<Integer> args) {
        super(args);
    }

    @Override
    public String serialize(Integer arg) {
        return arg.toString();
    }

    @Override
    public Integer parse(Context context, String arg) {
        return Integer.parseInt(arg);
    }
}
