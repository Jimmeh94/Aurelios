package com.aurelios.server.game.ability.condition.collision.shapes;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.condition.collision.CollisionUser;
import com.aurelios.server.game.user.User;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.misc.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.util.Direction;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollisionCylinder extends CollisionShape  {

    /**
     * Length means how long the cylinder should be. Essentially meaning how long it should grow
     * This could be useful for a stream-type of attack
     */
    protected double radius;
    private int length;

    public CollisionCylinder(Ability container, double radius) {
        this(container, radius, -1);
    }

    public CollisionCylinder(Ability container, double radius, int length) {
        super(container, radius);
        this.radius = radius;
        this.length = length;
    }

    @Override
    protected boolean checkCollisionWithSphere(CollisionSphere other) {
        for(Vector3d vector3d: container.getTargeting().getTracking().getPreviousCenters()){
            //if(vector3d.distance(other.getCenter()) <= radius + other.radius){
            if(LocationUtils.withinDistance(vector3d, other.getCenter(), radius + other.radius)){
                //Making sure that the collision isn't taking place "behind" the cylinder
                if(LocationUtils.within180Degrees(container.getTargeting().getDirection(),
                        other.container.getTargeting().getDirection())){
                    collisionLocation.add(vector3d);
                    return true;
                }
            }
        }

        if(LocationUtils.withinDistance(other.getCenter(), getCenter(), radius + other.radius)){
        //if(other.getCenter().distance(getCenter()) <= radius + other.radius){
            //if(getCenter().distance(other.getCenter()) <= radius + other.radius){
                if(LocationUtils.within180Degrees(container.getTargeting().getDirection(),
                        other.container.getTargeting().getDirection())){
                    collisionLocation.add(getCenter());
                    return true;
                }
            //}
        }
        return false;
    }

    @Override
    protected boolean checkCollisionWithDome(CollisionDome other) {
        if(LocationUtils.withinDistance(other.getCenter(), getCenter(), radius)){
        //if(other.getCenter().distance(getCenter()) <= radius){
            collisionLocation.add(LocationUtils.getMidPointLocation(other.getCenter(), getCenter()));
            return true;
        }

        for(Vector3d location: container.getTargeting().getTracking().getPreviousCenters()){
            if(LocationUtils.withinDistance(location, other.getCenter(), other.radius)){
            //if(location.distance(other.getCenter()) <= other.radius){
                //So this is worth checking further
                //Get Direction based on difference vector between location and dome center
                //If Direction doesn't equal dome's traveling direction or a direction 90 degrees off it,
                //return false

                Direction difference = Direction.getClosest(LocationUtils.getDifferenceVector(location, other.getCenter()));

                if(other.check(difference)){
                    collisionLocation.add(location);
                    return true;
                }
            }
        }

        if(LocationUtils.withinDistance(getCenter(), other.getCenter(), radius + other.radius)){
        //if(getCenter().distance(other.getCenter()) <= radius + other.radius){
            Direction difference = Direction.getClosest(LocationUtils.getDifferenceVector(getCenter(), other.getCenter()));

            if(other.check(difference)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean checkCollisionWithCylinder(CollisionCylinder other) {
        for(Vector3d vector3d: container.getTargeting().getTracking().getPreviousCenters()){
            for(Vector3d vector3d1: other.container.getTargeting().getTracking().getPreviousCenters()){
                if(LocationUtils.withinDistance(vector3d, vector3d1, radius + other.radius)){
                //if(vector3d.distance(vector3d1) <= radius + other.radius){
                    collisionLocation.add(vector3d);
                    return true;
                }
            }

            if(LocationUtils.withinDistance(vector3d, other.getCenter(), radius + other.radius)){
            //if(vector3d.distance(other.getCenter()) <= radius + other.radius){
                collisionLocation.add(vector3d);
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean userCollision() {
        List<User> hit = new CopyOnWriteArrayList<>();
        Vector3d feet, torso, head;

        double tempRadius = 1.5;

        if(radius > 1.5){
            tempRadius = Math.sqrt(radius);
        }

        for(User user: Managers.USER.getObjects()){
            if(user == container.getCaster() || user.equals(container.getCaster())){
                continue;
            } else {
                feet = user.getEntity().getLocation().getPosition().clone();
                torso = feet.clone().add(0, 1, 0);
                head = torso.clone().add(0, 1, 0);

                for(Vector3d location: getAbility().getTargeting().getTracking().getPreviousCenters()){
                    if(LocationUtils.withinDistance(location, feet, tempRadius) ||
                            LocationUtils.withinDistance(location, torso, tempRadius) ||
                            LocationUtils.withinDistance(location, head, tempRadius)){
                        hit.add(user);
                        collisionLocation.add(location);
                    }
                }
            }

            if(((CollisionUser)collision).shouldOnlyHitTarget()){

            } else {

            }
        }

        if(hit.size() > 0){
            collision.addCollisionReport(new CollisionUser.UserHitReport(hit));
            return true;
        }
        return false;
    }
}
