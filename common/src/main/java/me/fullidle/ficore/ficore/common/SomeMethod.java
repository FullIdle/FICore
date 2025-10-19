package me.fullidle.ficore.ficore.common;

import me.fullidle.ficore.ficore.common.api.util.LangMsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * 将换成更有识别度的 {@link me.fullidle.ficore.ficore.common.api.util.VersionUtil}
 */
@Deprecated
public class SomeMethod {
    public static String nmsVersion;

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

    public static String getNmsVersion(){
        if (nmsVersion == null){
            String name = new ItemStack(Material.GRASS).getItemMeta().getClass().getName();
            nmsVersion = name.split("\\.")[3];
        }
        return nmsVersion;
    }
}
