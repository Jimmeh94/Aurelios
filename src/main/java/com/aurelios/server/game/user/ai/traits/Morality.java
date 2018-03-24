package com.aurelios.server.game.user.ai.traits;

import java.util.Random;

public enum Morality {

    EVIL,
    CORRUPTED,
    NEUTRAL,
    GOOD,
    ENLIGHTENED;

    public static Morality getRandomMorality(){
        return Morality.values()[(new Random()).nextInt(Morality.values().length)];
    }

    public boolean isCompatibleWith(Morality morality) {
        switch (this){
            case EVIL: return morality == CORRUPTED || morality == NEUTRAL ? true : false;
            case CORRUPTED: return morality == EVIL || morality == NEUTRAL ? true : false;
            case NEUTRAL: return true;
            case GOOD: return morality == NEUTRAL || morality == ENLIGHTENED ? true : false;
            case ENLIGHTENED: return morality == NEUTRAL || morality == GOOD ? true : false;
            default: return false;
        }
    }
}
