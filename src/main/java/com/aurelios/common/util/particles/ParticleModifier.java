package com.aurelios.common.util.particles;

/**
 * An option every player can set to increase or reduce the particle amounts they see.
 */
public enum ParticleModifier{
    LOW(0.25),
    MEDIUM(0.5),
    NORMAL(1.0),
    HIGH(1.25),
    EXTREME(1.5);

    private double factor;

    ParticleModifier(double factor){this.factor = factor;}

    public double getFactor() {
        return factor;
    }
}
