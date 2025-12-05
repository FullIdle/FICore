package me.fullidle.ficore.ficore.common.api.commands;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.commands.args.Args;
import me.fullidle.ficore.ficore.common.api.commands.exec.ArgTabExec;
import me.fullidle.ficore.ficore.common.api.commands.exec.Exec;
import me.fullidle.ficore.ficore.common.api.commands.exec.TabExec;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommandBuilder {
    private final String name;
    private Function<Context, String> permission;
    private final Map<String, CommandBuilder> thenExecs = new HashMap<>();
    private Exec exec = WRONG_FORMAT_EXEC;
    private boolean built = false;

    public CommandBuilder(String name) {
        this.name = name;
    }

    public CommandBuilder permission(Function<Context, String> permission) {
        this.permission = permission;
        return this;
    }

    public Function<Context, String> permission() {
        return this.permission;
    }

    public CommandBuilder permission(String permission) {
        this.permission = permission == null ? null : context -> permission;
        return this;
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
        return new TabExec(name, permission, exec, map.values());
    }

    public PluginCommand getPluginCommand() {
        val command = Bukkit.getPluginCommand(this.name);
        if (command == null) throw new RuntimeException("No plugin registers the command!");
        return command;
    }

    public void registerPluginCommand() {
        val build = this.build();
        val command = getPluginCommand();
        command.setExecutor(build);
        command.setTabCompleter(build);
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
            return new ArgTabExec(build.name, build.permission, build.exec, build.thenExecs, this.args);
        }
    }

    public static final Exec WRONG_FORMAT_EXEC = context -> context.sender.sendMessage(
            String.format("§8[§r%s§8]§c Wrong format",
                    context.command instanceof PluginCommand ?
                            ((PluginCommand) context.command).getPlugin().getDescription().getName()
                            : "Unknown"
            )
    );

    public static final Exec NO_PERMISSION_EXEC = context -> context.sender.sendMessage(
            String.format("§8[§r%s§8]§c There is no permission to use the command!",
                    context.command instanceof PluginCommand ?
                            ((PluginCommand) context.command).getPlugin().getDescription().getName()
                            : "Unknown"
            )
    );
}
