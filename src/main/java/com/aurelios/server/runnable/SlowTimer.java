package com.aurelios.server.runnable;

import com.aurelios.Aurelios;
import com.aurelios.server.managers.Managers;

public class SlowTimer extends AbstractTimer {

    public SlowTimer(long interval) {
        super(interval);

        start();
    }

    @Override
    protected void runTask() {
        Managers.POLL.tick();
        Managers.BLOCK.tick();
        Aurelios.INSTANCE.getCalendar().tick();
        Managers.AI.tick();
        Managers.USER.slowTick();

        //TODO REMOVE THIS
        Managers.NODES.tick();
    }
}
