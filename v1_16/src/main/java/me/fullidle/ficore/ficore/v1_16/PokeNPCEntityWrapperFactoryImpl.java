package me.fullidle.ficore.ficore.v1_16;

import com.pixelmonmod.pixelmon.entities.npcs.NPCEntity;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import lombok.val;
import me.fullidle.ficore.ficore.common.api.pokemon.wrapper.IPokeStorageWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapper;
import me.fullidle.ficore.ficore.common.api.pokemon.npc.PokeNPCEntityWrapperFactory;
import me.fullidle.ficore.ficore.common.bukkit.CraftWorld;
import me.fullidle.ficore.ficore.common.bukkit.entity.CraftEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class PokeNPCEntityWrapperFactoryImpl implements PokeNPCEntityWrapperFactory<NPCEntity> {
    public static final PokeNPCEntityWrapperFactoryImpl INSTANCE = new PokeNPCEntityWrapperFactoryImpl();

    @Override
    public PokeNPCEntityWrapper<NPCEntity> create(NPCEntity entity) {
        return new PokeNPCEntityWrapperImpl(entity);
    }

    @Override
    public boolean isPokeNPCEntity(Entity entity) {
        return CraftEntity.getHandle(entity) instanceof NPCEntity;
    }

    @Override
    public PokeNPCEntityWrapper<NPCEntity> asPokeNPCEntity(Entity entity) {
        if (isPokeNPCEntity(entity)) return new PokeNPCEntityWrapperImpl((NPCEntity) CraftEntity.getHandle(entity));
        throw new IllegalArgumentException("Entity are not Pokémon NPC entities");
    }

    @Override
    public PokeNPCEntityWrapper<NPCEntity> create(Location location) {
        val world = (ServerWorld) CraftWorld.getHandle(location.getWorld());
        val trainer = new NPCTrainer(world);
        trainer.setPos(location.getX(), location.getY(), location.getZ());
        world.addFreshEntity(trainer);
        return create(trainer);
    }

    public static class PokeNPCEntityWrapperImpl extends PokeNPCEntityWrapper<NPCEntity> {
        public PokeNPCEntityWrapperImpl(NPCEntity wrapped) {
            super(wrapped);
        }

        @Override
        public IPokeStorageWrapper<?> getStorage() {
            if (!(getOriginal() instanceof NPCTrainer)) return null;
            return new PokeStorageManager.PokeStorageWrapper(((NPCTrainer) getOriginal()).getPokemonStorage());
        }

        @Override
        public void setTexture(String texture) {
            getOriginal().setCustomSteveTexture(texture);
        }

        @Override
        public Class<NPCEntity> getType() {
            return NPCEntity.class;
        }
    }
}
