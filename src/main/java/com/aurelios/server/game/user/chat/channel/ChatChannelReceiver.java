package com.aurelios.server.game.user.chat.channel;

import com.aurelios.server.game.user.User;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is a chat channel that is purely for listening to other players
 * You can specify a player to listen to and their messages will get sent here as well
 */
public class ChatChannelReceiver {

    private List<UUID> players;
    private User owner;

    public ChatChannelReceiver(User owner){
        this.owner = owner;
        players = new CopyOnWriteArrayList<>();
    }

    public void addPlayer(UUID uuid){
        if(!players.contains(uuid)){
            players.add(uuid);
        }
    }

    public void removePlayer(UUID uuid){
        players.remove(uuid);
    }

    public boolean has(UUID uuid){
        return players.contains(uuid);
    }

}
