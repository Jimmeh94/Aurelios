package com.aurelios.game.user.ai.traits;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Race {

    HUMAN,
    WOOD_ELF,
    DARK_ELF,
    DWARF,
    ORC,
    ANY;

    public static Race getRandomRace(){
        List<Race> temp = new ArrayList<>();
        for(Race race: Race.values()){
            if(race != ANY){
                temp.add(race);
            }
        }
        return temp.get((new Random()).nextInt(temp.size() - 1));
    }

}
