package com.aurelios.game.user.ai.nms.entity;

import com.aurelios.game.user.ai.NPC;
import com.aurelios.game.user.ai.nms.tasks.EntityAIMoveTo;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public class EntityNPC extends EntityCreature implements EntityAI{

    private NPC owner;

    public EntityNPC(World worldIn) {
        super(worldIn);
    }

    @Override
    public void init(NPC owner){
        this.owner = owner;

        getNavigator().setSpeed(1.0);
        clearTasks();
        tasks.addTask(0, new EntityAIMoveTo(owner));
    }

    @Override
    public boolean isAIDisabled(){
        return false;
    }

    @Override
    public void clearTasks() {
        tasks.taskEntries.clear();
        targetTasks.taskEntries.clear();
    }
}
