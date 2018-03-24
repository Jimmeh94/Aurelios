package com.aurelios.server.game.user.ai.traits;

import com.aurelios.server.game.user.ai.AIMetaData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public enum Aspiration {

    //TODO Do we allow higher ambitions to have a chance to get lower ambition goals, but not let
    //TODO lower ambitions have access to higher ambitions. This could give more variety to higher ambition npcs

    NONE("Live life day by day; no clear goal to go beyond where I am now", Ambition.NONE),
    ASSASSIN_WORLD_CLASS("Become the most well known assassin", Ambition.MAX, new Prerequisite.Needs(Role.ASSASSIN)),
    ASSASSIN_FOR_HIRE("Make enough coin to get by as an assassin", Ambition.NORMAL, new Prerequisite.Needs(Role.ASSASSIN)),
    CHEF_WORLD_CLASS("Become the most well known chef", Ambition.MAX, new Prerequisite.Needs(Role.COOK)),
    ORGANIZATION_LEADER("Lead an organization (can be bandit camp -> police force, based on morality)", Ambition.MAX),
    HIRED_GUARD("To make some extra coin as a hired guard", Ambition.NORMAL, new Prerequisite.NeedsOneOf(Role.ASSASSIN, Role.BANDIT,
            Role.GUARD, Role.MAGE, Role.WARRIOR, Role.MENTOR));

    private String description;
    private Ambition ambition;
    private Prerequisite[] prereqs;

    Aspiration(String d, Ambition ambition, Prerequisite... prereqs) {
        this.description = d;
        this.ambition = ambition;
        this.prereqs = prereqs;
    }

    public static List<Aspiration> getRandomAspiration(AIMetaData npc){
        List<Aspiration> a = new CopyOnWriteArrayList<>();
        a.addAll(Arrays.asList(Aspiration.values()));
        a.remove(NONE);

        List<Enum> e = npc.getAllEnums();
        for(Aspiration as: a){
            if(as.ambition != npc.getAmbition()){
                a.remove(as);
                continue;
            }
            if(as.prereqs != null){
                for(Prerequisite p: as.prereqs){
                    if(!p.valid(e)){
                        a.remove(as);
                        break;
                    }
                }
            }
        }

        if(a.size() == 0){
            return Arrays.asList(NONE);
        } else {
            return a;
        }
    }

    private boolean isApplicable(Ambition ambition, List<Enum> metadataEnums){
        if(Ambition.hasEnoughAmbition(this.ambition, ambition)){
            if(prereqs == null){
                return true;
            }

            for(Prerequisite p: prereqs){
                if(!p.valid(metadataEnums)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static int getMaxAspirationsAvailableFor(Ambition ambition, List<Enum> metadataEnums){
        int give = 1;
        for(int i = 1; i < Aspiration.values().length; i++){
            if(Aspiration.values()[i].isApplicable(ambition, metadataEnums)){
                give++;
            }
        }
        return give;
    }

    public static List<Aspiration> getAvailableAspirations(Ambition ambition, List<Enum> metadataEnums){
        List<Aspiration> give = new ArrayList<Aspiration>();
        give.add(NONE);
        for(int i = 1; i < Aspiration.values().length; i++){
            if(Aspiration.values()[i].isApplicable(ambition, metadataEnums)){
                give.add(Aspiration.values()[i]);
            }
        }
        return give;
    }

    public static Aspiration getRandomAspirationWithoutDuplicates(AIMetaData npc) {
        List<Aspiration> temp = getAvailableAspirations(npc.getAmbition(), npc.getAllEnums());
        Aspiration give = temp.get(new Random().nextInt(temp.size()));

        while(npc.hasAspiration(give)){
            give = temp.get(new Random().nextInt(temp.size()));
        }

        return give;
    }

    public String getDescription() {
        return description;
    }
}
