package com.aurelios.game.ability.condition;

import com.aurelios.game.ability.Ability;
import com.aurelios.game.ability.condition.effects.ConditionEndEffect;
import com.aurelios.game.ability.enums.AbilityConditionType;
import com.aurelios.game.user.User;
import com.aurelios.game.user.UserPlayer;
import com.aurelios.game.user.stats.Stat;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * This will drain the specified stat by an amount each ability iteration.
 * Currently, if the player shifts, that will cancel the focus
 */
public class ConditionFocus extends AbilityCondition {

    private double amount;
    private Stat.Type type;
    private Ability container;

    public ConditionFocus(Ability container, double amount, Stat.Type type, ConditionEndEffect... cancelEffects) {
        super(cancelEffects);

        this.container = container;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public boolean isComplete() {
        User caster = container.getCaster();
        if(caster.getStats().find(type).isPresent() && caster.getStats().find(type).get().canAfford(amount)
                && !caster.getEntity().get(Keys.IS_SNEAKING).get().booleanValue()){
            caster.getStats().find(type).get().subtract(amount);

            if(caster.isPlayer()){
                ((UserPlayer)caster).getSAureliosboard().updateSAureliosboard();
            }
            return false;
        }
        return true;
    }

    @Override
    public AbilityConditionType getConditionType() {
        return AbilityConditionType.STOPPED_FOCUS;
    }

    @Override
    public Text getChatRepresentation() {

        return Text.of(TextColors.GOLD, "Drains " + amount + " "
                + type.toString() + " every " + (container.getInterval()/20L) + "s");
    }
}
