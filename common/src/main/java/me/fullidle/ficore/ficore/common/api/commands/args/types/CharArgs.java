package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;
import java.util.Collections;

public class CharArgs extends TypeArgs<Character> {
    public static final CharArgs NO_COMPLETE = new CharArgs(Collections.emptyList());

    public CharArgs(Collection<Character> args) {
        super(args);
    }

    @Override
    public String serialize(Character arg) {
        return String.valueOf(arg);
    }

    @Override
    public Character parse(Context tempContext, String arg) {
        return arg.length() > 1 ? null : arg.charAt(0);
    }
}
