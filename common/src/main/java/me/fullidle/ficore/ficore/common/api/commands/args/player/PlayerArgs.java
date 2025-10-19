package me.fullidle.ficore.ficore.common.api.commands.args.player;

import me.fullidle.ficore.ficore.common.api.commands.Context;
import me.fullidle.ficore.ficore.common.api.commands.args.Args;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerArgs implements Args<Player> {
    public static PlayerArgs INSTANCE = new PlayerArgs();

    @Override
    public Player parse(Context context, String arg) {
        return Bukkit.getPlayer(arg);
    }

    @Override
    public List<String> prompts() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}
