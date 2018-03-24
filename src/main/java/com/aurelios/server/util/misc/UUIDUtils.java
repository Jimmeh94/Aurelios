package com.aurelios.server.util.misc;

import java.util.UUID;

public class UUIDUtils {

    public static String getRawUUID(UUID uuid){
       return uuid.toString().replaceAll("-", "");
    }

    public static String getHyphenatedUUID(String uuid){
        StringBuilder str = new StringBuilder(uuid);
        int idx = str.length() - 8;

        while (idx > 0)
        {
            str.insert(idx, "-");
            idx = idx - 8;
        }
        return str.toString();
    }

}
