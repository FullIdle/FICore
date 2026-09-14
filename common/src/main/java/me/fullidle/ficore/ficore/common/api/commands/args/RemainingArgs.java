package me.fullidle.ficore.ficore.common.api.commands.args;

import me.fullidle.ficore.ficore.common.api.commands.Context;

/**
 * 消耗当前位置及后续所有参数，必须作为命令的最后一个参数节点。
 * 和普通参数一样，至少需要传入一个参数。
 */
public interface RemainingArgs extends Args<String[]> {
    @Override
    default String[] get(Context context, String name) {
        String[] values = context.remainingArgs.get(name);
        return values == null ? null : parse(context, values.clone());
    }

    @Override
    default String[] parse(Context context, String arg) {
        return parse(context, new String[]{arg});
    }

    String[] parse(Context context, String[] args);
}
