package me.fullidle.ficore.ficore.common.api.commands;

import me.fullidle.ficore.ficore.common.api.commands.args.types.StringArgs;
import me.fullidle.ficore.ficore.common.api.commands.args.types.StringArrayArgs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static me.fullidle.ficore.ficore.common.api.commands.CommandBuilder.args;
import static me.fullidle.ficore.ficore.common.api.commands.CommandBuilder.builder;
import static org.junit.Assert.*;

public class StringArrayArgsTest {
    private final List<String> messages = new ArrayList<>();
    private boolean permitted = true;
    private final CommandSender sender = (CommandSender) Proxy.newProxyInstance(
            CommandSender.class.getClassLoader(), new Class<?>[]{CommandSender.class},
            (proxy, method, arguments) -> {
                if (method.getName().equals("hasPermission")) return permitted;
                if (method.getName().equals("sendMessage")) {
                    if (arguments[0] instanceof String) messages.add((String) arguments[0]);
                    else messages.addAll(Arrays.asList((String[]) arguments[0]));
                }
                return null;
            });
    private final Command command = new Command("ficore") {
        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            return true;
        }
    };

    private void execute(TabExecutor executor, String... values) {
        assertTrue(executor.onCommand(sender, command, "ficore", values));
    }

    @Test
    public void collectsOneOrManyArgumentsWithoutChangingBoundaries() {
        AtomicReference<Context> captured = new AtomicReference<>();
        TabExecutor executor = builder("ficore").then(builder("test")
                .then(args("values", StringArrayArgs.NO_COMPLETE).exec(captured::set))).build();

        execute(executor, "test", "one");
        assertArrayEquals(new String[]{"one"}, StringArrayArgs.NO_COMPLETE.get(captured.get(), "values"));

        String[] input = {"test", "one", "two words", "", "中文"};
        execute(executor, input);
        Context context = captured.get();
        assertArrayEquals(new String[]{"one", "two words", "", "中文"},
                StringArrayArgs.NO_COMPLETE.get(context, "values"));
        assertSame(input, context.rawArgs);
        input[1] = "changed";
        String[] values = StringArrayArgs.NO_COMPLETE.get(context, "values");
        assertEquals("one", values[0]);
        values[0] = "also changed";
        assertEquals("one", StringArrayArgs.NO_COMPLETE.get(context, "values")[0]);
        assertNull(StringArrayArgs.NO_COMPLETE.get(context, "missing"));
        assertTrue(messages.isEmpty());
    }

    @Test
    public void preservesFixedArgumentsBeforeTheArray() {
        AtomicReference<Context> captured = new AtomicReference<>();
        TabExecutor executor = builder("ficore").then(builder("test")
                .then(args("target", StringArgs.NO_COMPLETE)
                        .then(args("values", StringArrayArgs.NO_COMPLETE).exec(captured::set)))).build();
        execute(executor, "test", "player", "a", "b", "c");
        assertEquals("player", StringArgs.NO_COMPLETE.get(captured.get(), "target"));
        assertArrayEquals(new String[]{"a", "b", "c"},
                StringArrayArgs.NO_COMPLETE.get(captured.get(), "values"));
    }

    @Test
    public void ordinaryArgumentsStillConsumeExactlyOneToken() {
        AtomicReference<Context> captured = new AtomicReference<>();
        TabExecutor executor = builder("ficore")
                .then(args("value", StringArgs.NO_COMPLETE).exec(captured::set)).build();
        execute(executor, "one");
        assertEquals("one", StringArgs.NO_COMPLETE.get(captured.get(), "value"));
        captured.set(null);
        execute(executor, "one", "two");
        assertNull(captured.get());
        assertTrue(messages.get(0).contains("Wrong format"));
    }

    @Test
    public void missingArrayArgumentsUseTheParentExecutor() {
        AtomicReference<Context> captured = new AtomicReference<>();
        TabExecutor executor = builder("ficore").then(builder("test").exec(captured::set)
                .then(args("values", StringArrayArgs.NO_COMPLETE)
                        .exec(context -> fail("Array node requires an argument")))).build();
        execute(executor, "test");
        assertNotNull(captured.get());
        assertNull(StringArrayArgs.NO_COMPLETE.get(captured.get(), "values"));
    }

    @Test
    public void checksPermissionAfterCollectingAllArguments() {
        AtomicReference<Context> captured = new AtomicReference<>();
        TabExecutor executor = builder("ficore").then(builder("test")
                .then(args("values", StringArrayArgs.NO_COMPLETE)
                        .permission(context -> {
                            assertArrayEquals(new String[]{"a", "b"},
                                    StringArrayArgs.NO_COMPLETE.get(context, "values"));
                            return "ficore.test";
                        }).exec(captured::set))).build();
        permitted = false;
        execute(executor, "test", "a", "b");
        assertNull(captured.get());
        assertTrue(messages.get(0).contains("permission"));
        permitted = true;
        execute(executor, "test", "a", "b");
        assertNotNull(captured.get());
    }

    @Test
    public void completesEveryArrayPosition() {
        StringArrayArgs values = new StringArrayArgs(Arrays.asList("apple", "banana", "apricot"));
        TabExecutor executor = builder("ficore").then(builder("test")
                .then(args("values", values))).build();
        for (String[] input : new String[][]{
                {"test", "ap"}, {"test", "anything", "ap"}, {"test", "anything", "else", "ap"}}) {
            assertEquals(Arrays.asList("apple", "apricot"),
                    executor.onTabComplete(sender, command, "ficore", input));
        }
        assertEquals(values.prompts(), executor.onTabComplete(sender, command, "ficore",
                new String[]{"test", "anything", ""}));
        TabExecutor noPrompts = builder("ficore").then(builder("test")
                .then(args("values", StringArrayArgs.NO_COMPLETE))).build();
        assertEquals(Collections.emptyList(), noPrompts.onTabComplete(sender, command, "ficore",
                new String[]{"test", "a", "b", ""}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsChildrenAfterRemainingArguments() {
        builder("ficore").then(args("values", StringArrayArgs.NO_COMPLETE)
                .then(builder("unreachable"))).build();
    }
}
