package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;
import java.util.Collections;

public class IntArgs extends TypeArgs<Integer> {
    public static final IntArgs NO_COMPLETE = new IntArgs(Collections.emptyList());

    public IntArgs(Collection<Integer> args) {
        super(args);
    }

    @Override
    public String serialize(Integer arg) {
        return arg.toString();
    }

    @Override
    public Integer parse(Context context, String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {return null;}
    }
}
