package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;

public class ShortArgs extends TypeArgs<Short>{
    public ShortArgs(Collection<Short> args) {
        super(args);
    }

    @Override
    public String serialize(Short arg) {
        return String.valueOf(arg);
    }

    @Override
    public Short parse(Context tempContext, String arg) {
        return Short.valueOf(arg);
    }
}
