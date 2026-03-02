package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;
import java.util.Collections;

public class StringArgs extends TypeArgs<String> {
    public static final StringArgs NO_COMPLETE = new StringArgs(Collections.emptyList());

    public StringArgs(Collection<String> args) {
        super(args);
    }

    @Override
    public String serialize(String arg) {
        return arg;
    }

    @Override
    public String parse(Context context, String arg) {
        return arg;
    }
}
