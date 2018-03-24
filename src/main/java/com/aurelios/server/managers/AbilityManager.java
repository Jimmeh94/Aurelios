package com.aurelios.server.managers;

import com.aurelios.Aurelios;
import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.enums.AbilityConditionType;
import com.aurelios.server.util.misc.LocationUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbilityManager extends Manager<AbilityManager.Entry> {

    public Optional<Entry> find(Ability ability){
        for(Entry entry: objects){
            if(entry.getAbility() == ability || entry.getAbility().equals(ability)){
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    public void remove(Ability ability){
        for(Entry entry: objects){
            if(entry.getAbility() == ability || entry.getAbility().equals(ability)){

                objects.remove(entry);
            }
        }
    }

    public void update() {
        for(Entry entry: objects){
            if(entry.shouldUpdate()){
                entry.getAbility().update();
            }
        }

        Managers.ABILITY_COLLISION.performCollisions();
    }

    public List<Ability> getNearbyCollisionAbilities(Ability a) {
        List<Ability> give = new CopyOnWriteArrayList<>();

        for(Entry ability: objects){
            if(ability.getAbility() == a || ability.getAbility().equals(a)
                    || ability.getAbility().getCaster() == a.getCaster() || ability.getAbility().getCaster().equals(a.getCaster()))
                continue;
            else {
                if(ability.getAbility().hasCondition(AbilityConditionType.COLLISION)){
                    /*if(ability.getAbility().getTargeting().getTracking().getCenter()
                            .distance(a.getTargeting().getTracking().getCenter()) <= 150){
                        give.add(ability.ability);
                    }*/
                    if(LocationUtils.withinDistance(ability.getAbility().getTargeting().getTracking().getCenter(),
                            a.getTargeting().getTracking().getCenter(), 150)){
                        give.add(ability.ability);
                    }
                }
            }
        }

        return give;
    }

    public static class Entry{

        private Ability ability;
        private long interval, lastUpdated;

        public Entry(Ability ability, long interval) {
            this.ability = ability;
            this.interval = interval;
        }

        public boolean shouldUpdate(){
            lastUpdated += Aurelios.INSTANCE.getAbilityTimer().getTask().getInterval();
            if(lastUpdated >= interval){
                lastUpdated = 0L;
                return true;
            }

            return false;
        }

        public Ability getAbility() {
            return ability;
        }
    }
}
