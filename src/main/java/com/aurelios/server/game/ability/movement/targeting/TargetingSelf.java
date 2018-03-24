package com.aurelios.server.game.ability.movement.targeting;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.movement.Targeting;
import com.aurelios.server.game.user.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * =========== SELF TARGETING ==================
 */

public abstract class TargetingSelf extends Targeting<User> {

    public TargetingSelf(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget) {
        super(parent, endAbilityHere, playEffectAtTarget);
    }

    public TargetingSelf(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget, double movementScale) {
        super(parent, endAbilityHere, playEffectAtTarget, movementScale);
    }

    @Override
    public void findTarget() {
        target = parent.getCaster();
    }

    @Override
    public Text getChatRepresentation() {
        return Text.of(TextColors.GOLD, "Self");
    }
}
