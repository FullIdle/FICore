package me.fullidle.ficore.ficore.common.api.inventory.actions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class InvActionFactories {
    private static final Map<Predicate<Object>, InvActionFactory> registered = new HashMap<>();

    public static void register(@NotNull Predicate<?> matcher,@NotNull InvActionFactory factory) {
        registered.put((Predicate<Object>) matcher, factory);
    }

    @Nullable
    public static InvActionFactory match(@NotNull Object obj) {
        for (Map.Entry<Predicate<Object>, InvActionFactory> entry : registered.entrySet())
            if (entry.getKey().test(obj)) return entry.getValue();
        return null;
    }

    public static InvAction create(Object obj) {
        InvActionFactory factory = match(obj);
        if (factory == null) return null;
        return factory.create(obj);
    }

    static {
        register(CommandAction.Factory.MATCHER, CommandAction.Factory.INSTANCE);
    }
}
