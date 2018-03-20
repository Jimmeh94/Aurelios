package com.aurelios.managers;

import com.aurelios.game.user.User;
import com.aurelios.game.user.UserPlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserManager extends Manager<User>{

    public Optional<UserPlayer> findPlayer(UUID uuid){
        Optional<UserPlayer> give = Optional.empty();

        for(User user: this.objects){
            if(user.getUuid().equals(uuid) && user.isPlayer()){
                give = Optional.of((UserPlayer) user);
            }
        }

        return give;
    }

    public Optional<User> find(UUID uuid){
        Optional<User> give = Optional.empty();

        for(User user: this.objects){
            if(user.getUuid().equals(uuid)){
                give = Optional.of(user);
            }
        }

        return give;
    }

    public void tick() {
        for(User user: objects) {
            user.tick();
        }
    }

    public List<User> getObjects() {
        return objects;
    }
}
