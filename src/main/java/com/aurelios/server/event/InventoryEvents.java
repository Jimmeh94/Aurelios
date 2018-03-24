package com.aurelios.server.event;

import com.aurelios.server.game.user.User;
import com.aurelios.server.managers.Managers;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

public class InventoryEvents {

    @Listener
    public void onClick(ClickInventoryEvent event, @First Player player){
        User user = Managers.USER.find(player.getUniqueId()).get();

    }

}
