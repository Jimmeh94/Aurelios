package com.aurelios.common.game.ability.condition.collision;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.game.ability.condition.ConditionCollision;
import com.aurelios.common.game.ability.enums.AbilityConditionType;
import com.aurelios.common.managers.Managers;
import com.aurelios.common.util.misc.Pair;

import java.util.List;
import java.util.Optional;

public abstract class CollisionAbility extends Collision<Double>{

    /**
     * The double is an "HP" value for the ability, so if it collides with another,
     * the ability with higher "HP" survives
     * @param value
     */

    public CollisionAbility(Ability container, Double value, boolean endAbilityHere) {
        super(container, endAbilityHere, CollisionTypes.ABILITY, value);
    }

    public void subtractHP(Double value) {
        this.value -= value;
    }

    @Override
    public boolean checkForCollisions(){
        List<Ability> nearby = Managers.ABILITY.getNearbyCollisionAbilities(container);

        for(Ability ability: nearby){
            Optional<Collision> hit = ((ConditionCollision)ability.getCondition(AbilityConditionType.COLLISION).get())
                    .getCollision(CollisionTypes.ABILITY);
            System.out.println("hit: " + hit.isPresent());
            if(hit.isPresent() && shape.collides(hit)){
                System.out.println("COLLIDES");
                if(!Managers.ABILITY_COLLISION.contains(container, ability)) {
                    Managers.ABILITY_COLLISION.add(new Pair<>(container, ability));
                }
            } else {
                nearby.remove(ability);
            }
        }

        if(nearby.size() > 0) {
            addCollisionReport(new CollisionAbility.AbilityHitReport(nearby));
            return true;
        } else return false;
    }

    @Override
    public boolean shouldEndAbility() {
        return value <= 0 ? true : endAbilityHere;
    }

    public static class AbilityHitReport extends CollisionReport<List<Ability>>{

        public AbilityHitReport(List<Ability> hit) {
            super(CollisionTypes.ABILITY, hit);
        }
    }
}
