package com.aurelios.server;

import com.aurelios.server.network.ids.gui.GuiTypes;

public interface IProxy {

    void preInit();
    void init();
    void postInit();
    void openGUI(GuiTypes type);

}
