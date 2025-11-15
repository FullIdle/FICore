package me.fullidle.ficore.ficore.v1_21;

import com.mohistmc.youer.Youer;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import lombok.SneakyThrows;
import lombok.val;
import me.fullidle.ficore.ficore.common.V1_version;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.battle.IBattleManager;
import me.fullidle.ficore.ficore.common.api.pokemon.breeds.IBreedLogic;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageManager;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.ISpeciesWrapperFactory;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
import net.neoforged.bus.BusBuilderImpl;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.BusBuilder;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforgespi.language.IModInfo;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class V1_21 extends V1_version {
    @SneakyThrows
    public V1_21() {
        FIData.V1_version = this;
        for (ModContainer mod : ModList.get().getSortedMods()) {
            System.out.println(mod.getModId());
            val eventBus = mod.getEventBus();
            if (eventBus != null) {
                try {
                    eventBus.register(this);
                    System.out.println("注册成功");
                    continue;
                } catch (Exception e) {
                    System.out.println("注册失败");
                    continue;
                }
            }
            System.out.println("注册失败");
        }
        Youer
    }

    @SubscribeEvent
    public void test(BattleStartedEvent event) {
        throw new RuntimeException("test");
    }

    @Override
    public String getVersion() {
        return "1.21.1";
    }

    @SneakyThrows
    @Override
    public void registerForgeEvent() {
        FIData.plugin.getLogger().warning("1.21.1的ForgeEvent只支持neoforge的!");
    }

    @Override
    public void register(Plugin plugin, Object bus, Object target) {
        //TODO
    }

    @Override
    public void unregisterAllListener(Plugin plugin) {
        //TODO
    }

    @Override
    public ISpeciesWrapperFactory<?> getSpeciesWrapperFactory() {
        return SpeciesWrapperFactory.INSTANCE;
    }

    @Override
    public IPokemonWrapperFactory<?> getPokemonWrapperFactory() {
        return PokemonWrapperFactory.INSTANCE;
    }

    @Override
    public IBreedLogic getBreedLogic() {
        return BreedLogic.INSTANCE;
    }

    @Override
    public IBattleManager getBattleManager() {
        return BattleManager.INSTANCE;
    }

    @Override
    public IPokeStorageManager getPokeStorageManager() {
        return PokeStorageManager.INSTANCE;
    }

    @Override
    public PokeEntityWrapperFactory<?> getPokeEntityWrapperFactory() {
        return VPokeEntityWrapperFactory.INSTANCE;
    }
}
