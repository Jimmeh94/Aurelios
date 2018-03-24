package com.aurelios.server.game.ability.animation;

import com.aurelios.server.game.ability.Ability;
import org.spongepowered.api.effect.particle.ParticleType;

public class AnimationTrail extends Animation{

    private int length;
    private ParticleType type;
    private int quantity;
    private double offsetDivisor;

    public AnimationTrail(Ability ability, int length, ParticleType type, int quantity, double offsetDivisor) {
        super(ability);

        this.length = length;
        this.type = type;
        this.quantity = quantity;
        this.offsetDivisor = offsetDivisor;
    }

    @Override
    public void draw() {
        PremadeAnimations.playTrail(abilityContainer, length, type, quantity, offsetDivisor);
    }
}
