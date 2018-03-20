package com.aurelios.game.ability.movement.targeting;


import com.aurelios.game.ability.Ability;
import com.aurelios.game.ability.movement.Targeting;
import com.aurelios.game.user.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public abstract class TargetingSingle extends Targeting<User> {

    //TODO how do we find target? Nearest to player or nearest to eyesight?

    private TargetingFlag flag;

    public TargetingSingle(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget, TargetingFlag flag) {
        super(parent, endAbilityHere, playEffectAtTarget);
        this.flag = flag;
    }

    public TargetingSingle(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget, double movementScale, TargetingFlag flag) {
        super(parent, endAbilityHere, playEffectAtTarget, movementScale);
        this.flag = flag;
    }

    @Override
    public void findTarget() {

    }

    @Override
    public Text getChatRepresentation() {
        switch (flag){
            case ALLY: return Text.of(TextColors.GOLD, "Single - Ally");
            case ENEMY: return Text.of(TextColors.GOLD, "Single - Enemy");
            case EITHER: return Text.of(TextColors.GOLD, "Single - Ally/Enemy");
            default: return Text.of(TextColors.GOLD, "Single - Ally/Enemy");
        }
    }

}
