package me.fullidle.ficore.ficore;

import me.fullidle.ficore.ficore.common.api.commands.args.player.PlayerArgs;
import me.fullidle.ficore.ficore.common.api.commands.args.types.StringArrayArgs;

import java.util.Arrays;

import static me.fullidle.ficore.ficore.common.api.commands.CommandBuilder.args;
import static me.fullidle.ficore.ficore.common.api.commands.CommandBuilder.builder;

public class Commands {
    public static void register() {
        builder("ficore")
                .then(builder("test")
                        .then(args("values", StringArrayArgs.NO_COMPLETE)
                                .exec(context -> {
                                    String[] values = StringArrayArgs.NO_COMPLETE.get(context, "values");
                                    context.sender.sendMessage(Arrays.toString(values));
                                })))
                .then(builder("tabtest"))
                .then(builder("tabtest2"))
                .then(builder("3tabtest").then(builder("test")))
                .then(args("player", PlayerArgs.INSTANCE))
                .registerPluginCommand();
    }
}
