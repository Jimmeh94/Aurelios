package com.aurelios.server.game.user.ai.tasks;

import com.aurelios.server.game.user.ai.DailySchedule;

public class TaskLazy extends Task {

    /**
     * This task is for walking around or hanging out at a POI (home, tavern, park, etc)
     */

    public TaskLazy(int howLongToAccomplish, DailySchedule owner) {
        super(howLongToAccomplish, owner, TaskType.LAZY);
    }
}
