package com.aurelios.server.game.environment.nodes;

import com.aurelios.server.game.environment.nodes.shapes.Shape;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public abstract class Area {

    private Text displayName;
    protected Shape shape;
    protected UUID uuid;

    public Area(Text displayName, Shape shape) {
        this.displayName = displayName;
        this.shape = shape;
        uuid = UUID.randomUUID();
    }

    public Area(Text displayName, Shape shape, UUID uuid) {
        this.displayName = displayName;
        this.shape = shape;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public Shape getShape() {
        return shape;
    }
}
