package com.aurelios.game.ability.condition.effects;

import com.aurelios.game.ability.Ability;

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
