package com.aurelios.common.game.user.ai.tasks;

import com.aurelios.common.game.user.ai.DailySchedule;

public class TaskWork extends Task {

    public TaskWork(int howLongToAccomplish, DailySchedule owner) {
        super(howLongToAccomplish, owner, TaskType.WORK);
    }
}
