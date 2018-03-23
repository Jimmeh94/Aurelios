package com.aurelios.common.runnable;

import com.aurelios.common.managers.Managers;

/**
 * This is the main game runnable
 */
public class GameTimer extends AbstractTimer {

    public GameTimer(long interval) {
        super(interval);

        start();
    }

    @Override
    protected void runTask() {
        //Update necessary player information
        Managers.USER.tick();
    }
}
