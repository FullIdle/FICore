package me.fullidle.ficore.ficore.common.api.commands.args.types;

import me.fullidle.ficore.ficore.common.api.commands.Context;
import me.fullidle.ficore.ficore.common.api.commands.args.RemainingArgs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 将后续所有参数作为 String[] 获取，不进行拼接或拆分。
 */
public class StringArrayArgs implements RemainingArgs {
    public static final StringArrayArgs NO_COMPLETE = new StringArrayArgs(Collections.emptyList());

    private final List<String> prompts;

    public StringArrayArgs(Collection<String> prompts) {
        this.prompts = new ArrayList<>(prompts);
    }

    @Override
    public String[] parse(Context context, String[] args) {
        return args;
    }

    @Override
    public List<String> prompts() {
        return new ArrayList<>(this.prompts);
    }
}
