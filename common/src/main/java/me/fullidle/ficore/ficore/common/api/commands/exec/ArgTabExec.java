package me.fullidle.ficore.ficore.common.api.commands.exec;

import me.fullidle.ficore.ficore.common.api.commands.Context;
import me.fullidle.ficore.ficore.common.api.commands.args.Args;
import me.fullidle.ficore.ficore.common.api.commands.args.RemainingArgs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class ArgTabExec extends TabExec {
    private final Args<?> args;

    public ArgTabExec(String name, Function<Context, String> permission, Exec exec, Collection<TabExec> thenExecs, Args<?> args) {
        super(name, permission, exec, thenExecs);
        this.args = args;
        if (consumesRemaining() && !thenExecs.isEmpty()) {
            throw new IllegalArgumentException("Remaining arguments must be the last command node: " + name);
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean match(Context context, String arg) {
        return this.args.parse(context, arg) != null;
    }

    @Override
    public void writeArg(Context context, String value) {
        context.args.put(this.name, value);
    }

    @Override
    public boolean consumesRemaining() {
        return this.args instanceof RemainingArgs;
    }

    @Override
    public boolean match(Context context, String[] args, int index) {
        if (!consumesRemaining()) return super.match(context, args, index);
        return ((RemainingArgs) this.args).parse(context, Arrays.copyOfRange(args, index, args.length)) != null;
    }

    @Override
    public int writeArgs(Context context, String[] args, int index) {
        if (!consumesRemaining()) return super.writeArgs(context, args, index);
        context.remainingArgs.put(this.name, Arrays.copyOfRange(args, index, args.length));
        return args.length - index;
    }

    @Override
    public List<String> prompts() {
        return this.args.prompts();
    }
}
