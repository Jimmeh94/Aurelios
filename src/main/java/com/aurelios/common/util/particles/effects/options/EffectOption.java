package com.aurelios.common.util.particles.effects.options;

public class EffectOption<T> {

    private T value;
    private EffectOptionTypes type;

    public EffectOption(T value, EffectOptionTypes type) {
        this.value = value;
        this.type = type;
    }

    public EffectOptionTypes getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public enum EffectOptionTypes{
        ITERATE,
        ROTATION_CASTER, CENTER_FOLLOW_CASTER, ANIMATION_DATA, STATIC_ROTATION, GROW_WITH_ABILLITY, GROW_RADIUS, OFFSET, SLEEP, CHAIN_EFFECT,
    }
}
