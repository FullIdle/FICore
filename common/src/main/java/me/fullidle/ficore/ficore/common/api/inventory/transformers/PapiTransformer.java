package me.fullidle.ficore.ficore.common.api.inventory.transformers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fullidle.ficore.ficore.common.api.inventory.InvButton;
import me.fullidle.ficore.ficore.common.api.inventory.InvConfig;
import me.fullidle.ficore.ficore.common.api.inventory.actions.InvAction;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

/**
 * 不用时FICore是不需要PlaceholderAPI的
 * <p>
 * 使用方法
 *     <pre>
 *         new InvConfig.Builder(title).transformer(PapiTransformer.INSTANCE)...
 *     </pre>
 * </p>
 */
public class PapiTransformer implements InvTransformer {
    public static PapiTransformer INSTANCE = new PapiTransformer();

    @Override
    public String action(InventoryClickEvent event, InvButton button, InvAction action, String needTransformed) {
        if (!(event.getWhoClicked() instanceof OfflinePlayer)) return needTransformed;
        return papi(needTransformed, ((OfflinePlayer) event.getWhoClicked()));
    }

    @Override
    public String title(@Nullable InvConfig config, @Nullable InventoryHolder invHolder, @Nullable OfflinePlayer papiTarget, String title) {
        return papi(title, papiTarget);
    }

    @Override
    public String buttonStr(InvConfig config, InventoryHolder holder, InvButton invButton, OfflinePlayer papiTarget, String needTransformed) {
        return papi(needTransformed, papiTarget);
    }

    protected String papi(String str, OfflinePlayer target) {
        return PlaceholderAPI.setPlaceholders(target, str);
    }
}
