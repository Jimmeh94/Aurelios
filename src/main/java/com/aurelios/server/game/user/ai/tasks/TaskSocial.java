package com.aurelios.server.game.user.ai.tasks;

import com.aurelios.server.game.user.ai.DailySchedule;
import com.aurelios.server.managers.Managers;

public class TaskSocial extends Task {
    /**
     * This task is to meet another NPC at some POI to form/grow a relationship
     */

    public TaskSocial(int howLongToAccomplish, DailySchedule owner) {
        super(howLongToAccomplish, owner, TaskType.SOCIAL);
    }

    @Override
    public void perform() {
        Managers.AI.simulateNPCRelationship(getOwner().getOwner());
    }
}
