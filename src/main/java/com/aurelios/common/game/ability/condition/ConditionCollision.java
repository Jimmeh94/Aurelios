package com.aurelios.common.game.ability.condition;

import com.aurelios.common.game.ability.condition.collision.Collision;
import com.aurelios.common.game.ability.condition.collision.shapes.CollisionShape;
import com.aurelios.common.game.ability.enums.AbilityConditionType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ConditionCollision extends AbilityCondition {

    private List<Collision> collisions;
    private boolean colliding = false;

    /**
     * The order in which Collision... collision is passed in determines priority of collision types
     * @param shape
     * @param collision
     */
    public ConditionCollision(CollisionShape shape, Collision... collision) {
        super(null);

        this.collisions = Arrays.asList(collision);
        for(Collision collision1: collisions){
            collision1.setShape(shape);
        }
        Sponge.getServer().shutdown();
    }

    @Override
    public boolean isComplete() {
        colliding = false;

        for(Collision collision: collisions){
            collision.update();
            if(collision.checkForCollisions()){
                System.out.println("Collision Check For Collisions was True");
                colliding = true;
                collision.doCollisionEffect();

                if(collision.shouldEndAbility())
                    return true;
            }
        }

        return false;
    }

    public boolean isColliding() {
        return colliding;
    }

    @Override
    public AbilityConditionType getConditionType() {
        return AbilityConditionType.COLLISION;
    }

    @Override
    public Text getChatRepresentation() {
        Text.Builder builder = Text.builder();
        builder.append(Text.of(TextColors.GRAY, "Collides with: "));

        for(Collision collision: collisions){
            if(collision.getType() == Collision.CollisionTypes.ABILITY){
                builder.append(Text.of(TextColors.GOLD, "abilities, "));
            } else if(collision.getType() == Collision.CollisionTypes.ENVIRONMENT){
                builder.append(Text.of(TextColors.GOLD, "environment, "));
            } else if(collision.getType() == Collision.CollisionTypes.USER){
                builder.append(Text.of(TextColors.GOLD, "users, "));
            }
        }
        return builder.build();
    }

    public List<Collision> getCollisions() {
        return collisions;
    }

    public Optional<Collision> getCollision(Collision.CollisionTypes type){
        for(Collision collision: collisions){
            if(collision.getType() == type){
                return Optional.of(collision);
            }
        }
        return Optional.empty();

    }
}
