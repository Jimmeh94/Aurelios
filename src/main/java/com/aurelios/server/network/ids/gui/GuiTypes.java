package com.aurelios.server.network.ids.gui;

import com.aurelios.client.gui.GuiNodeCreator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;

public enum GuiTypes {

    GUI_NODE_CREATOR;

    public int getID(){
        for(int i = 0; i < GuiTypes.values().length; i++){
            if(GuiTypes.values()[i] == this){
                return i;
            }
        }
        return -1;
    }

    public static Optional<GuiTypes> getGuiType(int id){
        if(id < 0 || id >= GuiTypes.values().length){
            return Optional.empty();
        } else {
            return Optional.of(GuiTypes.values()[id]);
        }
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui() {
        switch (this){
            case GUI_NODE_CREATOR: return new GuiNodeCreator();
            default: return null;
        }
    }
}
