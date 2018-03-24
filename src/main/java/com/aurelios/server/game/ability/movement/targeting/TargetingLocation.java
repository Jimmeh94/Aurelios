package com.aurelios.server.game.ability.movement.targeting;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.movement.Targeting;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.World;

import java.util.Optional;

/**
 * This is a single location
 */
public abstract class TargetingLocation extends Targeting<Vector3d> {

    protected double distance;
    //if true, the target location will not update based on the User's current rotation
    //if false, the target location will update: i.e. dragging a beam around
    protected boolean staticLocation;

    public TargetingLocation(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget, double distance, boolean staticLocation) {
        super(parent, endAbilityHere, playEffectAtTarget);
        this.distance = distance;
        this.staticLocation = staticLocation;
    }

    public TargetingLocation(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget, double distance,
                    boolean staticLocation, double movementScale) {
        super(parent, endAbilityHere, playEffectAtTarget, movementScale);
        this.distance = distance;
        this.staticLocation = staticLocation;
    }

    @Override
    public void findTarget() {
        BlockRay<World> blockRay = BlockRay.from(parent.getCaster().getEntity()).distanceLimit(distance)
                .stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1)).build();
        Optional<BlockRayHit<World>> hitOpt = blockRay.end();
        if(hitOpt.isPresent()){
            target = hitOpt.get().getPosition().clone();
        }
    }

    @Override
    public void update() {
        if(staticLocation) {
            super.update();
        } else{
            int distanceCurrentlyTraveled = getTracking().getTotalCenters();

            tracking.reset();
            findTarget();

            tracking.setCenter(parent.getCaster().getEntity().getLocation().getPosition()
                    .clone().add(0, 1, 0));

            //This will make sure the ability isn't starting out with 0 distance again
            for(int a = 0; a <= distanceCurrentlyTraveled && !parent.getTargeting().hasReachedTarget(); a++){
                super.update();
            }
        }
    }

    @Override
    public Text getChatRepresentation() {
        return Text.of(TextColors.GOLD, "Location - " + distance + " blocks");
    }
}
