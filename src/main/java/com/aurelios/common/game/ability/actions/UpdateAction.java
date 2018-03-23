package com.aurelios.common.game.ability.actions;

import com.aurelios.common.game.ability.Ability;

/**
 * Any sort of "extra" action the ability needs to do on updating
 */
public abstract class UpdateAction{

    protected Ability ability;
    private UpdateActionWhen when;

    public abstract void doAction();

    public UpdateAction(UpdateActionWhen when, Ability ability) {
        this.when = when;
        this.ability = ability;
    }

    public Ability getAbility() {
        return ability;
    }

    public UpdateActionWhen getWhen() {
        return when;
    }

    public enum UpdateActionWhen{
        FIRST,
        OVERWRITE_TARGETING,
        LAST
    }
}
