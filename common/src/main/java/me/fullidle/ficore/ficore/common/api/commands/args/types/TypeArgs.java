package me.fullidle.ficore.ficore.common.api.commands.args.types;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.commands.args.Args;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class TypeArgs<T> implements Args<T> {
    private final List<String> args;

    public TypeArgs(Collection<T> args) {
        val list = new ArrayList<String>(args.size());
        for (T arg : args) list.add(serialize(arg));
        this.args = Collections.unmodifiableList(list);
    }

    public abstract String serialize(T arg);

    @Override
    public List<String> prompts() {
        return new ArrayList<>(this.args);
    }
}
