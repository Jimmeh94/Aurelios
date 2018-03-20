package com.aurelios.game.environment.nodes.shapes;

import org.spongepowered.api.world.Location;

public interface Shape {

    boolean isWithin(Location location);

}
