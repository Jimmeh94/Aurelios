package com.aurelios.game.user.ai.traits;

import com.aurelios.game.user.ai.AIMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Names {

    public enum FirstName{
        BOB("Bob", new Prerequisite.Needs(Gender.MALE, EconomicClass.ANY, Race.ANY)),
        BILL("Bill", new Prerequisite.Needs(Gender.MALE, EconomicClass.ANY, Race.DARK_ELF, Race.ORC)),
        DEFAULT_MALE("No Matching Name", new Prerequisite.Needs(Gender.MALE, EconomicClass.ANY, Race.ANY)),

        JANE("Jane", new Prerequisite.Needs(Gender.FEMALE, EconomicClass.ANY, Race.DARK_ELF, Race.HUMAN)),
        JILL("Jill", new Prerequisite.Needs(Gender.FEMALE, EconomicClass.NORMAL, EconomicClass.WEALTHY, Race.HUMAN, Race.DWARF)),
        DEFAULT_FEMALE("No Matching Name - F", new Prerequisite.Needs(Gender.FEMALE, EconomicClass.ANY, Race.ANY));

        private String name;
        private Prerequisite prerequisite;

        FirstName(String name, Prerequisite prerequisite) {
            this.name = name;
            this.prerequisite = prerequisite;
        }

    }

    public static String getRandomFirstName(AIMetaData metaData){
        Random random = new Random();
        List<Names.FirstName> possible = new ArrayList<>();
        List<Enum> enums = metaData.getAllEnums();

        for(Names.FirstName f: Names.FirstName.values()){
            if(f == Names.FirstName.DEFAULT_FEMALE || f == Names.FirstName.DEFAULT_MALE){
                continue;
            }
            if(f.prerequisite.valid(enums)){
                possible.add(f);
            }
        }

        if(possible.size() > 0){
            return possible.get(random.nextInt(possible.size())).name;
        } else {
            if(metaData.getGender() == Gender.MALE){
                return Names.FirstName.DEFAULT_MALE.name;
            } else return Names.FirstName.DEFAULT_FEMALE.name;
        }
    }

    public static String getRandomLastName(){
        return "ThisIsALazyLastName";
    }

}
