package com.aurelios.server.game.user.ai.nms.tasks;

import com.aurelios.server.game.environment.nodes.shapes.ShapeCircle;
import com.aurelios.server.game.user.ai.NPC;
import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIMoveTo extends EntityAIBase {

    private NPC npc;

    public EntityAIMoveTo(NPC entity) {
        this.npc = entity;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(npc.getUser().getEntity() != null){
            if(npc.getDailySchedule().getCurrentTask() != null){
                if(npc.getUser().getCurrentArea() != npc.getDailySchedule().getCurrentTask().getLocation()){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void startExecuting() {
        //super.startExecuting();

        EntityCreature entityCreature = (EntityCreature) npc.getUser().getEntity();
        //set target location to move to
        Vector3d target = ((ShapeCircle)npc.getDailySchedule().getCurrentTask().getLocation().getShape()).getCenter();
        entityCreature.moveToBlockPosAndAngles(new BlockPos(target.getX(), target.getY(), target.getZ()), entityCreature.rotationYaw, entityCreature.rotationPitch);
    }

    //This determines whether this task can be interrupted by a lower priority task
    @Override
    public boolean isInterruptible() {
        return false;
    }
}
