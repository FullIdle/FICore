package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;

public class LongArgs extends TypeArgs<Long> {
    public LongArgs(Collection<Long> args) {
        super(args);
    }

    @Override
    public String serialize(Long arg) {
        return arg.toString();
    }

    @Override
    public Long parse(Context context, String arg) {
        try {
            return Long.parseLong(arg);
        } catch (NumberFormatException e) {return null;}
    }
}
