package com.aurelios.server.game.user.ai.traits;

import java.util.Random;

public enum Ambition {

    NONE,
    NORMAL,
    MAX;

    public static Ambition getRandomAmibition(){
        return Ambition.values()[(new Random()).nextInt(Ambition.values().length)];
    }

    public static boolean hasEnoughAmbition(Ambition needed, Ambition has){
        if(needed == NONE){
            return true;
        } else if(needed == NORMAL && (has == NORMAL || has == MAX)){
            return true;
        } else if(needed == MAX && has == MAX){
            return true;
        } else return false;
    }

}
