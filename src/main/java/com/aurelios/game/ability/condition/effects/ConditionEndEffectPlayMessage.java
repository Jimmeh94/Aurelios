package com.aurelios.game.ability.condition.effects;

import com.aurelios.game.ability.Ability;
import com.aurelios.util.text.Message;
import com.aurelios.util.text.Messager;

public class ConditionEndEffectPlayMessage extends ConditionEndEffect {

    private Message message;

    public ConditionEndEffectPlayMessage(Ability container, Message message) {
        super(container);

        this.message = message;
    }

    @Override
    public void doEffect() {
        Messager.sendMessage(message);
    }
}
