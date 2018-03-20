package com.aurelios.event;

import com.aurelios.game.user.User;
import com.aurelios.managers.Managers;
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
