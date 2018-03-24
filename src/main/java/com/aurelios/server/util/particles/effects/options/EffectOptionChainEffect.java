package com.aurelios.server.util.particles.effects.options;

import com.aurelios.server.util.particles.ParticleData;
import com.aurelios.server.util.particles.ParticlePlayer;
import com.flowpowered.math.vector.Vector3d;

import static com.aurelios.server.util.particles.effects.options.EffectOptionChainEffect.Where.*;

/**
 * This is to chain simple extra particles to an AbstractEffect, using the parent AbstractEffect's displayAt location
 */
public class EffectOptionChainEffect extends EffectOption<ParticleData> {

    private Where where;

    public EffectOptionChainEffect(ParticleData value, Where where) {
        super(value, EffectOptionTypes.CHAIN_EFFECT);

        this.where = where;
    }

    public void playEffect(Vector3d displayAt){
        getValue().setDisplayAt(displayAt);
        ParticlePlayer.display(getValue());
    }

    public boolean shouldPlayHere(int currentIndex, int length){
        if(where == ALL)
            return true;

        if(where == BEGINNING && currentIndex == 0)
            return true;
        else if(where == MIDDLE){
            return length / 2 == currentIndex;
        } else if(where == END){
            return currentIndex == length - 1;
        }

        return false;
    }

    /**
     * Where should these extra particles be played on the parent AbstractEffect?
     */
    public enum Where{
        END,
        BEGINNING,
        MIDDLE,
        ALL
    }
}
