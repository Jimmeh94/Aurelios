package com.aurelios.server.game.environment.nodes;

import com.aurelios.server.game.environment.nodes.shapes.Shape;
import com.aurelios.server.game.user.ai.NPC;
import com.aurelios.server.game.user.ai.traits.Role;
import com.aurelios.server.managers.Managers;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A city/area in the world
 */

public class Node extends Area{

    protected List<PointOfInterest> children = new CopyOnWriteArrayList<>();

    //This is the amount of AI that will ideally be alive in this node at one time
    private int aiCap;



    //protected abstract void loadChildren();

    public Node(UUID uuid, Text displayName, Shape shape, int aiCap, PointOfInterest... children) {
        super(displayName, shape, uuid);
        this.aiCap = aiCap;

        if(children != null){
            Collections.addAll(this.children, children);
        }

        //this.children.add(new PointOfInterest(Text.of("POI One"), new ShapeCircle(new Vector3d(0, 0, 0), 15), this));

      //  loadChildren();
    }

    public Optional<Area> getContainingArea(Entity p){
        return getContainingArea(p.getLocation());
    }

    public boolean isWithin(Entity player) {
        return isWithin(player.getLocation());
    }

    public boolean isWithin(Location location){
        if (shape.isWithin(location)) {
            return true;
        } else {
            for (PointOfInterest area : children) {
                if (area.getShape().isWithin(location)) {
                    return true;
                }
            }
            return false;
        }
    }

    public Optional<Area> getContainingArea(Location location) {
        Optional<Area> give = Optional.empty();

        if(shape.isWithin(location)){
            give = Optional.of(this);

            /**
             * This will narrow down the area to the "smallest" child
             */
            for(PointOfInterest area: children){
                if(area.getShape().isWithin(location)){
                    return Optional.of(area);
                }
            }
        }

        return give;
    }

    public List<PointOfInterest> getChildren() {
        return children;
    }

    public void addChild(PointOfInterest poi) {
        children.add(poi);
    }

    public void tick(){
        int alive = 0;
        for(NPC npc: Managers.AI.getObjects()){
            if(npc.getMetaData().getHome() == this){
                alive++;
            }
        }
        if(alive < aiCap){
            do {
                Managers.AI.createNPC(this);
                alive++;
            } while(alive < aiCap);
        }
    }

    public int getAiCap() {
        return aiCap;
    }

    public List<PointOfInterest> getPOIsWithRole(Role role) {
        List<PointOfInterest> possible = new ArrayList<>();
        for(PointOfInterest child: children){
            if(child.hasRole(role)){
                possible.add(child);
            }
        }
        return possible;
    }

    public void displayNPCMenu(Player player){

    }
}
