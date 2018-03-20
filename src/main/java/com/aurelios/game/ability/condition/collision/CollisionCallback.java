package com.aurelios.game.ability.condition.collision;

import com.aurelios.game.ability.Ability;
import com.aurelios.util.misc.Callback;
import com.aurelios.util.particles.ParticleData;
import com.aurelios.util.particles.ParticlePlayer;

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
