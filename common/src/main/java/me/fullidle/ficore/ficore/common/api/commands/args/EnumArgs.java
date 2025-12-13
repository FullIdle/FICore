package me.fullidle.ficore.ficore.common.api.commands.args;

import com.google.common.collect.Lists;
import me.fullidle.ficore.ficore.common.api.commands.Context;
import me.fullidle.ficore.ficore.common.api.commands.args.types.TypeArgs;

public class EnumArgs<T extends Enum<T>> extends TypeArgs<T> {
    private final Class<T> type;

    public EnumArgs(Class<T> type) {
        super(Lists.newArrayList(enumValues(type)));
        this.type = type;
    }

    @Override
    public String serialize(T arg) {
        return arg.name();
    }

    @Override
    public T parse(Context tempContext, String arg) {
        try {
            return Enum.valueOf(type, arg);
        } catch (Exception e) {
            for (T t : enumValues(type)) if (t.name().equalsIgnoreCase(arg)) return t;
        }
        return null;
    }

    public static <T extends Enum<T>> T[] enumValues(Class<T> type) {
        if (!type.isEnum()) throw new IllegalArgumentException("Type is not an enum");
        return type.getEnumConstants();
    }
}
