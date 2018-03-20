package com.aurelios.util.particles.effects.options;

/**
 * Use this to "grow" the thickness of an effect
 */
public class EffectOptionGrowRadius extends EffectOption{

    private double startRadius, endRadius, step;

    public EffectOptionGrowRadius(double startRadius, double endRadius, double step) {
        super(null, EffectOptionTypes.GROW_RADIUS);

        this.startRadius = startRadius;
        this.endRadius = endRadius;
        this.step = step;
    }

    public double getStartRadius() {
        return startRadius;
    }

    public double getEndRadius() {
        return endRadius;
    }

    public double getStep() {
        return step;
    }
}
