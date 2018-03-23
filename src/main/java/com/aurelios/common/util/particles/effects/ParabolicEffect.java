package com.aurelios.common.util.particles.effects;

import com.aurelios.common.util.misc.Pair;
import com.aurelios.common.util.particles.ParticleData;
import com.aurelios.common.util.particles.ParticlePlayer;
import com.aurelios.common.util.particles.effects.options.*;
import com.flowpowered.math.vector.Vector3d;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParabolicEffect extends AbstractEffect {

    private double height;
    private int amount, current = 0;

    private List<Vector3d> centers = new CopyOnWriteArrayList<>();

    public ParabolicEffect(ParticleData effectData, double height, int amount, EffectOption... options) {
        super(effectData, options);
        this.height = height;
        this.amount = amount;
        init(null);
    }

    private void init(EffectOptionRotation option){
        Vector3d targetVector = new Vector3d(effectData.getCenter().add(11, 0, 11));
        Vector3d to = targetVector.sub(effectData.getCenter());

        if(getOption(EffectOption.EffectOptionTypes.ITERATE).isPresent()){
            current++;
            if(current > amount){
                current = amount;
            }
        } else current = amount;

        for(int t = 0; t < current; t++) {
            float length = (float) to.length(); //length of the current vector
            float pitch = (float) (4 * height / Math.pow(length, 2));
            Vector3d v = to.clone().normalize().mul(length * t / 100);
            float x = ((float) t / 100) * length - length / 2;
            float y = (float) (-pitch * Math.pow(x, 2) + height);

            if(option != null){
                Pair<Double, Double> yAxis = option.getYAxis();
                Pair<Double, Double> zAxis = option.getZAxis();
                Vector3d temp = effectData.getCenter().clone().add(v).add(0, y, 0);

                centers.add(rotateAroundAxisX(temp, zAxis.getFirst(), zAxis.getSecond()));
            } else centers.add(effectData.getCenter().clone().add(v).add(0, y, 0));
        }
    }

    @Override
    public void prePlay(){
        Optional<EffectOption> option = getOption(EffectOption.EffectOptionTypes.ROTATION_CASTER);
        if(option.isPresent()){
            init(((EffectOptionCasterRotation)option.get()));
        } else {
            option = getOption(EffectOption.EffectOptionTypes.STATIC_ROTATION);
            if(option.isPresent()){
                init(((EffectOptionStaticRotation)option.get()));
            } else {
                option = getOption(EffectOption.EffectOptionTypes.ITERATE);
                if(option.isPresent()){
                    init(null);
                }
            }
        }
        play();
    }

    @Override
    protected void play() {
        for(Vector3d vector3d: centers){
            effectData.setDisplayAt(vector3d);
            ParticlePlayer.display(effectData);
        }
    }
}
