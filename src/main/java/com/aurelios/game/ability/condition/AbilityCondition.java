package com.aurelios.game.ability.condition;

import com.aurelios.game.ability.condition.effects.ConditionEndEffect;
import com.aurelios.game.ability.enums.AbilityConditionType;
import org.spongepowered.api.text.Text;

/**
 * Currently this is a condition to end the ability
 */
public abstract class AbilityCondition {

    private ConditionEndEffect[] cancelEffects;

    public AbilityCondition(ConditionEndEffect... cancelEffects) {
        this.cancelEffects = cancelEffects;
    }

    public abstract boolean isComplete();

    public abstract AbilityConditionType getConditionType();

    public ConditionEndEffect[] getCancelEffects(){
        return cancelEffects;
    }

    public abstract Text getChatRepresentation();
}
