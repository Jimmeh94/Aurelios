package com.aurelios.game.user.ai.traits.personality;

import com.aurelios.game.user.ai.traits.personality.types.*;

public enum Personalities {

    CUNNING(Cunning.class),
    DECISIVE(Decisive.class),
    IRRITABLE(Irritable.class),
    NAIVE(Naive.class),
    SOCIAL(Social.class);

    private Class<? extends Personality> clazz;

    Personalities(Class<? extends Personality> c) {
        this.clazz = c;
    }

    public Class<? extends Personality> getClazz() {
        return clazz;
    }
}
