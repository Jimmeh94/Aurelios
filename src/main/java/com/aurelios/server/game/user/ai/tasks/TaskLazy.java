package com.aurelios.server.game.user.ai.tasks;

import com.aurelios.server.game.user.ai.DailySchedule;
import com.aurelios.server.managers.Managers;

public class TaskLazy extends Task {

    /**
     * This task is for walking around or hanging out at a POI (home, tavern, park, etc)
     */

    private boolean hasPerformed = false;

    public TaskLazy(int howLongToAccomplish, DailySchedule owner) {
        super(howLongToAccomplish, owner, TaskType.LAZY);
    }

    @Override
    public void perform() {
        if(!hasPerformed && Managers.AI.simulateNPCRelationship(getOwner().getOwner())) {
            hasPerformed = true;
        }
    }
}
