package com.aurelios.game.user.ai.nms.tasks;

import com.aurelios.game.user.ai.NPC;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

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
    }

    //This determines whether this task can be interrupted by a lower priority task
    @Override
    public boolean isInterruptible() {
        return false;
    }
}
