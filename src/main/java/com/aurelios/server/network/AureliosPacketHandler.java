package com.aurelios.shared.network;

import com.aurelios.shared.network.packets.PacketOpenGui;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class AureliosPacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("aurelios");
    private static int id = -1;

    public AureliosPacketHandler() {
        INSTANCE.registerMessage(PacketOpenGui.GuiPacketHandler.class, PacketOpenGui.class, id++, Side.CLIENT);
    }
}
