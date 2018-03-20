package com.aurelios.game.user.ai.traits;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum EconomicClass {

    WEALTHY,
    NORMAL,
    PEASANT,
    ANY;

    public static EconomicClass getRandomClass(){
        List<EconomicClass> temp = new ArrayList<>();
        for(EconomicClass e: EconomicClass.values()){
            if(e != ANY){
                temp.add(e);
            }
        }
        return temp.get((new Random()).nextInt(temp.size() - 1));
    }

}
