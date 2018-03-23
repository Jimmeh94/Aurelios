package com.aurelios.shared.network.packets;

import com.aurelios.shared.network.ids.gui.GuiTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenGui implements IMessage {

    public PacketOpenGui(){}

    private GuiTypes guiType;
    public PacketOpenGui(GuiTypes type){
        this.guiType = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        System.out.println("GUI ID: " + guiType.getID());
        guiType = GuiTypes.getGuiType(buf.readInt()).get();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        System.out.println("GUI ID: " + guiType.getID());
        buf.writeInt(guiType.getID());
    }

    public static class GuiPacketHandler implements IMessageHandler<PacketOpenGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx) {
            Minecraft.getMinecraft().player.sendChatMessage("I'M INSIDE ============");
            Minecraft.getMinecraft().displayGuiScreen(message.guiType.getGui());
            return null;
        }
    }
}
