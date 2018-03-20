package com.aurelios.game.ability.animation;

import com.aurelios.game.ability.Ability;

public abstract class Animation {

    protected Ability abilityContainer;

    public Animation(Ability abilityContainer) {
        this.abilityContainer = abilityContainer;
    }

    public abstract void draw();
}
