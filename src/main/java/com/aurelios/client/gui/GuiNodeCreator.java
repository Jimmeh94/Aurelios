package com.aurelios.client.gui;

import com.aurelios.common.network.ids.gui.GuiTypes;
import com.aurelios.common.network.ids.gui.Identifiable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class GuiNodeCreator extends GuiScreen implements Identifiable{

    public static void displayGui(){
        Minecraft.getMinecraft().displayGuiScreen(new GuiNodeCreator());
    }

    private final ResourceLocation texture = new ResourceLocation("aurelios","textures/gui/NodeCreatorGui.png");
    private final int guiWidth = 206;
    private final int guiHeight = 239;

    @Override
    public GuiTypes getGuiType() {
        return GuiTypes.GUI_NODE_CREATOR;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        System.out.println("HERE ===================");
        if(texture == null){
            Minecraft.getMinecraft().player.sendChatMessage("NULL");
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        //where on the screen x, where on the screen y, where on texture x, where on texture y, width, height
        drawTexturedModalRect(0, 0, 0, 0, guiWidth, guiHeight);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
