package com.aurelios.server.util.misc;

import com.aurelios.Aurelios;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;

public class MiscTools {

    public static Cause getEmptyCause(){return Cause.builder().append(Aurelios.INSTANCE.PLUGIN_CONTAINER).build(EventContext.builder().build());}

}
