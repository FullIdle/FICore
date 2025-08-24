package me.fullidle.ficore.ficore;

import net.md_5.bungee.api.plugin.Plugin;

public class Bungee extends Plugin {
    public static Bungee INSTANCE;

    public Bungee(){
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("§3 FICore Bungee loaded!");
    }
}
