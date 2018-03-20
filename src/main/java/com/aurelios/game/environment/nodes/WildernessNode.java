package com.aurelios.game.environment.nodes;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.Optional;

public class WildernessNode extends Node {

    public WildernessNode(Text displayName) {
        super(null, displayName, null, -1);
    }

    @Override
    public Optional<Area> getContainingArea(Entity p) {
        return Optional.of(this);
    }

    @Override
    public boolean isWithin(Entity player) {
        return true;
    }

    @Override
    public boolean isWithin(Location location) {
        return true;
    }

    @Override
    public Optional<Area> getContainingArea(Location location) {
        return Optional.of(this);
    }

    @Override
    public void addChild(PointOfInterest poi) {

    }
}
