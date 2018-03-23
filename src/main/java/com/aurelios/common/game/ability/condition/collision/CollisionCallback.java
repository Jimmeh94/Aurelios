package com.aurelios.common.game.ability.condition.collision;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.util.misc.Callback;
import com.aurelios.common.util.particles.ParticleData;
import com.aurelios.common.util.particles.ParticlePlayer;

public class CollisionCallback implements Callback {

    protected ParticleData particleData;
    protected Ability container;

    public CollisionCallback(Ability container, ParticleData particleData) {
        this.particleData = particleData;
        this.container = container;
    }

    public ParticleData getParticleData() {
        return particleData;
    }

    @Override
    public boolean doWork() {
        particleData.setViewers(container.getTargeting().getTracking().getCenter(), 100);
        ParticlePlayer.display(particleData);
        return false;
    }
}
