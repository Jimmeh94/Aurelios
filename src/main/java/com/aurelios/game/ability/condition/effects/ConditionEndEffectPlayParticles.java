package com.aurelios.game.ability.condition.effects;

import com.aurelios.game.ability.Ability;
import com.aurelios.util.particles.ParticleData;
import com.aurelios.util.particles.ParticlePlayer;

public class ConditionEndEffectPlayParticles extends ConditionEndEffect {

    private ParticleData data;

    public ConditionEndEffectPlayParticles(Ability container, ParticleData data) {
        super(container);

        this.data = data;
    }

    @Override
    public void doEffect() {
        ParticlePlayer.display(data);
    }
}
