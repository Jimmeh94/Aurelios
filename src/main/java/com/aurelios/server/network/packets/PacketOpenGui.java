package com.aurelios.server.network.packets;

import com.aurelios.Aurelios;
import com.aurelios.server.network.ids.gui.GuiTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketOpenGui implements IMessage {

    private GuiTypes guiType;

    public PacketOpenGui(){}

    public PacketOpenGui(GuiTypes type){
        this.guiType = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        guiType = GuiTypes.getGuiType(buf.readInt()).get();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(guiType.getID());
    }

    public static class GuiPacketHandler implements IMessageHandler<PacketOpenGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                Aurelios.proxy.openGUI(message.guiType);
            }
            return null;
        }
    }
}
