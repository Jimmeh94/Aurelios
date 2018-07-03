package com.aurelios.server.game.user.ai.tasks;

import com.aurelios.server.game.user.ai.DailySchedule;

public class TaskAspiration extends Task {

    public TaskAspiration(int howLongToAccomplish, DailySchedule owner) {
        super(howLongToAccomplish, owner, TaskType.ASPIRATION);
    }

    @Override
    protected void perform() {

    }
}
