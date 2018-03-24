package com.aurelios.server.managers;

import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.modules.poll.Poll;

import java.util.Optional;

public class PollManager extends Manager<Poll> {

    public void tick(){
        for(Poll poll: objects){
            if((System.currentTimeMillis() - poll.getWhenStarted())/1000 >= poll.getLength() * 60){
                poll.stop();
            }
        }
    }

    public boolean has(Poll poll) {
        for(Poll p: objects){
            if(p == poll || p.equals(poll)){
                return true;
            }
        }
        return false;
    }

    public boolean isRunningPoll(UserPlayer player){
        for(Poll p: objects){
            if(p.getOwner() == player || p.getOwner().equals(player)){
                return true;
            }
        }
        return false;
    }

    public Optional<Poll> getPoll(UserPlayer player){
        for(Poll p: objects){
            if(p.getOwner() == player || p.getOwner().equals(player)){
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
}
