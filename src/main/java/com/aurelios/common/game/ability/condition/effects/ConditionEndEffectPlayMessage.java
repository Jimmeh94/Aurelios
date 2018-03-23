package com.aurelios.common.game.ability.condition.effects;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.util.text.Message;
import com.aurelios.common.util.text.Messager;

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
