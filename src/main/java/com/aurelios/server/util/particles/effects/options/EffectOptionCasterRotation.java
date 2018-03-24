package com.aurelios.server.util.particles.effects.options;

import com.aurelios.server.game.user.User;
import com.aurelios.server.util.misc.Pair;
import com.flowpowered.math.TrigMath;

public class EffectOptionCasterRotation extends EffectOption<User> implements EffectOptionRotation{

    public EffectOptionCasterRotation(User value) {
        super(value, EffectOptionTypes.ROTATION_CASTER);
    }

    public Pair<Double, Double> getYAxis(){
        double yaw = getValue().getEntity().getHeadRotation().getY();
        double yangle = Math.toRadians(yaw); // note that here we do have to convert to radians.

        return new Pair<>((double)TrigMath.cos(-yangle), (double)TrigMath.sin(-yangle));
    }

    public Pair<Double, Double> getZAxis(){
        double pitch = getValue().getEntity().getHeadRotation().getX();
        double zangle = Math.toRadians(pitch); // note that here we do have to convert to radians.

        return new Pair<>((double)TrigMath.cos(zangle), (double)TrigMath.sin(zangle));
    }
}
