package me.fullidle.ficore.ficore.common.api.data;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FIData {
    public static me.fullidle.ficore.ficore.common.V1_version V1_version;
    public static Map<Plugin,Map<Integer,ArrayList<Object>>> listenerList = new HashMap<>();
    public static Plugin plugin;
}
