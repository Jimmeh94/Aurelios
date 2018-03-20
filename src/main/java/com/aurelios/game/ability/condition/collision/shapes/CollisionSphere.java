package com.aurelios.game.ability.condition.collision.shapes;

import com.aurelios.game.ability.Ability;
import com.aurelios.util.misc.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.util.Direction;

public class CollisionSphere extends CollisionShape {

    protected double radius;

    public CollisionSphere(Ability container, double radius) {
        super(container, radius);

        this.radius = radius;
    }

    @Override
    protected boolean checkCollisionWithSphere(CollisionSphere other) {
        System.out.println("distance: " + getCenter().distanceSquared(other.getCenter()) + " " + (other.radius + radius) * (other.radius + radius));
        if(LocationUtils.withinDistance(getCenter(), other.getCenter(), other.radius + radius)){
        //if(getCenter().distance(other.getCenter()) <= radius + other.radius){
            collisionLocation.add(getCenter());
            return true;
        }
        return false;
    }

    @Override
    protected boolean checkCollisionWithDome(CollisionDome other) {
        if(LocationUtils.withinDistance(other.getCenter(), getCenter(), radius)){
        //if(other.getCenter().distance(getCenter()) <= radius){
            collisionLocation.add(getCenter());
            return true;
        }

        if(LocationUtils.withinDistance(other.getCenter(), getCenter(), radius + other.radius)){
        //if(other.getCenter().distance(getCenter()) <= radius + other.radius){
            Direction direction = Direction.getClosest(LocationUtils.getDifferenceVector(getCenter(), other.getCenter()));

            if(other.check(direction)){
                collisionLocation.add(getCenter());
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean checkCollisionWithCylinder(CollisionCylinder other) {
        for(Vector3d vector3d: other.container.getTargeting().getTracking().getPreviousCenters()){
            if(LocationUtils.withinDistance(vector3d, getCenter(), other.radius + radius)){
            //if(vector3d.distance(getCenter()) <= other.radius + radius){
                if(LocationUtils.within180Degrees(container.getTargeting().getDirection(),
                        other.container.getTargeting().getDirection())){
                    collisionLocation.add(vector3d);
                    return true;
                }
            }
        }

        return false;
    }
}
