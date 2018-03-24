package com.aurelios.server.runnable;

import com.aurelios.server.managers.Managers;

public class AbilityTimer extends AbstractTimer {

    public AbilityTimer(long interval) {
        super(interval);

        start();
    }

    @Override
    protected void runTask() {
        //Update abilities
        Managers.ABILITY.update();
    }
}
