package com.aurelios.server.game.user.ai.traits.personality.types;


import com.aurelios.server.game.user.ai.traits.personality.Personalities;
import com.aurelios.server.game.user.ai.traits.personality.Personality;

public class Naive extends Personality {

    public Naive() {
        super(Personalities.NAIVE, 5, 25);
    }

    protected void generateCompatiblePersonalities() {

    }
}
