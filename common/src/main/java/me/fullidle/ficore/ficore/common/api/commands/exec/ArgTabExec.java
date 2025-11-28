package me.fullidle.ficore.ficore.common.api.commands.exec;

import me.fullidle.ficore.ficore.common.api.commands.Context;
import me.fullidle.ficore.ficore.common.api.commands.args.Args;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ArgTabExec extends TabExec {
    private final Args<?> args;

    public ArgTabExec(String name, Exec exec, Collection<TabExec> thenExecs, Args<?> args) {
        super(name, exec, thenExecs);
        this.args = args;
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
    public List<String> prompts() {
        return this.args.prompts();
    }
}
