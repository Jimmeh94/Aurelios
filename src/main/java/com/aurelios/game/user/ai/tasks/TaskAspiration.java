package com.aurelios.game.user.ai.tasks;

import com.aurelios.game.user.ai.DailySchedule;

public class TaskAspiration extends Task {

    public TaskAspiration(int howLongToAccomplish, DailySchedule owner) {
        super(howLongToAccomplish, owner, TaskType.ASPIRATION);
    }
}
