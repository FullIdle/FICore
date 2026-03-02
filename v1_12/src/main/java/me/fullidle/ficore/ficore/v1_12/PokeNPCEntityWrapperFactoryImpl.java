package me.fullidle.ficore.ficore.v1_12;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.CraftWorld;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class PokeNPCEntityWrapperFactoryImpl implements PokeNPCEntityWrapperFactory<EntityNPC> {
    public static final PokeNPCEntityWrapperFactoryImpl INSTANCE = new PokeNPCEntityWrapperFactoryImpl();

    @Override
    public PokeNPCEntityWrapper<EntityNPC> create(EntityNPC entity) {
        return new PokeNPCEntityWrapperImpl(entity);
    }

    @Override
    public boolean isPokeNPCEntity(Entity entity) {
        return CraftEntity.getHandle(entity) instanceof EntityNPC;
    }

    @Override
    public PokeNPCEntityWrapper<EntityNPC> asPokeNPCEntity(Entity entity) {
        if (isPokeNPCEntity(entity)) return new PokeNPCEntityWrapperImpl((EntityNPC) CraftEntity.getHandle(entity));
        throw new IllegalArgumentException("Entity are not Pokémon NPC entities");
    }

    @Override
    public PokeNPCEntityWrapper<EntityNPC> create(Location location) {
        val world = (World) CraftWorld.getHandle(location.getWorld());
        val npc = new NPCTrainer(world);
        npc.setPosition(location.getX(), location.getY(), location.getZ());
        world.spawnEntity(npc);
        return create(npc);
    }

    public static class PokeNPCEntityWrapperImpl extends PokeNPCEntityWrapper<EntityNPC> {
        public PokeNPCEntityWrapperImpl(EntityNPC wrapped) {
            super(wrapped);
        }

        @Override
        public IPokeStorageWrapper<?> getStorage() {
            if (!(getOriginal() instanceof NPCTrainer)) return null;
            return new PokeStorageManager.PokeStorageWrapper(((NPCTrainer) getOriginal()).getPokemonStorage());
        }

        @Override
        public void setTexture(String texture) {
            this.getOriginal().setCustomSteveTexture(texture);
        }

        @Override
        public Class<EntityNPC> getType() {
            return EntityNPC.class;
        }
    }
}
