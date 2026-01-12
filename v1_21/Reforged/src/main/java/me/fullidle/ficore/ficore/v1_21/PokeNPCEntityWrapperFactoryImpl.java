package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.api.util.ResourceWithFallback;
import com.pixelmonmod.pixelmon.entities.npcs.NPC;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeNPCEntityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.PokeNPCEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.entity.Entity;

public class PokeNPCEntityWrapperFactoryImpl implements PokeNPCEntityWrapperFactory<NPC> {
    public static final PokeNPCEntityWrapperFactoryImpl INSTANCE = new PokeNPCEntityWrapperFactoryImpl();

    @Override
    public PokeNPCEntityWrapper<NPC> create(NPC entity) {
        return new PokeNPCEntityWrapperImpl(entity);
    }

    @Override
    public boolean isPokeNPCEntity(Entity entity) {
        return CraftEntity.getHandle(entity) instanceof NPC;
    }

    @Override
    public PokeNPCEntityWrapper<NPC> asPokeNPCEntity(Entity entity) {
        if (isPokeNPCEntity(entity)) return new PokeNPCEntityWrapperImpl((NPC) CraftEntity.getHandle(entity));
        throw new IllegalArgumentException("Entity are not Pokémon NPC entities");
    }

    public static class PokeNPCEntityWrapperImpl extends PokeNPCEntityWrapper<NPC> {
        public PokeNPCEntityWrapperImpl(NPC wrapped) {
            super(wrapped);
        }

        @Override
        public IPokeStorageWrapper<?> getStorage() {
            if (getOriginal().getParty() == null) return null;
            return new PokeStorageManager.PokeStorageWrapper(getOriginal().getParty());
        }

        @Override
        public void setTexture(String texture) {
            getOriginal().displayIcon(ResourceWithFallback.from(ResourceLocation.tryParse(texture)));
        }

        @Override
        public Class<NPC> getType() {
            return NPC.class;
        }
    }
}
