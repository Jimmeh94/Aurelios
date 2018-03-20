package com.aurelios.game.ability.condition.collision.shapes;

import com.aurelios.game.ability.Ability;
import com.aurelios.util.misc.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.util.Direction;

public class CollisionDome extends CollisionSphere {

    /**
     * Should act just like sphere but only account for the blocks in the direction of travel
     * @param container
     * @param radius
     */

    public CollisionDome(Ability container, double radius) {
        super(container, radius);
    }

    /**
     * Return true if the direction passed in is the same as this ability's direction or within 90 degrees of it
     * @param difference
     * @return
     */
    public boolean check(Direction difference) {
        return LocationUtils.within180Degrees(container.getTargeting().getDirection(), difference);
    }

    @Override
    protected boolean checkCollisionWithSphere(CollisionSphere other) {

        /*if(getCenter().distance(other.getCenter()) <= other.radius){
            collisionLocation.add(LocationUtils.getMidPointLocation(getCenter(), other.getCenter()));
            return true;
        }*/

        if(LocationUtils.withinDistance(getCenter(), other.getCenter(), other.radius)){
            collisionLocation.add(LocationUtils.getMidPointLocation(getCenter(), other.getCenter()));
            return true;
        }

        //if(getCenter().distance(other.getCenter()) <= radius + other.radius){
        if(LocationUtils.withinDistance(getCenter(), other.getCenter(), radius + other.radius)){

            Direction direction = Direction.getClosest(LocationUtils.getDifferenceVector(other.getCenter(), getCenter()));

            if(check(direction)){
                collisionLocation.add(getCenter());
                return true;
            }

        }
        return false;
    }

    @Override
    protected boolean checkCollisionWithDome(CollisionDome other) {

        //if(getCenter().distance(other.getCenter()) <= other.radius){
        if(LocationUtils.withinDistance(getCenter(), other.getCenter(), other.radius)){
            return true;
        }

        //if(getCenter().distance(other.getCenter()) <= radius + other.radius){
        if(LocationUtils.withinDistance(getCenter(), other.getCenter(), radius + other.radius)){
            Direction direction = Direction.getClosest(LocationUtils.getDifferenceVector(other.getCenter(), getCenter()));

            if(check(direction)){
                collisionLocation.add(getCenter());
                return true;
            }

        }
        return false;
    }

    @Override
    protected boolean checkCollisionWithCylinder(CollisionCylinder other) {
        for(Vector3d vector3d: other.container.getTargeting().getTracking().getPreviousCenters()){

            //if(getCenter().distance(vector3d) <= other.radius){
            if(LocationUtils.withinDistance(getCenter(), vector3d, other.radius)){
                return true;
            }

            if(LocationUtils.withinDistance(getCenter(), vector3d, radius + other.radius)){
            //if(vector3d.distance(getCenter()) <= radius + other.radius){

                Direction direction = Direction.getClosest(LocationUtils.getDifferenceVector(vector3d, getCenter()));

                if(check(direction)){
                    collisionLocation.add(vector3d);
                    return true;
                }
            }
        }
        return false;
    }
}
