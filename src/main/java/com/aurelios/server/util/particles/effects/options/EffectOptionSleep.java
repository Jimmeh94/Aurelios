package com.aurelios.server.util.particles.effects.options;

public class EffectOptionSleep extends EffectOption<Integer> {

    private int sleptFor = 0;

    /**
     * Sleep is the amount of iterations to sleep between playing the particles
     * @param sleep
     */
    public EffectOptionSleep(Integer sleep) {
        super(sleep, EffectOptionTypes.SLEEP);
    }

    public boolean isSleeping(){
        if(sleptFor < getValue()){
            sleptFor++;
            return true;
        } else {
            sleptFor = 0;
            return false;
        }
    }
}
