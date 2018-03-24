package com.aurelios.server.game.ability.condition.effects;

import com.aurelios.server.game.ability.Ability;

public abstract class ConditionEndEffect {

    protected Ability container;

    public abstract void doEffect();

    public ConditionEndEffect(Ability container) {
        this.container = container;
    }

    public Ability getContainer() {
        return container;
    }
}
