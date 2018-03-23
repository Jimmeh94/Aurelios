package com.aurelios.common.util.particles.effects.options;

import com.aurelios.common.game.ability.movement.Tracking;
import com.aurelios.common.util.particles.ParticleData;

/**
 * This moves the ability forward one step at a time in an animation
 */
public class EffectOptionIterate extends EffectOption {

    public EffectOptionIterate() {
        super(null, EffectOptionTypes.ITERATE);
    }

    public int getIterator(ParticleData effectData){
        Tracking tracking = effectData.getAbility().get().getTargeting().getTracking();
        return (int) (tracking.getPreviousCenters().size() *
                effectData.getAbility().get().getTargeting().getMovementScale());
    }
}
