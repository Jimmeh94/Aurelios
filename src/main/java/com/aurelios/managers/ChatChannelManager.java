package com.aurelios.managers;

import com.aurelios.game.user.UserPlayer;
import com.aurelios.game.user.chat.channel.ChatChannel;
import com.aurelios.game.user.chat.channel.ChatChannelGlobal;
import com.aurelios.util.text.Message;
import com.aurelios.util.text.Messager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class ChatChannelManager extends Manager<ChatChannel> {

    public ChatChannel GLOBAL;

    public void init(){
        GLOBAL = new ChatChannelGlobal("");
    }


    public boolean isKeyAvailable(String key){
        return !findChannel(key).isPresent();
    }

    private Optional<ChatChannel> findChannel(String key){
        for(ChatChannel chatChannel: objects){
            if(chatChannel.getKey().equalsIgnoreCase(key)){
                return Optional.of(chatChannel);
            }
        }
        return Optional.empty();
    }

    public void setToDefault(UserPlayer userPlayer){
        userPlayer.setChatChannel(GLOBAL);
    }

    public void setChannel(UserPlayer userPlayer, String key){
        Optional<ChatChannel> channel = findChannel(key);

        if(channel.isPresent() && channel.get() != userPlayer.getChatChannel()){
            userPlayer.setChatChannel(channel.get());
            Messager.sendMessage(Message.builder().addReceiver(userPlayer.getPlayer()).addMessage(Text.of(TextColors.GRAY, "Moved to chat channel: " + channel.get().getKey()), Messager.Prefix.SUCCESS).build());
        } else {
            Messager.sendMessage(Message.builder().addReceiver(userPlayer.getPlayer()).addMessage(Text.of(TextColors.GRAY, "The channel doesn't exist or you are already in it: " + key), Messager.Prefix.ERROR).build());
        }
    }

}
