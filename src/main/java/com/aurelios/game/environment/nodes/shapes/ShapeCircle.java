package com.aurelios.game.environment.nodes.shapes;

import com.aurelios.util.misc.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.world.Location;

public class ShapeCircle implements Shape {

    private Vector3d center;
    private double radius;
    private boolean ignoreY;

    public ShapeCircle(Vector3d center, double radius, boolean ignoreY) {
        this.center = center;
        this.radius = radius;
        this.ignoreY = ignoreY;
    }

    @Override
    public boolean isWithin(Location location) {
        if(ignoreY) {
            return LocationUtils.withinDistanceIgnoreY(center, location.getPosition(), radius);
        } else return LocationUtils.withinDistance(center, location.getPosition(), radius);
        //return center.getPosition().distance(location.getPosition()) <= radius;
    }

    public Vector3d getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public String getCenterAsString(){
        return String.valueOf(center.getX())
                + "," + String.valueOf(center.getY())
                + "," + String.valueOf(center.getZ());
    }

    public boolean ignoreY() {
        return ignoreY;
    }
}
