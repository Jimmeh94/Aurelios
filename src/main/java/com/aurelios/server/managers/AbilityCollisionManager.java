package com.aurelios.server.managers;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.condition.ConditionCollision;
import com.aurelios.server.game.ability.condition.collision.Collision;
import com.aurelios.server.game.ability.condition.collision.CollisionAbility;
import com.aurelios.server.game.ability.enums.AbilityConditionType;
import com.aurelios.server.util.misc.Pair;

/**
 * This manager is simply to handle ability-to-ability collisions.
 * The AbilityManager will update all abilities and the abilities with ability collision as a condition
 * will get added here paired with each ability it has collided into.
 * Once all abilities have been updated, all ability-to-ability collisions will be performed.
 */
public class AbilityCollisionManager extends Manager<Pair<Ability, Ability>> {

    public boolean contains(Ability a, Ability b){
        for(Pair pair: objects){
            if(pair.has(a) && pair.has(b)){
                return true;
            }
        }
        return false;
    }

    private void removePairsContaining(Ability ability){
        for(Pair<Ability, Ability> pair: objects){
            if(pair.has(ability)){
                objects.remove(pair);
            }
        }
    }


    public void performCollisions() {
        for(Pair<Ability, Ability> pair: objects){
            ConditionCollision first = ((ConditionCollision) pair.getFirst().getCondition(AbilityConditionType.COLLISION).get());
            ConditionCollision second = ((ConditionCollision) pair.getSecond().getCondition(AbilityConditionType.COLLISION).get());
            CollisionAbility firstCollision = (CollisionAbility) first.getCollision(Collision.CollisionTypes.ABILITY).get();
            CollisionAbility secondCollision = (CollisionAbility) second.getCollision(Collision.CollisionTypes.ABILITY).get();

            firstCollision.doCollisionEffect();
            secondCollision.doCollisionEffect();

            double hp = firstCollision.getValue();
            firstCollision.subtractHP(secondCollision.getValue());
            secondCollision.subtractHP(hp);

            if(firstCollision.shouldEndAbility()){
                firstCollision.getAbility().stop(AbilityConditionType.COLLISION);
                removePairsContaining(pair.getFirst());
            }

            if(secondCollision.shouldEndAbility()){
                secondCollision.getAbility().stop(AbilityConditionType.COLLISION);
                removePairsContaining(pair.getSecond());
            }
        }
        objects.clear();
    }
}
