package com.aurelios.util.particles.effects.options;

import com.aurelios.game.ability.Ability;
import com.aurelios.game.ability.movement.Tracking;

/**
 * I believe this means the ability animation grows in length with the ability itself
 */
public class EffectOptionGrowWithAbility extends EffectOption<Ability> {

    public EffectOptionGrowWithAbility(Ability value) {
        super(value, EffectOptionTypes.GROW_WITH_ABILLITY);
    }

    public int getLength(){
        Tracking tracking;
        try {
            tracking = getValue().getTargeting().getTracking();
        } catch (NullPointerException e){
            return 1;
        }

        return tracking.getPreviousCenters() == null
                || tracking.getPreviousCenters().isEmpty() ? 1 : tracking.getPreviousCenters().size();
    }
}
