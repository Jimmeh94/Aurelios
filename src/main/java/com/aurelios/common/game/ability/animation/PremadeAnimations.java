package com.aurelios.common.game.ability.animation;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.util.particles.ParticleData;
import com.aurelios.common.util.particles.ParticlePlayer;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.effect.particle.ParticleType;

import java.util.List;

public class PremadeAnimations {

    /**
     * This will simply print a trail "length" length behind the ability
     * @param ability
     * @param length
     * @param type
     * @param quantity
     */
    public static void playTrail(Ability ability, int length, ParticleType type, int quantity, double offsetDivisor){

        if (ability.getTargeting().getTracking().getPreviousCenters().size() > 1) {
            List<Vector3d> centers = ability.getTargeting().getTracking().getPreviousCenters();
            for (int i = 0; i < length && centers.size() > i + 1; i++) {
                ParticlePlayer.display(ParticleData.builder()
                        .addViewers(ability.getCaster().getEntity().getLocation().getPosition(), 100)
                        .center(centers.get(i)).quantity(quantity)
                        .offsets(.1 + (i / offsetDivisor), .1 + (i / offsetDivisor), .1 + (i / offsetDivisor)).type(type).build());
            }
        }
    }

}
