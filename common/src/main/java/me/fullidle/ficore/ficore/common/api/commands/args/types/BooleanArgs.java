package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;
import java.util.Collections;

public class BooleanArgs extends TypeArgs<Boolean> {
    public static final BooleanArgs NO_COMPLETE = new BooleanArgs(Collections.emptyList());

    public BooleanArgs(Collection<Boolean> args) {
        super(args);
    }

    @Override
    public String serialize(Boolean arg) {
        return arg.toString();
    }

    @Override
    public Boolean parse(Context context, String arg) {
        return Boolean.parseBoolean(arg);
    }
}
