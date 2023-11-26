package me.fullidle.ficore.ficore.common.api.util;

import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;

public class LangMsgUtil {
    @SneakyThrows
    public LangMsgUtil(LangMsgUtil util, ConfigurationSection section){
        for (Field field : util.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            field.set(util, section.get(field.getName()));
        }
    }
}
