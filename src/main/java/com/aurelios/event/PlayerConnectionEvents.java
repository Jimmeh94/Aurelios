package com.aurelios.event;

import com.aurelios.game.user.UserPlayer;
import com.aurelios.managers.Managers;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerConnectionEvents {

    @Listener
    public void onJoin(ClientConnectionEvent.Join event){
        event.setMessageCancelled(true);

        new UserPlayer(event.getTargetEntity());
    }

    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect event){
        event.setMessageCancelled(true);
        Managers.USER.find(event.getTargetEntity().getUniqueId()).get().cleanUp();
    }

}
