package com.aurelios.server.util.misc;

import net.minecraft.entity.EntityCreature;

public class EntityUtils {

    public static void clearTasks(EntityCreature entity){
            entity.tasks.taskEntries.clear();
            entity.targetTasks.taskEntries.clear();
    }


}
