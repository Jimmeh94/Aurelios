package com.aurelios.server.game.ability.condition;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.condition.effects.ConditionEndEffect;
import com.aurelios.server.game.ability.enums.AbilityConditionType;
import com.aurelios.server.game.user.User;
import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.stats.Stat;
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
                ((UserPlayer)caster).getScoreboard().updateScoreboard();
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
