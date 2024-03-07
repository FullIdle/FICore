package me.fullidle.ficore.ficore.common;

import me.fullidle.ficore.ficore.common.api.util.LangMsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class SomeMethod {
    /**
     * @return 获取minecraft的版本
     */
    public static String getMinecraftVersion(){
        return Bukkit.getServer().getVersion().split("MC: ")[1].replace(")", "");
    }

    /**
     * @param msg 原信息
     * @return 替换颜色代码后的信息
     */
    public static String getColorMsg(String msg) {
        return msg.replace("§", "&");
    }
    public static void setLangMsgUtil(Object util, ConfigurationSection section,String spiltSign){
        LangMsgUtil.langMsgUtil(util,section,spiltSign);
    }
}
