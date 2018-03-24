package com.aurelios.server.game.user.ai.traits.personality.types;


import com.aurelios.server.game.user.ai.traits.personality.Personalities;
import com.aurelios.server.game.user.ai.traits.personality.Personality;

public class Irritable extends Personality {

    public Irritable() {
        super(Personalities.IRRITABLE, -5, 10);
    }

    protected void generateCompatiblePersonalities() {

    }
}
