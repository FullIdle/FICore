package me.fullidle.ficore.ficore.common.api.inventory;

import me.fullidle.ficore.ficore.common.api.inventory.transformers.PapiTransformer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InvHolderUseExample {
    public static void customInv() {
        String title = "example";
        InvConfig config = new InvConfig.Builder(title)
                .transformer(PapiTransformer.INSTANCE)
                //预先好布局
                .layout(Arrays.asList(
                        "000000000",
                        "0 A  B  0",
                        "000000000"
                ))
                .button('0', new InvButton.Builder()
                        .displayMaterial(Material.STONE)
                        .displayName("我是边界") //可以用 Component 类型
                        .displayLore("看我干什么!")
                        //这里只对左键起作用
                        .actions(ClickType.LEFT, (clickEvent, button, transformer) -> {
                            clickEvent.getWhoClicked().sendMessage("你左键我干什么!");
                        })
                        //这一个是所有动作的按钮，包裹了各种点击类型 actions 方法只增加，不覆盖!
                        .actions((clickEvent, button, transformer) -> {
                            clickEvent.getWhoClicked().sendMessage("你点我干什么!");
                        })
                        //当玩家点击0位置上的这些按钮 会提示 '你点我干什么!' 左键时会有两个提示，第一个是 '你左键我干什么!'
                        // 第二个是 '你点我干什么!'
                        .build())
                .button('A', new InvButton.Builder()
                        .displayMaterial(Material.DIAMOND)
                        .displayName("我是钻石")
                        .displayLore("我是什么?", "变量测试%player_name% <<")
                        .actions((clickEvent, button, transformer) ->
                                clickEvent.getWhoClicked().sendMessage("你点了钻石!")
                        ).build())
                .button('B', new InvButton.Builder()
                        .displayMaterial(Material.GOLD_INGOT)
                        .displayName("我是金子")
                        .displayLore("我是什么?")
                        .actions((clickEvent, button,transformer) ->
                                clickEvent.getWhoClicked().sendMessage("你点了金子!")
                        ).build())
                .build();
        //之后给玩家打开这个inv就好了
        for (Player player : Bukkit.getOnlinePlayers()) {
            // papiTarget 是变量提供者，这会将标题，物品名，物品lore都进行 papi变量处理(项目暂没有依赖,实际不起作用) 不需要就传null
            InvHolder invHolder = new InvHolder(config, player);
            Inventory inv = invHolder.getInventory();
            player.openInventory(inv);
        }
    }
}
