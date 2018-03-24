package com.aurelios.server.game.ability.condition.effects;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.util.text.Message;
import com.aurelios.server.util.text.Messager;

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
