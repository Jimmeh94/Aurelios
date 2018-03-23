package com.aurelios.common.event.custom;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

public class CustomEvent extends AbstractEvent {

    protected Cause cause;

    public CustomEvent(Cause cause){this.cause = cause;}

    @Override
    public Cause getCause() {
        return cause;
    }
}
