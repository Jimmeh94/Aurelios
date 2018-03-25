package com.aurelios.server;


import com.aurelios.Aurelios;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.network.ids.gui.GuiTypes;

    public class ServerProxy implements IProxy{

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {

    }

    @Override
    public void openGUI(GuiTypes type) {

    }

    @Override
    public void stoppingServer() {
        Aurelios.INSTANCE.getMongoUtils().close();
        Managers.AI.despawn();
    }
}
