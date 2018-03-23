package com.aurelios.server.game.user.ai.tasks;

import com.aurelios.server.game.user.ai.DailySchedule;

public class TaskWork extends Task {

    public TaskWork(int howLongToAccomplish, DailySchedule owner) {
        super(howLongToAccomplish, owner, TaskType.WORK