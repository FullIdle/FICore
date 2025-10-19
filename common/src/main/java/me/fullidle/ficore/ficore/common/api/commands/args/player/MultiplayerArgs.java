package me.fullidle.ficore.ficore.common.api.commands.args.player;

import com.google.common.collect.Lists;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.commands.Context;
import me.fullidle.ficore.ficore.common.api.commands.args.Args;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MultiplayerArgs implements Args<Set<Player>> {
    public static MultiplayerArgs INSTANCE = new MultiplayerArgs();

    @Override
    public Set<Player> parse(Context context, String arg) {
        val onlinePlayers = Bukkit.getOnlinePlayers();
        switch (arg) {
            case "@a": {
                if (onlinePlayers.isEmpty()) return Collections.emptySet();
                return new HashSet<>(onlinePlayers);
            }
            case "@r": {
                //随机一个玩家
                if (onlinePlayers.isEmpty()) return Collections.emptySet();
                val list = Lists.newArrayList(onlinePlayers);
                val size = list.size();
                if (size == 1) return Collections.singleton(list.get(0));
                val i = ThreadLocalRandom.current().nextInt(size);
                return Collections.singleton(list.get(i));
            }
            case "@s":
            case "@p": {
                if (!(context.sender instanceof Player)) return null;
                return Collections.singleton((Player) context.sender);
            }
        }

        val player = Bukkit.getPlayer(arg);
        return player == null ? null : Collections.singleton(player);
    }

    @Override
    public List<String> prompts() {
        return PlayerArgs.INSTANCE.prompts();
    }
}
