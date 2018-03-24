package com.aurelios.server.game.user.ai.traits;

import com.aurelios.server.game.environment.nodes.Node;
import com.aurelios.server.game.environment.nodes.PointOfInterest;
import com.aurelios.server.game.user.ai.AIMetaData;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public enum Role {

    COOK,
    ASSASSIN,
    WARRIOR,
    BANKER,
    BEGGAR(new Prerequisite.Needs(EconomicClass.PEASANT)),
    WIDOW(new Prerequisite.Needs(Gender.FEMALE)),
    BLACKSMITH,
    FLETCHER,
    MENTOR,
    MAGE,
    GUARD,
    BANDIT(new Prerequisite.Needs(Morality.EVIL, Morality.CORRUPTED));

    private Prerequisite[] prerequisite;

    Role(Prerequisite... e){this.prerequisite = e;}

    Role(){}

    public static Role getRandomRole(AIMetaData npc, Node home){
        List<Role> temp = new CopyOnWriteArrayList<>();
        List<Enum> enums = npc.getAllEnums();

        for(Role role: Role.values()){
            if(role.prerequisite == null){
                temp.add(role);
            } else {
                boolean add = true;
                for(Prerequisite p: role.prerequisite){
                    if(!add){
                        continue;
                    } else if(!p.valid(enums)){
                        add = false;
                    }
                }
                if(add) {
                    temp.add(role);
                }
            }
        }

        //Here we make sure that the NPC is getting assigned a role that has an accessible
        //POI in the home Node
        for(PointOfInterest p: home.getChildren()){
            for(Role role: temp){
                if(!p.hasRole(role)){
                    temp.remove(role);
                }
            }
        }

        return temp.get((new Random()).nextInt(temp.size()));
    }

}
