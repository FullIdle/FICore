package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;
import me.fullidle.ficore.ficore.common.api.commands.args.Args;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UUIDArgs extends TypeArgs<UUID> {
    public static final UUIDArgs NO_COMPLETE = new UUIDArgs(Collections.emptyList());

    public UUIDArgs(Collection<UUID> args) {
        super(args);
    }

    @Override
    public String serialize(UUID arg) {
        return arg.toString();
    }

    @Override
    public UUID parse(Context tempContext, String arg) {
        try {
            return UUID.fromString(arg);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
