package com.aurelios.game.ability.condition.collision;

import com.aurelios.game.ability.Ability;
import com.aurelios.game.user.User;

import java.util.List;
import java.util.Optional;

public abstract class CollisionUser extends Collision<CollisionUser.UserCollisionTypes>{

    /**
     * This will mean the collision only detects the selected target found at
     * #Ability.getTargeting()
     */
    private boolean onlyHitTarget;

    public CollisionUser(Ability container, boolean endAbilityHere, boolean onlyHitTarget, UserCollisionTypes value) {
        super(container, endAbilityHere, CollisionTypes.USER, value);
        this.onlyHitTarget = onlyHitTarget;
    }

    @Override
    public boolean checkForCollisions() {
        return shape.collides(Optional.empty());
    }

    public enum UserCollisionTypes{
        ALLY,
        ENEMY,
        ANY
    }

    public boolean shouldOnlyHitTarget() {
        return onlyHitTarget;
    }

    public static class UserHitReport extends CollisionReport<List<User>>{

        public UserHitReport(List<User> hit) {
            super(CollisionTypes.USER, hit);

            for(User user: hit){
                user.endAllAbilities();
                user.getEntity().remove();
            }
        }
    }
}
