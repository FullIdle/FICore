package me.fullidle.ficore.ficore.common.api.commands.args;

import lombok.val;
import me.fullidle.ficore.ficore.common.api.commands.Context;

import java.util.List;

public interface Args<T> {
    /**
     * 在执行的时候用
     */
    default T get(Context context, String name) {
        val value = context.args.get(name);
        if (value == null) return null;
        return parse(context, value);
    }

    /**
     * 在解析的时候用
     * 同时也是判断是否走的参数路线
     *
     * @return 为NULL时则跳过这一个参数的路线
     */
    T parse(Context tempContext, String arg);

    /**
     * 提供的tab补全提示
     */
    List<String> prompts();
}
