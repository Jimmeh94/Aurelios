package com.aurelios.server.game.ability.condition.collision.shapes;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.condition.collision.Collision;
import com.aurelios.server.game.ability.condition.collision.CollisionEnvironment;
import com.aurelios.server.game.ability.condition.collision.CollisionUser;
import com.aurelios.server.game.user.User;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.misc.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class CollisionShape {

    protected Ability container;
    protected Collision collision;
    protected double radius;
    protected List<Vector3d> collisionLocation;
    protected List<Vector3d> allLocs;

    public CollisionShape(Ability container, double radius) {
        this.container = container;
        this.radius = radius;
        collisionLocation = new CopyOnWriteArrayList<>();
    }

    protected abstract boolean checkCollisionWithSphere(CollisionSphere other);
    protected abstract boolean checkCollisionWithDome(CollisionDome other);
    protected abstract boolean checkCollisionWithCylinder(CollisionCylinder other);

    public void setCollision(Collision collision){this.collision = collision;}

    public Vector3d getCenter(){
        return container.getTargeting().getTracking().getCenter();
    }

    /**
     * Other should be empty unless it's checking for an ability-to-ability collision
     * @param other
     * @return
     */
    public boolean collides(Optional<Collision> other){
        allLocs = getAllLocs();

        if(this instanceof CollisionDome){
            for(Vector3d vector3d: allLocs){
                Direction difference = Direction.getClosest(LocationUtils.getDifferenceVector(vector3d,
                        container.getTargeting().getTracking().getCenter()));
                if(!((CollisionDome)collision.getShape()).check(difference)){
                    allLocs.remove(vector3d);
                }
            }
        }

        /*for(Vector3d vector3d: allLocs){
            Managers.BLOCK.getValue().add(new BlockManager.TemporaryBlock(vector3d, 1));
            Sponge.getServer().getWorld("world").get().getLocation(vector3d)
                    .setBlockType(BlockTypes.STONE, Cause.source(Aurelios.INSTANCE.PLUGIN_CONTAINER).build());
        }*/

        if(collision.getType() == Collision.CollisionTypes.ENVIRONMENT){
            System.out.println("Instance of Environment");
            return environmentCollision();
        } else if(collision.getType() == Collision.CollisionTypes.USER){
            System.out.println("Instance of User");
            return userCollision();
        } else if(collision.getType() == Collision.CollisionTypes.ABILITY){
            System.out.println("Instance of Ability");
            return abilityCollision(other);
        }

        return false;
    }

    protected boolean abilityCollision(Optional<Collision> other) {//This is ability collision
        System.out.println("ABILITY COLLISION CHECK");
        if(other.isPresent()){
            CollisionShape shape = other.get().getShape();

            if(shape instanceof CollisionCylinder){
                return checkCollisionWithCylinder((CollisionCylinder) shape);
            } else if(shape instanceof CollisionDome){
                return checkCollisionWithDome((CollisionDome) shape);
            } else if(shape instanceof CollisionSphere){
                return checkCollisionWithSphere((CollisionSphere) shape);
            }
        }
        return false;
    }

    protected boolean userCollision(){//Need to take into account the targeting as well
        List<User> hit = new CopyOnWriteArrayList<>();
        Vector3d feet, torso, head;

        for(User user: Managers.USER.getObjects()){
            if(user == container.getCaster() || user.equals(container.getCaster())){
                continue;
            } else {
                feet = user.getEntity().getLocation().getPosition().clone();
                torso = feet.clone().add(0, 1, 0);
                head = torso.clone().add(0, 1, 0);

                for(Vector3d location: allLocs){
                    if(LocationUtils.withinDistance(location, feet, 1.22) ||
                            LocationUtils.withinDistance(location, torso, 1.22) ||
                            LocationUtils.withinDistance(location, head, 1.22)){
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

    protected boolean environmentCollision(){
        boolean give = false;
        List<BlockType> types = ((CollisionEnvironment)collision).getValue();
        for(Vector3d v: allLocs){
            Location location = Sponge.getServer().getWorld("world").get().getLocation(v);
            if(!types.contains(location.getBlockType())){
                collisionLocation.add(location.getPosition());
                give = true;
            } else {
                allLocs.remove(location);
            }
        }

        if(give){
            collision.addCollisionReport(new CollisionEnvironment.EnvironmentHitReport(allLocs));
        }

        return give;
    }

    public void resetCollisionLocations() {
        collisionLocation.clear();
    }

    public List<Vector3d> getCollisionLocations() {
        return collisionLocation;
    }

    public double getRadius() {
        return radius;
    }

    public List<Vector3d> getAllLocs(){
        Vector3d center = getCenter();
        return LocationUtils.getCube(new Vector3d(center.getFloorX() - radius,
                        center.getFloorY() - radius,
                        center.getFloorZ() - radius),
                new Vector3d(center.getFloorX() + radius,
                        center.getFloorY() + radius,
                        center.getFloorZ() + radius));
    }

    public Ability getAbility() {
        return container;
    }
}
