package me.fullidle.ficore.ficore.common;

import org.bukkit.Bukkit;

public class SomeMethod {
    public static String getMinecraftVersion(){
        return Bukkit.getServer().getVersion().split("MC: ")[1].replace(")", "");
    }
}
