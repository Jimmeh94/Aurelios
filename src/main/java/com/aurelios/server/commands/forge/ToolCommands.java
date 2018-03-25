package com.aurelios.server.commands.forge;

import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.modules.UserModuleTypes;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.network.AureliosPacketHandler;
import com.aurelios.server.network.ids.gui.GuiTypes;
import com.aurelios.server.network.packets.PacketOpenGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolCommands extends CommandBase {

    private final List<String> aliases;

    public ToolCommands() {
        aliases = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "tool";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "tool <node|poi> <radius> <name> [aiCap|roles]";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(!sender.getEntityWorld().isRemote && sender instanceof EntityPlayer){
            //means this is server side

            AureliosPacketHandler.INSTANCE.sendTo(new PacketOpenGui(GuiTypes.GUI_NODE_CREATOR), (EntityPlayerMP) sender);
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        //Make sure this is server side
        if(!sender.getEntityWorld().isRemote && sender instanceof EntityPlayer){
            Optional<UserPlayer> player = Managers.USER.findPlayer(((EntityPlayer)sender).getUniqueID());
            if(player.isPresent()){
                return player.get().hasModule(UserModuleTypes.NODE_CREATOR);
            }
        }
        return false;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }




}
