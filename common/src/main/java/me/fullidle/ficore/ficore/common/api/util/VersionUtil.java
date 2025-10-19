package me.fullidle.ficore.ficore.common.api.util;

import lombok.Getter;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class VersionUtil {
    public final static String CRAFT_BUKKIT_HEAD_TAG = "org.bukkit.craftbukkit";

    /**
     * NMS 版本
     * 在 Your 核心上 这将会是一个空字符
     */
    @Getter
    private final static String nmsVersion;

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

    public static void setLangMsgUtil(Object util, ConfigurationSection section, String spiltSign){
        LangMsgUtil.langMsgUtil(util,section,spiltSign);
    }

    static {
        val info = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        val ver = info.length == 3 ? "" : info[3];
        nmsVersion = (ver.isEmpty() || (ver.startsWith("v") && ver.matches(".*\\d.*") && ver.contains("_"))) ? ver : null;
        if (nmsVersion == null) {
            FIData.plugin.getLogger().warning("当前版本涉及NMS的功能无法正常使用! >> " + ver);
        }
    }
}
