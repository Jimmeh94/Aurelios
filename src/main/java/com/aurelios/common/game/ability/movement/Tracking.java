package com.aurelios.common.game.ability.movement;

import com.aurelios.common.game.ability.Ability;
import com.flowpowered.math.vector.Vector3d;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is to track where the ability has been
 */
public class Tracking {

    private Vector3d center;
    private List<Vector3d> previousCenters;
    private Ability ability;

    public Tracking(Ability ability){
        this.ability = ability;
        previousCenters = new CopyOnWriteArrayList<>();
    }

    public void setCenter(Vector3d center) {
        if(this.center != null){
            previousCenters.add(0, this.center.clone());
        }

        this.center = center;
    }

    public List<Vector3d> getPreviousCenters() {
        return previousCenters;
    }

    public Vector3d getCenter() {
        return center;
    }

    public void reset() {
        center = null;
        previousCenters.clear();
    }

    public double getCurrentDistance(){
        if(center == null || previousCenters.size() == 0){
            return 0;
        } else return center.distance(previousCenters.get(previousCenters.size() - 1));
    }

    public int getTotalCenters(){
        return previousCenters.size();
    }
}
