package com.aurelios.common.game.ability.condition;

import com.aurelios.common.game.ability.condition.effects.ConditionEndEffect;
import com.aurelios.common.game.ability.enums.AbilityConditionType;
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
