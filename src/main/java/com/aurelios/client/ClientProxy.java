package com.aurelios.client;

import com.aurelios.server.IProxy;
import com.aurelios.server.network.ids.gui.GuiTypes;
import net.minecraft.client.Minecraft;

public class ClientProxy implements IProxy {

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
        Minecraft.getMinecraft().displayGuiScreen(type.getGui());
    }

    @Override
    public void stoppingServer() {

    }
}
