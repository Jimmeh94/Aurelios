package com.aurelios.util.particles;

import com.aurelios.game.user.User;
import com.aurelios.game.user.UserPlayer;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOption;

import java.util.Iterator;
import java.util.Map;

public class ParticlePlayer {

    public static void display(ParticleData data){
        ParticleEffect.Builder builder = ParticleEffect.builder();

        builder.type(data.getType());
        builder.offset(data.getOffsetVector());

        if(data.getVelocity().isPresent()){
            builder.velocity(data.getVelocity().get());
        }

        if(data.getParticleOptions().isPresent()){
            Iterator<Map.Entry<ParticleOption, Object>> iterator = data.getParticleOptions().get().entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<ParticleOption, Object> pair = iterator.next();
                builder.option(pair.getKey(), pair.getValue());
            }
        }

        for(UserPlayer user: data.getViewers()){
            user.getPlayer().spawnParticles(builder.quantity((int) (data.getQuantity() * user.getPlayerKeys().MISC_PARTICLE_MODIFIER.getValue().getFactor())).build(), data.getDisplayAt());
        }
    }

}
