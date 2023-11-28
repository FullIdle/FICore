package me.fullidle.ficore.ficore.common.api.util;

import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;

public class LangMsgUtil {
    /**
     * @param util 一个带有基本类型变量的对象
     * @param section 节点
     * @apiNote 自动将对象中所有变量进行设置,所有值都来自与section内同变量名的节点值
     */
    @SneakyThrows
    public static void langMsgUtil(Object util, ConfigurationSection section){
        for (Field field : util.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            field.set(util, section.get(field.getName()));
        }
    }
}
