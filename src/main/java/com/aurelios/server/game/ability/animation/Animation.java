package com.aurelios.server.game.ability.animation;

import com.aurelios.server.game.ability.Ability;

public abstract class Animation {

    protected Ability abilityContainer;

    public Animation(Ability abilityContainer) {
        this.abilityContainer = abilityContainer;
    }

    public abstract void draw();
}
