package com.aurelios.util.misc;

import com.flowpowered.math.TrigMath;
import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocationUtils {

    private static List<Direction> directions;

    static {
        directions = new ArrayList<>();
        Collections.addAll(directions, Direction.values());
        directions.remove(Direction.UP);
        directions.remove(Direction.DOWN);
        directions.remove(Direction.NONE);
    }

    /**
     * Degrees must be a multiple of 22.5
     * @param current
     * @param degrees
     * @return
     */
    public static Direction getNextDirection(Direction current, double degrees){
        int index = directions.indexOf(current);
        int iterations = (int) (degrees/22.5);
        int newIndex = index + iterations;

        if(newIndex < 0){
            newIndex += 12;
        } else if(newIndex >= directions.size()){
            newIndex -= directions.size();
        }

        return directions.get(newIndex);
    }

    /**
     * Checking to make sure "check" is within 180 degrees of where "source" is heading
     * @param source
     * @param check
     * @return
     */
    public static boolean within180Degrees(Direction source, Direction check) {

        Direction left, left_leftMid, leftMid, mid_midLeft, mid_midRight, rightMid, right_rightMid, right;

        left = LocationUtils.getNextDirection(source, -90);
        left_leftMid = LocationUtils.getNextDirection(source, -67.5);
        leftMid = LocationUtils.getNextDirection(source, -45);
        mid_midLeft = LocationUtils.getNextDirection(source, -22.5);
        mid_midRight = LocationUtils.getNextDirection(source, 22.5);
        rightMid = LocationUtils.getNextDirection(source, 45);
        right_rightMid = LocationUtils.getNextDirection(source, 67.5);
        right = LocationUtils.getNextDirection(source, 90);


        return check == source || check == left || check == left_leftMid
                || check == leftMid || check == mid_midLeft || check == mid_midRight
                || check == rightMid || check == right_rightMid || check == right;
    }

    public static boolean withinDistance(Vector3d one, Vector3d two, double radius){
        return one.distanceSquared(two) <= (radius * radius);
    }

    /**
     * x = pitch
     * y = yaw
     * z = roll (will always be 0 because MC doesn't support head roll)
     * @param entity
     * @return
     */
    public static Direction getRotation(Living entity){
        Vector3d rotation = entity.getHeadRotation();
        return Direction.getClosest(Quaterniond.fromAxesAnglesDeg(rotation.getX(), -rotation.getY(), rotation.getZ()).getDirection());
    }

    public static Vector3d getEyeHeight(Entity entity){
        return entity.getLocation().getPosition().clone().add(0, 1.75, 0);
    }

    //the next step towards a certain target, adjusted by a scale
    public static Vector3d getNextLocation(Vector3d start, Vector3d end, double scale){
        if(start == null || end == null){
            return null;
        }

        return start.add(end.sub(start).normalize().mul(scale));
    }

    public static Vector3d getOffsetBetween(Location start, Location end){
        double deltaX, deltaY, deltaZ;
        deltaX = Math.max(start.getX(), end.getX()) - Math.min(start.getX(), end.getX());
        if(deltaX > 0)
            deltaX = Math.max(start.getX(), end.getX()) == start.getX() ? deltaX *-1 : deltaX * 1;

        deltaY = Math.max(start.getY(), end.getY()) - Math.min(start.getY(), end.getY());
        if(deltaY > 0)
            deltaY = Math.max(start.getY(), end.getY()) == start.getY() ? deltaY *-1 : deltaY * 1;

        deltaZ = Math.max(start.getZ(), end.getZ()) - Math.min(start.getZ(), end.getZ());
        if(deltaZ > 0)
            deltaZ = Math.max(start.getZ(), end.getZ()) == start.getX() ? deltaZ *-1 : deltaZ * 1;

        return new Vector3d(deltaX, deltaY, deltaZ);
    }

    public static double getDistance(double a, double b){
        return Math.abs(a - b);
    }

    public static List<Vector3i> getChunksBetween(Vector3i start, Vector3i end) {
        List<Vector3i> give = new ArrayList<>();

        for (int x = start.getX(); x <= end.getX(); x++) {
            for (int z = start.getZ(); z <= end.getZ(); z++) {
                Vector3i result = chunkPositionFromWorldPosition(x, z);

                if (!give.contains(result)) {
                    give.add(result);
                }
            }
        }

        return give;
    }

    public static long chunkPositionToLong(Vector3i position) {
        return (0xFFFFFFFFL & position.getX()) << 32 + (0xFFFFFFFFL & position.getZ());
    }

    public static Vector3i chunkPositionFromWorldPosition(int x, int z){
        return Vector3i.from(x >> 4, 0, z >> 4);
    }

    public static List<Vector3d> getConnectingLine(Vector3d start, Vector3d end, double scale){
        List<Vector3d> give = new ArrayList<>();
        give.add(start);

        while(give.get(give.size() - 1) != end){
            give.add(getNextLocation(give.get(give.size() - 1), end, scale));
        }

        return give;
    }

    //Should be a 1 block thick ring around the outside of the circle
    /*
    Given a radius length r and an angle t in radians and a circle's center (h,k),
    you can calculate the coordinates of a point on the circumference as follows
     */
    public static List<Vector3d> getCircleOutline(Vector3d center, double radius){
        List<Vector3d> threshold = new ArrayList<>();
        Vector3d copy = center.clone();

        for(int i = 0; i < 360; i++){
            //2 radians = 360 degrees
            double t = i * (TrigMath.PI / 180);
            double x = radius * TrigMath.cos(t) + copy.getX();
            double z = radius * TrigMath.sin(t) + copy.getZ();
            Vector3d add = new Vector3d(x, Math.floor(copy.getY()), z);
            threshold.add(add);
        }
        return threshold;
    }

    public static List<Vector3d> getCube(Vector3d first, Vector3d second){
        List<Vector3d> give = new CopyOnWriteArrayList<>();

        int minX = Math.min(first.getFloorX(), second.getFloorX()), maxX = Math.max(first.getFloorX(), second.getFloorX());
        int minY = Math.min(first.getFloorY(), second.getFloorY()), maxY = Math.max(first.getFloorY(), second.getFloorY());
        int minZ = Math.min(first.getFloorZ(), second.getFloorZ()), maxZ = Math.max(first.getFloorZ(), second.getFloorZ());

        for(int y = minY; y <= maxY; y++){
            for(int z = minZ; z <= maxZ; z++){
                for(int x = minX; x <= maxX; x++){
                    give.add(new Vector3d(x, y, z));
                }
            }
        }

        return give;
    }

    public static List<Location> getSquareOutline(Vector3d first, Vector3d second){
        World world = Sponge.getServer().getWorld("world").get();
        return getSquareOutline(world.getLocation(first.getX(), first.getY(), first.getZ()), world.getLocation(second.getX(), second.getY(), second.getZ()));
    }

    public static List<Location> getSquareOutline(Location first, Location second){
        List<Location> threshold = new ArrayList<>();

        int xCoefficient = first.getBlockX() > second.getBlockX() ? -1 : 1, zCoefficient = first.getBlockZ() > second.getBlockZ() ? -1 : 1;
        int deltaX = Math.max(first.getBlockX(), second.getBlockX()) - Math.min(first.getBlockX(), second.getBlockX());
        int deltaZ = Math.max(first.getBlockZ(), second.getBlockZ()) - Math.min(first.getBlockZ(), second.getBlockZ());

        Location current = first.copy();
        threshold.add(current);

        for(int i = 0; i < deltaX; i++){
            current = current.add(xCoefficient, 0, 0);
            threshold.add(current);
        }
        for(int i = 0; i < deltaZ; i++){
            current = current.add(0, 0, zCoefficient);
            threshold.add(current);
        }

        xCoefficient *= -1;
        zCoefficient *= -1;
        for(int i = 0; i < deltaX; i++){
            current = current.add(xCoefficient, 0, 0);
            threshold.add(current);
        }
        for(int i = 0; i < deltaZ; i++){
            current = current.add(0, 0, zCoefficient);
            threshold.add(current);
        }

        threshold.add(second);

        return threshold;
    }

    public static Vector3d getMidPointLocation(Vector3d start, Vector3d end){
        Double[] delta = new Double[]{Math.abs(start.getX() - end.getX()),
                Math.abs(start.getY() - end.getY()),
                Math.abs(start.getZ() - end.getZ())};
        Double[] xyz = new Double[3];
        Double[] startXYZ = new Double[]{start.getX(), start.getY(), start.getZ()};
        Double[] endXYZ = new Double[]{end.getX(), end.getY(), end.getZ()};

        //3 just to cycle through the x, y and z coord
        for(int i = 0; i < 3; i++){
            if(startXYZ[i] > endXYZ[i]){
                xyz[i] = startXYZ[i] - delta[i]/2;
            } else if(startXYZ[i] < endXYZ[i]){
                xyz[i] = startXYZ[i] + delta[i]/2;
            } else xyz[i] = startXYZ[i];
        }

        return new Vector3d(xyz[0], xyz[1], xyz[2]);
    }

    /**
     * Trace a "trail" from one block start point to another, also applying noise to that trail
     * Useful for things like lightning
     * @param start
     * @param end
     * @param noise
     * @return
     */
    public static List<Vector3d> traceInBlock(Vector3d start, Vector3d end, Noise noise){

        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double dz = end.getZ() - start.getZ();
        List<Vector3d> list = new ArrayList<>();
        list.add(start);

        int midAmount = (int) (noise.getRandomScale() * 10);
        boolean getMidClosetToStart = true;

        //want noise on both axes that aren't biggest
        //get midpoint of both of those, the midpoint between those, then apply noise

        if(dx == dy && dy == dz){
            //need to alter 2 of these values to apply noise
            Random random = new Random();
            int choice1 = random.nextInt(3), choice2;
            Double[] temp = new Double[]{dx, dy, dz};

            temp[choice1] *= 0;

            choice2 = random.nextInt(3);
            while(choice2 == choice1){
                choice2 = random.nextInt(3);
            }

            temp[choice2] /= 2;
        }

        if(dx > dy && dx > dz){
            //noise to be on y and z axes
            //how many midpoints, theoretically the more midpoints the more noise
            for(int i = 0; i < midAmount; i++){ //0 - 4 at max
                //first midpoint in the direct center
                if(i == 0){
                    list.add(getMidPointLocation(start, end).add(0, noise.getRandomScale() * getRandomNegOrPos(), noise.getRandomScale() * getRandomNegOrPos()));
                } else {
                    //next midpoint needs to be closer to the start
                    if(getMidClosetToStart){
                        list.add(getMidPointLocation(start, list.get(list.size() - 1))
                                .add(0, noise.getRandomScale() * getRandomNegOrPos(), noise.getRandomScale() * getRandomNegOrPos()));
                        getMidClosetToStart = false;
                    } else {
                        //next midpoint needs to be closer to the end
                        list.add(getMidPointLocation(list.get(list.size() - 1), end)
                                .add(0, noise.getRandomScale() * getRandomNegOrPos(), noise.getRandomScale() * getRandomNegOrPos()));
                        getMidClosetToStart = true;
                    }
                }
            }

        } else if(dy > dx && dy > dz){
            //noise to be on x and z axes
            //how many midpoints, theoretically the more midpoints the more noise
            for(int i = 0; i < midAmount; i++){ //0 - 4 at max
                //first midpoint in the direct center
                if(i == 0){
                    list.add(getMidPointLocation(start, end).add(noise.getRandomScale() * getRandomNegOrPos(), 0, noise.getRandomScale() * getRandomNegOrPos()));
                } else {
                    //next midpoint needs to be closer to the start
                    if(getMidClosetToStart){
                        list.add(getMidPointLocation(start, list.get(list.size() - 1))
                                .add(noise.getRandomScale() * getRandomNegOrPos(), 0, noise.getRandomScale() * getRandomNegOrPos()));
                        getMidClosetToStart = false;
                    } else {
                        //next midpoint needs to be closer to the end
                        list.add(getMidPointLocation(list.get(list.size() - 1), end)
                                .add(noise.getRandomScale() * getRandomNegOrPos(), 0, noise.getRandomScale() * getRandomNegOrPos()));
                        getMidClosetToStart = true;
                    }
                }
            }
        } else if(dz > dx && dz > dy){
            //noise to be on x and y axes
            //how many midpoints, theoretically the more midpoints the more noise
            for(int i = 0; i < midAmount; i++){ //0 - 4 at max
                //first midpoint in the direct center
                if(i == 0){
                    list.add(getMidPointLocation(start, end).add(noise.getRandomScale() * getRandomNegOrPos(), noise.getRandomScale() * getRandomNegOrPos(), 0));
                } else {
                    //next midpoint needs to be closer to the start
                    if(getMidClosetToStart){
                        list.add(getMidPointLocation(start, list.get(list.size() - 1))
                                .add(noise.getRandomScale() * getRandomNegOrPos(), noise.getRandomScale() * getRandomNegOrPos(), 0));
                        getMidClosetToStart = false;
                    } else {
                        //next midpoint needs to be closer to the end
                        list.add(getMidPointLocation(list.get(list.size() - 1), end)
                                .add(noise.getRandomScale() * getRandomNegOrPos(), noise.getRandomScale() * getRandomNegOrPos(), 0));
                        getMidClosetToStart = true;
                    }
                }
            }
        }

        return list;
    }

    public static int getRandomNegOrPos(){
        int give = (new Random()).nextInt(2) == 0 ? -1 : 1;
        return give;
    }

    /**
     * Simply returns a vector based on what direction the new vector has moved vs the old vector.
     * @param center
     * @param old
     * @return
     */
    public static Vector3d getDifferenceVector(Vector3d center, Vector3d old) {
        double x, y, z;

        if(center.getFloorX() > old.getFloorX()){
            x = 1;
        } else if(center.getFloorX() < old.getFloorX()){
            x = -1;
        } else x = 0;

        if(center.getFloorY() > old.getFloorY()){
            y = 1;
        } else if(center.getFloorY() < old.getFloorY()){
            y = -1;
        } else y = 0;

        if(center.getFloorZ() > old.getFloorZ()){
            z = 1;
        } else if(center.getFloorZ() < old.getFloorZ()){
            z = -1;
        } else z = 0;

        return new Vector3d(x, y, z);
    }

    public static boolean withinDistanceIgnoreY(Vector3d center, Vector3d position, double radius) {
        return withinDistance(new Vector3d(center.getX(), 0, center.getZ()), new Vector3d(position.getX(), 0, position.getZ()), radius);
    }

    public enum Noise{

        HIGH(0.5),
        MEDIUM(0.3),
        NONE(0.0);

        private double scale;

        Noise(double scale){this.scale = scale;}

        public double getMaxScale() {
            return scale;
        }

        public double getRandomScale(){
            Random random = new Random();
            double temp = (temp = random.nextInt(3) / 10) + (temp == 0 ? 0.1 : 0);
            switch (this){
                case HIGH: return temp + MEDIUM.getMaxScale();
                case MEDIUM: return temp + NONE.getMaxScale();
                default: return 0.0;
            }
        }
    }

}
