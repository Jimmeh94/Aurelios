package com.aurelios.server.game.user.ai.nms.entity;

import com.aurelios.server.game.user.ai.NPC;
import com.aurelios.server.game.user.ai.nms.tasks.EntityAIMoveTo;
import com.aurelios.server.util.misc.EntityUtils;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

public class EntityNPC extends EntityZombie{//extends EntityCreature{

    private NPC owner;

    public EntityNPC(World worldIn) {
        super(worldIn);

        EntityUtils.clearTasks(this);
    }

    public EntityNPC(World world, NPC owner){
        super(world);

        this.owner = owner;

        getNavigator().setSpeed(1.0);
        EntityUtils.clearTasks(this);
        tasks.addTask(0, new EntityAIMoveTo(owner));
    }

    @Override
    public boolean isAIDisabled(){
        return false;
    }
}
