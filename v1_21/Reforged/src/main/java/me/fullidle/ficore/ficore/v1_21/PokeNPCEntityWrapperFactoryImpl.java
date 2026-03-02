package me.fullidle.ficore.ficore.v1_21;

import com.pixelmonmod.pixelmon.api.util.ResourceWithFallback;
import com.pixelmonmod.pixelmon.entities.npcs.NPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCBuilder;
import com.pixelmonmod.pixelmon.init.registry.EntityRegistration;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.CraftWorld;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
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

    @Override
    public PokeNPCEntityWrapper<NPC> create(Location location) {
        val handle = (ServerLevel) CraftWorld.getHandle(location.getWorld());
        val npc = new NPC(EntityRegistration.NPC.get(), handle);
        handle.addFreshEntity(npc);
        return create(npc);
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
