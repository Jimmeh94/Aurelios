package com.aurelios.common.game.ability.condition.collision;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.game.ability.condition.collision.shapes.CollisionCylinder;
import com.aurelios.common.game.ability.condition.collision.shapes.CollisionDome;
import com.aurelios.common.util.misc.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class CollisionEnvironment extends Collision<List<BlockType>>{

    public static final BlockType[] AIR = new BlockType[]{BlockTypes.AIR};

    /**
     * These block types will not trigger collision
     * @param allowed
     */
    public CollisionEnvironment(Ability container, boolean endAbilityHere, BlockType... allowed){
        super(container, endAbilityHere, CollisionTypes.ENVIRONMENT, Arrays.asList(allowed));
    }

    public List<Vector3d> getAllLocs(){
        List<Vector3d> give = shape.getAllLocs();

        if(shape instanceof CollisionCylinder){
            for(Vector3d vector3d: container.getTargeting().getTracking().getPreviousCenters()){
                give.addAll(LocationUtils.getCube(new Vector3d(vector3d.getFloorX() - shape.getRadius(),
                                vector3d.getFloorY() - shape.getRadius(),
                                vector3d.getFloorZ() - shape.getRadius()),
                        new Vector3d(vector3d.getFloorX() + shape.getRadius(),
                                vector3d.getFloorY() + shape.getRadius(),
                                vector3d.getFloorZ() + shape.getRadius())));
            }
        } else if(shape instanceof CollisionDome){
            //remove all that are behind the dome
            for(Vector3d vector3d: give){
                Direction difference = Direction.getClosest(LocationUtils.getDifferenceVector(vector3d, center));
                if(!((CollisionDome)shape).check(difference)){
                    give.remove(vector3d);
                }
            }
        }

        return give;
    }

    @Override
    public boolean checkForCollisions() {
        return shape.collides(Optional.empty());
    }

    public static class EnvironmentHitReport extends CollisionReport<List<Vector3d>>{

        public EnvironmentHitReport(List<Vector3d> hit) {
            super(CollisionTypes.ENVIRONMENT, hit);
        }
    }
}
