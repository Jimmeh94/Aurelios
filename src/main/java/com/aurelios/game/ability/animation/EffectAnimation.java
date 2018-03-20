package com.aurelios.game.ability.animation;

import com.aurelios.game.ability.Ability;
import com.aurelios.util.particles.effects.AbstractEffect;

public class EffectAnimation extends Animation {

    protected AbstractEffect effect;

    public EffectAnimation(Ability container, AbstractEffect abstractEffect){
        super(container);

        this.effect = abstractEffect;
    }

    @Override
    public void draw() {
        effect.prePlay();
    }
}
