package com.aurelios.server.runnable;

public class Timers {
    
    public final GameTimer gameTimer;
    public final SlowTimer slowTimer;
    public final AbilityTimer abilityTimer;

    public Timers() {
        gameTimer = new GameTimer(5L);
        slowTimer = new SlowTimer(20L);
        abilityTimer = new AbilityTimer(1L);
    }
}
