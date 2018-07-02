package com.aurelios.server.managers;

import com.aurelios.server.game.user.User;
import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.scoreboard.presets.DefaultPreset;

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

    public void slowTick(){
        for(User user: objects){
            if(user.isPlayer()){
                UserPlayer u = (UserPlayer)user;
                if(u.getScoreboard().getPreset() instanceof DefaultPreset){
                    u.getScoreboard().updateScoreboard();
                    //Updating the time of day
                }
            }
        }

    }

    public List<User> getObjects() {
        return objects;
    }
}
