package me.fullidle.ficore.ficore.common.api.commands.exec;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.commands.CommandBuilder;
import me.fullidle.ficore.ficore.common.api.commands.Context;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class TabExec implements TabExecutor {
    public final String name;
    @NotNull
    public final Exec exec;
    public final List<TabExec> thenExecs = new ArrayList<>();

    public TabExec(String name, @NotNull Exec exec, Collection<TabExec> thenExecs) {
        this.name = name;
        this.exec = exec;
        this.thenExecs.addAll(thenExecs);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            if (this.thenExecs.isEmpty()) {
                CommandBuilder.WRONG_FORMAT_EXEC.exec(new Context(sender, command, Collections.emptyMap()));
                return true;
            }
            val context = new Context(sender, command, new HashMap<>());
            TabExec finalExec = this;
            for (String arg : args) {
                val first = finalExec.thenExecs.stream().filter(e -> e.match(context, arg)).findFirst();
                if (!first.isPresent()) {
                    CommandBuilder.WRONG_FORMAT_EXEC.exec(new Context(sender, command, Collections.emptyMap()));
                    return true;
                }
                finalExec = first.get();
                //只有ArgTabExec才有实际作用
                finalExec.writeArg(context, arg);
            }
            context.rawArgs = args;
            finalExec.exec.exec(context);
            return true;
        }
        //根无参执行
        val context = new Context(
                sender, command, Collections.emptyMap()
        );
        context.rawArgs = args;
        this.exec.exec(context);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return allPrompts(this.thenExecs, null);
        }
        TabExec finalExec = this;
        val temp = new Context(sender, command, Collections.emptyMap());
        for (int i = 0; i < args.length - 1; i++) {
            val arg = args[i];
            val first = finalExec.thenExecs.stream().filter(e -> e.match(temp, arg)).findFirst();
            if (!first.isPresent()) return Collections.emptyList();
            finalExec = first.get();
        }
        if (finalExec.thenExecs.isEmpty()) return Collections.emptyList();
        return allPrompts(finalExec.thenExecs, args[args.length - 1]);
    }

    public List<String> allPrompts(List<TabExec> execs, String arg) {
        val list = new ArrayList<String>();
        execs.forEach(e -> list.addAll(e.prompts()));
        if (arg == null || arg.isEmpty()) return list;
        return list.stream().filter(e -> e.startsWith(arg)).collect(Collectors.toList());
    }

    public boolean match(Context context, String arg) {
        return this.name.equals(arg);
    }

    public void writeArg(Context context, String value) {
        //不做任何行为
    }

    public List<String> prompts() {
        return Collections.singletonList(this.name);
    }
}
