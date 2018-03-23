package com.aurelios.common.managers;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.Location;

public class BlockManager extends Manager<BlockManager.TemporaryBlock> {

    public void tick(){
        for(TemporaryBlock block: objects){
            if(block.shouldRecover()){
                block.replace();
                objects.remove(block);
            }
        }
    }

    public static class TemporaryBlock{

        private BlockType type;
        private Location location;
        private Long whenStarted;
        private int replaceIn;

        public TemporaryBlock(Vector3d position, int replaceIn) {
            location = Sponge.getServer().getWorld("world").get().getLocation(position);
            type = location.getBlockType();
            whenStarted = System.currentTimeMillis();
            this.replaceIn = replaceIn;
        }

        public boolean shouldRecover(){
            return ((System.currentTimeMillis() - whenStarted)/1000) >= replaceIn;
        }

        public void replace(){
            location.setBlockType(type);
        }
    }
}
