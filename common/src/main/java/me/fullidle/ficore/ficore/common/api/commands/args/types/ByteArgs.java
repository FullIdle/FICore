package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.Collection;

public class ByteArgs extends TypeArgs<Byte> {
    public ByteArgs(Collection<Byte> args) {
        super(args);
    }

    @Override
    public String serialize(Byte arg) {
        return String.valueOf(arg);
    }

    @Override
    public Byte parse(Context tempContext, String arg) {
        try {
            return Byte.parseByte(arg);
        } catch (NumberFormatException e) {return null;}
    }
}
