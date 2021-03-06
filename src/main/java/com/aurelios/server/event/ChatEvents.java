package com.aurelios.server.event;

import com.aurelios.server.game.user.User;
import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.chat.ChatColorTemplate;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.text.Message;
import com.aurelios.server.util.text.Messager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;

public class ChatEvents {

    @Listener
    public void onChat(MessageChannelEvent.Chat event, @First Player player){
        event.setCancelled(true);
        UserPlayer userPlayer = Managers.USER.findPlayer(player.getUniqueId()).get();
        userPlayer.getChatChannel().displayMessage(build(event.getRawMessage().toPlain(), userPlayer));

        //Now we're going to check for receiver channels listening to this player (a channel that just listens for messages from this player)
        for(User user: Managers.USER.getObjects()){
            if(!user.isPlayer()){
                continue;
            }

            UserPlayer player1 = (UserPlayer)user;
            if(player1.hasReceiverChannel() && player1.getReceiveChannel().has(player.getUniqueId())){
                Messager.sendMessage(Message.builder().addReceiver(player1.getPlayer()).addMessage(build(event.getRawMessage().toPlain(), userPlayer)).build());
            }
        }
    }

    private Text build(String message, UserPlayer userPlayer){
        ChatColorTemplate color = userPlayer.getChatColorTemplate();
        return Text.builder().append(Text.of(color.getPrefix(), userPlayer.getTitle().getDisplay()))
                .append(Text.of(color.getName(), userPlayer.getPlayer().getName() + ": "))
                .append(Text.of(color.getMessage(), message)).build();
    }

}
