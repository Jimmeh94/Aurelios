package com.aurelios.server.game.ability.animation;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.util.particles.effects.AbstractEffect;

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
