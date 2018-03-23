package com.aurelios.common.util.particles.effects.options;

import com.aurelios.common.util.misc.Pair;
import com.flowpowered.math.TrigMath;

public class EffectOptionStaticRotation extends EffectOption<Pair<Double, Double>> implements EffectOptionRotation{

    private Pair<Double, Double> yAxis, zAxis;

    public EffectOptionStaticRotation(Pair<Double, Double> pitchAndYaw) {
        super(pitchAndYaw, EffectOptionTypes.STATIC_ROTATION);

        double yaw = getValue().getSecond();
        double yangle = Math.toRadians(yaw);
        yAxis = new Pair<>((double) TrigMath.cos(-yangle), (double)TrigMath.sin(-yangle));

        double pitch = getValue().getFirst();
        double zangle = Math.toRadians(pitch);
        zAxis = new Pair<>((double)TrigMath.cos(zangle), (double)TrigMath.sin(zangle));
    }

    public Pair<Double, Double> getYAxis(){
        return yAxis;
    }

    public Pair<Double, Double> getZAxis(){
        return zAxis;
    }
}
