package com.aurelios.common;

import com.aurelios.common.commands.*;
import com.aurelios.common.event.*;
import com.aurelios.common.runnable.AbilityTimer;
import com.aurelios.common.runnable.GameTimer;
import com.aurelios.common.runnable.SlowTimer;
import com.aurelios.common.util.database.MongoUtils;
import com.aurelios.common.util.misc.Calendar;
import org.spongepowered.api.Sponge;

//@SidedProxy(serverSide = )
public enum ServerProxy implements IProxy{

    INSTANCE;

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {

    }
}
