package com.aurelios.server.game.user.ai.traits;

import com.aurelios.server.util.misc.StringUtils;

import java.util.Arrays;
import java.util.List;

public abstract class Prerequisite {

    protected List<Enum> enums;

    public Prerequisite(Enum... e){
        this.enums = Arrays.asList(e);
    }

    public abstract boolean valid(List<Enum> metadata);

    public static class Needs extends Prerequisite{

        public Needs(Enum... e) {
            super(e);
        }

        @Override
        public boolean valid(List<Enum> metadata) {
            for(Enum e: enums) {
                if (StringUtils.enumToString(e, false).equalsIgnoreCase("ANY")) {
                    continue;
                } else if(!metadata.contains(e)){
                    return false;
                }
            }
            return true;
        }
    }

    public static class NeedsOneOf extends Prerequisite{

        public NeedsOneOf(Enum... e) {
            super(e);
        }

        @Override
        public boolean valid(List<Enum> metadata) {
            for(Enum e: enums){
                if(metadata.contains(e)){
                    return true;
                }
            }
            return false;
        }
    }

    public static class CantHave extends Prerequisite{

        public CantHave(Enum... e) {
            super(e);
        }

        @Override
        public boolean valid(List<Enum> metadata) {
            for(Enum e: enums){
                if(metadata.contains(e)){
                    return false;
                }
            }
            return true;
        }
    }

}
