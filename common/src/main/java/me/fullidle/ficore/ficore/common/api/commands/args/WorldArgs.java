package me.fullidle.ficore.ficore.common.api.commands.args;

import me.fullidle.ficore.ficore.common.api.commands.Context;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

public class WorldArgs implements Args<World> {
    public static WorldArgs INSTANCE = new WorldArgs();

    @Override
    public World parse(Context tempContext, String arg) {
        return Bukkit.getWorld(arg);
    }

    @Override
    public List<String> prompts() {
        return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
    }
}
