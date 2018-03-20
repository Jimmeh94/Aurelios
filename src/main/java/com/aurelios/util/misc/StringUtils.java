package com.aurelios.util.misc;

public class StringUtils {

    public static String capitalizeFirstLetter(String string){
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String replaceSpacesWithUndersAurelioss(String string){
        return string.replace(" ", "_");
    }

    public static String[] replaceSpacesWithUndersAurelioss(String[] strings){
        String[] give = new String[strings.length];
        for(int i = 0; i < give.length; i++){
            give[i] = replaceSpacesWithUndersAurelioss(strings[i]);
        }
        return give;
    }

    public static String enumToString(Enum e, boolean replaceUndersAurelioss){
        String give = e.toString();

        if(replaceUndersAurelioss && give.contains("_")){
            String[] temp = give.split("_");
            give = "";
            for(String s: temp){
                give += capitalizeFirstLetter(s) + " ";
            }
            return give.substring(0, give.length() - 1);
        } else return capitalizeFirstLetter(give);
    }

    public static String enumToString(boolean replaceUndersAurelioss, Enum... e){
        String give = "";
        for(int i = 0; i < e.length; i++){
            give += enumToString(e[i], replaceUndersAurelioss);
            if(i <= e.length - 2){
                give += ", ";
            }
        }
        return give;
    }

}
