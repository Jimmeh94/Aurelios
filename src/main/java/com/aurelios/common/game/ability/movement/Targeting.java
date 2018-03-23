package com.aurelios.common.game.ability.movement;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.game.user.User;
import com.aurelios.common.util.misc.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;

public abstract class Targeting<T>{

        protected T target;
        protected Ability parent;
        protected Tracking tracking;
        protected Direction direction;

        //determines how many blocks the ability moves per update
        protected double movementScale = 1.0;

        private boolean endAbilityHere;
        /**
         * This is in case endAbilityHere is false but you still want to play the effect on target reached
         */
        private boolean playEffectAtTarget;

        public Targeting(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget){
            this(parent, endAbilityHere, playEffectAtTarget, 1.0);
        }

        public Targeting(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget, double movementScale) {
            this.parent = parent;
            tracking = new Tracking(parent);
            this.movementScale = movementScale;
            this.endAbilityHere = endAbilityHere;
            this.playEffectAtTarget = playEffectAtTarget;
        }

        public abstract void findTarget();
        public abstract Text getChatRepresentation();
        public abstract void onReachTarget();

        public T getTarget(){
            return target;
        }

        public Vector3d getTargetLocation(){
            if(target instanceof Vector3d){
                return (Vector3d) target;
            } else if(target instanceof User){
                return ((User)target).getEntity().getLocation().getPosition().clone().add(0, 1, 0);
            } else return null;
        }

        public void removeTarget(){
            target = null;
        }

        public Tracking getTracking() {
            return tracking;
        }

        public void update(){
            if(hasReachedTarget()){
                return;
            }

            Vector3d temp = LocationUtils.getNextLocation(tracking.getCenter(), getTargetLocation(), movementScale);
            if(temp == null){
                if(tracking.getCenter() != null){
                    temp = tracking.getCenter();
                }
            }
            tracking.setCenter(temp);

            if(tracking.getPreviousCenters().size() > 0) {
                direction = Direction.getClosest(LocationUtils
                        .getDifferenceVector(tracking.getCenter(), tracking.getPreviousCenters().get(0)));
            }
        }

        public Direction getDirection() {
            return direction;
        }

        public double getMovementScale() {
            return movementScale;
        }

        public void determineTargets(){
            getTracking().reset();
            findTarget();
        }

        public boolean hasReachedTarget() {
            return getTracking().getCenter() != null &&
                    LocationUtils.withinDistance(getTargetLocation(), getTracking().getCenter(), 1);
            //getTargetLocation().distance(getTracking().getCenter()) <= 0.3;
        }

        public boolean shouldEndAbilityHere() {
            return endAbilityHere;
        }

        public boolean shouldPlayEffectAtTarget() {
            return playEffectAtTarget;
        }

        /**
         * Targeting types:
         * 1) Self
         * 2) Single (Ally, Enemy, Both)
         * 3) Group (Ally, Enemy, Both)
         * 4) Location
         */

        public enum TargetingFlag{
            ALLY,
            ENEMY,
            EITHER
        }
}
