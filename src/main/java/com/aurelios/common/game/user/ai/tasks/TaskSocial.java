package com.aurelios.common.game.user.ai.tasks;

import com.aurelios.common.game.user.ai.DailySchedule;

public class TaskSocial extends Task {

    /**
     * This task is to meet another NPC at some POI to form/grow a relationship
     */

    public TaskSocial(int howLongToAccomplish, DailySchedule owner) {
        super(howLongToAccomplish, owner, TaskType.SOCIAL);
    }
}
