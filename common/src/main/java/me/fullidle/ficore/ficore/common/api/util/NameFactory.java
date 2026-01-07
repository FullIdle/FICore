package me.fullidle.ficore.ficore.common.api.util;

import java.util.List;

public abstract class NameFactory<T> {
    public abstract T create(String name);
    public abstract List<String> names();
}
