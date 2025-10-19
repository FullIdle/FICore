package me.fullidle.ficore.ficore.common.api.commands;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class CommandBuilder {
    private final String name;
    private final Map<String, CommandBuilder> thenExecs = new HashMap<>();
    private Exec exec = WRONG_FORMAT_EXEC;
    private boolean built = false;

    public CommandBuilder(String name) {
        this.name = name;
    }

    public CommandBuilder then(CommandBuilder builder) {
        if (this.thenExecs.containsKey(builder.name))
            throw new RuntimeException("then already exists -> " + builder.name);
        this.thenExecs.put(builder.name, builder);
        return this;
    }

    public CommandBuilder exec(Exec exec) {
        if (!this.exec.equals(WRONG_FORMAT_EXEC)) throw new RuntimeException("exec already exists");
        this.exec = exec;
        return this;
    }

    public TabExecutor build() {
        if (this.built) throw new RuntimeException("already built");
        val map = new HashMap<String, TabExec>();
        this.thenExecs.forEach((k, v) -> map.put(k, ((TabExec) v.build())));
        this.built = true;
        return new TabExec(name, exec, map.values());
    }

    public PluginCommand getPluginCommand() {
        val command = Bukkit.getPluginCommand(this.name);
        if (command == null) throw new RuntimeException("No plugin registers the command!");
        return command;
    }

    public static CommandBuilder builder(String name) {
        return new CommandBuilder(name);
    }

    public static CommandBuilder args(String name, Args<?> args) {
        return new ArgCommandBuilder(name, args);
    }

    public static class ArgCommandBuilder extends CommandBuilder {
        public Args<?> args;

        public ArgCommandBuilder(String name, Args<?> args) {
            super(name);
            this.args = args;
        }

        @Override
        public TabExecutor build() {
            val build = ((TabExec) super.build());
            return new ArgTabExec(build.name, build.exec, build.thenExecs, this.args);
        }
    }

    public interface Args<T> {
        /**
         * 在执行的时候用
         */
        default T get(Context context, String name) {
            val value = context.args.get(name);
            if (value == null) return null;
            return parse(value);
        }

        /**
         * 在解析的时候用
         */
        T parse(String arg);

        List<String> prompts();
    }

    public static class PlayerArgs implements Args<Player> {
        public static final PlayerArgs INSTANCE = new PlayerArgs();

        @Override
        public Player parse(String arg) {
            return Bukkit.getPlayer(arg);
        }

        @Override
        public List<String> prompts() {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
    }

    public interface Exec {
        void exec(Context context);
    }

    public static class Context {
        public CommandSender sender;
        public Command command;
        public Map<String, String> args;

        public Context(CommandSender sender, Command command, Map<String, String> args) {
            this.sender = sender;
            this.command = command;
            this.args = args;
        }
    }

    public static class TabExec implements TabExecutor {
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
                    WRONG_FORMAT_EXEC.exec(new Context(sender, command, Collections.emptyMap()));
                    return true;
                }
                val map = new HashMap<String, String>();
                TabExec finalExec = this;
                for (String arg : args) {
                    val first = finalExec.thenExecs.stream().filter(e -> e.match(arg)).findFirst();
                    if (!first.isPresent()) {
                        WRONG_FORMAT_EXEC.exec(new Context(sender, command, Collections.emptyMap()));
                        return true;
                    }
                    finalExec = first.get();
                    //只有ArgTabExec才有实际作用
                    finalExec.writeArg(map, arg);
                }
                finalExec.exec.exec(new Context(sender, command, map));
                return true;
            }
            //根无参执行
            this.exec.exec(new Context(
                    sender, command, Collections.emptyMap()
            ));
            return true;
        }

        @Nullable
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (args.length <= 1) {
                return allPrompts(this.thenExecs, null);
            }
            val map = new HashMap<String,String>();
            TabExec finalExec = this;
            for (int i = 0; i < args.length - 1; i++) {
                val arg = args[i];
                val first = finalExec.thenExecs.stream().filter(e -> e.match(arg)).findFirst();
                if (!first.isPresent()) return Collections.emptyList();
                finalExec = first.get();
                finalExec.writeArg(map, arg);
            }
            if (finalExec.thenExecs.isEmpty()) return Collections.emptyList();
            return allPrompts(finalExec.thenExecs, args[args.length - 1]);
        }

        public List<String> allPrompts(List<TabExec> execs, String arg) {
            val list = new ArrayList<String>();
            execs.forEach(e->list.addAll(e.prompts()));
            if (arg == null || arg.isEmpty()) return list;
            return list.stream().filter(e -> e.startsWith(arg)).collect(Collectors.toList());
        }

        public boolean match(String arg) {
            return this.name.equals(arg);
        }

        public void writeArg(Map<String, String> args, String value) {
            //不做任何行为
        }

        public List<String> prompts() {
            return Collections.singletonList(this.name);
        }
    }

    public static class ArgTabExec extends TabExec {
        private final Args<?> args;

        public ArgTabExec(String name, Exec exec, Collection<TabExec> thenExecs, Args<?> args) {
            super(name, exec, thenExecs);
            this.args = args;
        }

        @Nullable
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            return this.args.prompts();
        }

        @Override
        public boolean match(String arg) {
            return this.args.parse(arg) != null;
        }

        @Override
        public void writeArg(Map<String, String> args, String value) {
            args.put(this.name, value);
        }

        @Override
        public List<String> prompts() {
            return this.args.prompts();
        }
    }

    public static final Exec WRONG_FORMAT_EXEC = context -> context.sender.sendMessage(
            String.format("§8[§r%s§8]§c Wrong format",
                    context.command instanceof PluginCommand ?
                            ((PluginCommand) context.command).getPlugin().getDescription().getName()
                            : "Unknown"
            )
    );
}
