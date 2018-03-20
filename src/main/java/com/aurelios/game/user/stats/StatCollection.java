package com.aurelios.game.user.stats;

import com.aurelios.game.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StatCollection {

    private Map<Stat.Type, Stat> stats;
    private User owner;

    public StatCollection(User owner){
        this.owner = owner;
        stats = new HashMap<>();
    }

    public void add(Stat stat){
        stats.put(stat.getType(), stat);
    }

    public boolean has(Stat.Type type){
        return stats.containsKey(type);
    }

    public void remove(Stat.Type type){
        stats.remove(type);
    }

    public Optional<Stat> find(Stat.Type type){
        if(has(type)){
            return Optional.of(stats.get(type));
        } else return Optional.empty();
    }

}
