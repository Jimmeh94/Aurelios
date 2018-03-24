package com.aurelios.server.game.user.ai.traits;

import java.util.Random;

public enum Gender {

    MALE, FEMALE;

    public static Gender getRandomGender(){
        Random random = new Random();
        return Gender.values()[random.nextInt(2)];
    }

}
