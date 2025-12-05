package me.fullidle.ficore.ficore.common.api.inventory;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

/**
 * 容器配置将该配置对象用来构造的 {@link InvHolder} 所得到的 {@link InventoryHolder} 对象
 * 调用 {@link InventoryHolder#getInventory()} 得到的容器给玩家打开，可以正常使用实例化后的配置行为
 */
public class InvConfig {
    private final String title;
    // layout[y][x]
    private final InvButton[][] layout;
    //标题，物品等在初始化的时候会调用该方法进行处理后赋予
    private final BiFunction<OfflinePlayer, String, String> papiFun;

    public InvConfig(String title, InvButton[][] layout, BiFunction<OfflinePlayer, String, String> papiFun) {
        this.title = title;
        this.layout = layout;
        this.papiFun = papiFun == null ? (p, s) -> s : papiFun;
    }

    public InvConfig(String title, InvButton[][] layout) {
        this(title, layout, null);
    }

    public Inventory createInv(InventoryHolder holder, @Nullable OfflinePlayer papiTarget) {
        Inventory inv = Bukkit.createInventory(holder, layout.length * 9, papi(title, papiTarget));
        for (int y = 0; y < layout.length; y++)
            for (int x = 0; x < layout[y].length; x++) {
                val invButton = layout[y][x];
                if (invButton == null) continue;
                inv.setItem(y * 9 + x, invButton.getIcon(papiFun, papiTarget));
            }
        return inv;
    }

    private String papi(String title, OfflinePlayer papiTarget) {
        return papiFun.apply(papiTarget, title);
    }

    public InvButton getButton(int slot) {
        return layout[slot / 9][slot % 9];
    }

    public InvButton getButton(int x, int y) {
        return layout[y][x];
    }

    public static InvConfig of(String text, List<String> layout, Map<Character, InvButton> buttonMap) {
        return new InvConfig(text, combined(layout, buttonMap));
    }

    public static InvConfig of(String text, List<String> layout, Map<Character, InvButton> buttonMap, BiFunction<OfflinePlayer, String, String> papiFun) {
        return new InvConfig(text, combined(layout, buttonMap), papiFun);
    }

    public static InvButton[][] combined(List<String> layout, Map<Character, InvButton> buttonMap) {
        InvButton[][] buttons = new InvButton[layout.size()][9];
        for (int y = 0; y < Math.min(layout.size(), 6); y++)
            for (int x = 0; x < Math.min(layout.get(y).length(), 9); x++)
                buttons[y][x] = buttonMap.get(layout.get(y).charAt(x));
        return buttons;
    }

    /**
     * 通过解析yaml得InvConfig
     * <p>
     * yaml格式:
     * <pre>
     *         title: "标题"
     *         layout:
     *           - '000000000'
     *           - '0       0'
     *           - '000000000'
     *          buttons:
     *            '0':
     *              material: STONE
     *              name: "§a按钮"
     *              lore:
     *                - '§alore'
     *              actions:
     *                left:
     *                  - 'command: /say Hello world'
     *     </pre>
     * 其中 actions下的动作类型更具 {@link org.bukkit.event.inventory.ClickType} 美剧的名字来定的(可以小写)
     * <p>
     * 可用的动作列表则是更具注册在工厂 {@link me.fullidle.ficore.ficore.common.api.inventory.actions.InvActionFactories} 来获取的
     * </p>
     * </p>
     *
     * @see org.bukkit.event.inventory.ClickType
     * @see me.fullidle.ficore.ficore.common.api.inventory.actions.InvActionFactories
     */
    public static InvConfig parseYaml(@NotNull ConfigurationSection config) {
        ConfigurationSection section = config.getConfigurationSection("buttons");
        HashMap<Character, InvButton> map = new HashMap<>();
        if (section != null) for (String s : section.getKeys(false)) {
            if (s.length() != 1) throw new IllegalArgumentException("Button name must be a single character");
            map.put(s.charAt(0), InvButton.parseYaml(Objects.requireNonNull(section.getConfigurationSection(s))));
        }
        return of(config.getString("title"), config.getStringList("layout"), map);
    }


    public static class Builder {
        private final String title;
        private final List<String> layout = new ArrayList<>();
        private final Map<Character, InvButton> buttons = new HashMap<>();
        private BiFunction<OfflinePlayer, String, String> papiFun;

        public Builder(String title) {
            this.title = title;
        }

        public Builder papiFun(BiFunction<OfflinePlayer, String, String> papiFun) {
            this.papiFun = papiFun;
            return this;
        }

        public BiFunction<OfflinePlayer, String, String> papiFun() {
            return papiFun;
        }

        public List<String> layout() {
            return new ArrayList<>(this.layout);
        }

        public Builder layout(List<String> layout) {
            this.layout.clear();
            this.layout.addAll(layout);
            return this;
        }

        public Map<Character, InvButton> buttons() {
            return new HashMap<>(this.buttons);
        }

        public Builder button(Character identity, InvButton button) {
            this.buttons.put(identity, button);
            return this;
        }

        public InvButton button(Character identity) {
            return this.buttons.get(identity);
        }

        public String title() {
            return title;
        }

        public InvConfig build() {
            return InvConfig.of(this.title, this.layout, this.buttons, this.papiFun);
        }
    }
}
