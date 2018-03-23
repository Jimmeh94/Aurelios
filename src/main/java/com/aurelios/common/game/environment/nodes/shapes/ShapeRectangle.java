package com.aurelios.common.game.environment.nodes.shapes;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.world.Location;

public class ShapeRectangle implements Shape {

    private Vector3d left, right;

    public ShapeRectangle(Vector3d left, Vector3d right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isWithin(Location location) {
        Vector3d compare = location.getPosition();

        double lowX = Math.min(left.getX(), right.getX()),
                lowZ = Math.min(left.getZ(), right.getZ()),
                highX = Math.max(left.getZ(), right.getZ()),
                highZ = Math.max(left.getZ(), right.getZ()),
                lowY = Math.min(left.getY(), right.getY()),
                highY = Math.max(left.getY(), right.getY());

        return (lowX <= compare.getX() && highX >= compare.getX())
                && (lowY <= compare.getY() && highY >= compare.getY())
                && (lowZ <= compare.getZ() && highZ >= compare.getZ());
    }
    
    public String getBottomLeftAsString(){
        return String.valueOf(left.getX())
                + "," + String.valueOf(left.getY())
                + "," + String.valueOf(left.getZ());
    }

    public String getTopRightAsString(){
        return String.valueOf(right.getX())
        + "," + String.valueOf(right.getY())
        + "," + String.valueOf(right.getZ());
    }
}
