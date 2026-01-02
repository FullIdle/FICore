package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import me.fullidle.ficore.ficore.common.api.data.FIData;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokemonWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.lang.reflect.Method;

public class VPokeEntityWrapperFactory implements PokeEntityWrapperFactory<EntityPixelmon> {
    public static VPokeEntityWrapperFactory INSTANCE = new VPokeEntityWrapperFactory();

    @Override
    public PokeEntityWrapper<EntityPixelmon> create(EntityPixelmon entity) {
        return new VPokeEntityWrapper(entity);
    }

    @Override
    public boolean isPokeEntity(Entity entity) {
        return CraftEntity.getHandle(entity) instanceof EntityPixelmon;
    }

    @Override
    public PokeEntityWrapper<EntityPixelmon> asPokeEntity(Entity entity) {
        if (!isPokeEntity(entity)) throw new IllegalArgumentException("Entity is not a PokeEntity");
        return create(((EntityPixelmon) CraftEntity.getHandle(entity)));
    }

    public static class VPokeEntityWrapper extends PokeEntityWrapper<EntityPixelmon> {

        // ====== 反射缓存（避免每次调用都查找 Method） ======
        private static final Method CAN_DESPAWN_METHOD = resolveCanDespawnMethod();

        private static Method resolveCanDespawnMethod() {
            // 1.12.x：方法一般叫 canDespawn()Z（受保护）
            // 有些环境可能是 SRG 名 func_70692_ba()Z
            final String[] candidates = new String[] {
                    "canDespawn",
                    "func_70692_ba"
            };

            Class<?> c = net.minecraft.entity.passive.EntityAnimal.class;
            for (String name : candidates) {
                try {
                    Method m = c.getDeclaredMethod(name);
                    m.setAccessible(true);
                    return m;
                } catch (NoSuchMethodException ignored) {
                }
            }

            // 理论上也可能被放在父类里（保险起见向上找）
            Class<?> p = c.getSuperclass();
            while (p != null && p != Object.class) {
                for (String name : candidates) {
                    try {
                        Method m = p.getDeclaredMethod(name);
                        m.setAccessible(true);
                        return m;
                    } catch (NoSuchMethodException ignored) {
                    }
                }
                p = p.getSuperclass();
            }

            return null; // 找不到就 null，调用处兜底
        }

        public VPokeEntityWrapper(EntityPixelmon wrapped) {
            super(wrapped);
        }

        @Override
        public boolean isBoss() {
            return this.getOriginal().isBossPokemon();
        }

        @Override
        public IPokemonWrapper<?> getPokemon() {
            return ((PokemonWrapperFactory) FIData.V1_version.getPokemonWrapperFactory())
                    .create(this.getOriginal().getPokemonData());
        }

        @Override
        public boolean canDespawn() {
            // 反射调用父类 protected 方法
            Method m = CAN_DESPAWN_METHOD;
            if (m == null) {
                // 找不到方法时的安全兜底：一般可认为不允许自然消失（避免刷怪场景被意外清掉）
                return false;
            }
            try {
                Object r = m.invoke(this.getOriginal());
                return (r instanceof Boolean) ? (Boolean) r : false;
            } catch (Throwable t) {
                // 反射失败兜底：同上，返回 false 更安全
                return false;
            }
        }

        @Override
        public boolean inBattle() {
            return this.getOriginal().battleController != null;
        }
    }
}
