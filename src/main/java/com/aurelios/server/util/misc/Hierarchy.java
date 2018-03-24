package com.aurelios.server.util.misc;

import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * General hierarchy structure that can be used for chat channels, parties, guilds, etc
 */
public class Hierarchy {

    private List<Rank> ranks;

    public Hierarchy(){ranks = new CopyOnWriteArrayList<>();}

    public Hierarchy addRank(Rank rank){
        for(Rank r: ranks){
            if(r.getTitle() == rank.getTitle()){
                return this;
            } else {
                if(r.hasInheritance(rank)){
                    return this;
                }
            }
        }
        return this;
    }

    public Hierarchy removeRank(Rank rank){
        //TODO remove all inheritances of this and then remove from list
        return this;
    }



    public static class Rank{

        private Text title;
        private List<Rank> inheritance;
        private List<String> perms;

        public Rank(){
            inheritance = new CopyOnWriteArrayList<>();
            perms = new CopyOnWriteArrayList<>();
        }

        public Rank(Text title){this.title = title;}

        public Rank addPerm(String perm){
            if(!hasPerm(perm)){
                if(inheritance != null){
                    for(Rank rank: inheritance){
                        if(rank.hasPerm(perm)){
                            return this;
                        }
                    }
                }
                perms.add(perm);
            }
            return this;
        }

        public Rank addInheritance(Rank rank){
            if(!hasInheritance(rank)){
                inheritance.add(rank);
            }
            return this;
        }

        private boolean hasInheritance(Rank rank) {
            for(Rank r: inheritance){
                if(r.getTitle() == rank.getTitle()){
                    return true;
                } else {
                    if(r.hasInheritance(rank)){
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean hasPerm(String perm) {
            return perms.contains(perm);
        }

        public Text getTitle() {
            return title;
        }
    }

}
