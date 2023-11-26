package me.fullidle.ficore.ficore.common;

import org.bukkit.Bukkit;

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
}
