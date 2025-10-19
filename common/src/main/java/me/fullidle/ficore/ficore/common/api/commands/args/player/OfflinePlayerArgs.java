package me.fullidle.ficore.ficore.common.api.commands.args.player;

import me.fullidle.ficore.ficore.common.api.commands.Context;
import me.fullidle.ficore.ficore.common.api.commands.args.Args;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfflinePlayerArgs implements Args<OfflinePlayer> {
    public static OfflinePlayerArgs INSTANCE = new OfflinePlayerArgs();

    @Override
    public OfflinePlayer parse(Context context, String arg) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(p ->
                        arg.equals(p.getName()) || p.getUniqueId().toString().equals(arg)
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<String> prompts() {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
    }
}
