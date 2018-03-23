package com.aurelios.common.game.ability.condition.collision;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.game.ability.condition.collision.shapes.CollisionShape;
import com.aurelios.common.util.particles.ParticleData;
import com.aurelios.common.util.particles.ParticlePlayer;
import com.flowpowered.math.vector.Vector3d;

import java.util.Optional;

public abstract class Collision<T> {

    protected CollisionShape shape;
    protected Vector3d center;
    protected Ability container;
    protected Optional<CollisionReport> collisionReport = Optional.empty();
    protected T value;
    /**
     * If true, ability will be stopped on this collision
     * If false, collision will still be detected but won't stop ability
     */
    protected boolean endAbilityHere;
    protected CollisionTypes type;

    public abstract boolean checkForCollisions();
    protected abstract void doEffect();

    public Collision(Ability container, boolean endAbilityHere, CollisionTypes type, T value) {
        this.container = container;
        this.value = value;
        this.type = type;
        this.endAbilityHere = endAbilityHere;
    }

    public void setShape(CollisionShape shape) {
        this.shape = shape;
        this.shape.setCollision(this);
    }

    public void update(){
        center = container.getTargeting().getTracking().getCenter().clone();
        collisionReport = Optional.empty();
    }

    protected void playParticlesAtCollisionPoints(ParticleData data){
        for(Vector3d vector3d: shape.getCollisionLocations()){
            if(data != null){
                data.setCenter(vector3d);
                ParticlePlayer.display(data);
            }
        }
    }

    public void doCollisionEffect(){
        doEffect();
        shape.resetCollisionLocations();
    }

    public void addCollisionReport(CollisionReport report){
        collisionReport = Optional.of(report);
    }

    public Optional<CollisionReport> getCollisionReport() {
        return collisionReport;
    }

    public CollisionShape getShape() {
        return shape;
    }

    public T getValue() {
        return value;
    }

    public CollisionTypes getType() {
        return type;
    }

    public Ability getAbility() {
        return container;
    }

    public boolean shouldEndAbility() {
        return endAbilityHere;
    }

    public enum CollisionTypes{
        USER,
        ABILITY,
        ENVIRONMENT
    }

    public static abstract class CollisionReport<T>{

        private CollisionTypes type;
        private T hit;

        public CollisionReport(CollisionTypes type, T hit) {
            this.type = type;
            this.hit = hit;
        }

        public CollisionTypes getType() {
            return type;
        }

        public T getHit() {
            return hit;
        }
    }

}
