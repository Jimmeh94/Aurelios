package com.aurelios.common.game.ability.condition;

import com.aurelios.common.game.ability.condition.effects.ConditionEndEffect;
import com.aurelios.common.game.ability.enums.AbilityConditionType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ConditionTimeLimit extends AbilityCondition {

    private double howLong;
    private long whenStarted;

    public ConditionTimeLimit(double seconds, ConditionEndEffect... cancelEffects) {
        super(cancelEffects);

        this.howLong = seconds;
    }

    public void start(){
        whenStarted = System.currentTimeMillis();
    }

    @Override
    public boolean isComplete() {
        return whenStarted != 0L && (System.currentTimeMillis() - whenStarted)/1000 >= howLong;
    }

    @Override
    public AbilityConditionType getConditionType() {
        return AbilityConditionType.TIME_LIMIT_REACHED;
    }

    @Override
    public Text getChatRepresentation() {
        return Text.of(TextColors.GOLD, "Duration: " + howLong + "s");
    }
}
