package com.aurelios.commands;

import com.aurelios.Aurelios;
import com.aurelios.managers.Managers;
import com.aurelios.util.particles.AnimationData;
import com.aurelios.util.particles.ParticleData;
import com.aurelios.util.particles.PreloadedParticle;
import com.aurelios.util.particles.effects.*;
import com.aurelios.util.particles.effects.options.EffectOptionAnimationData;
import com.aurelios.util.particles.effects.options.EffectOptionCasterRotation;
import com.aurelios.util.particles.effects.options.EffectOptionIterate;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Color;

public class ParticleEffectCommands implements CommandExecutor{

    public ParticleEffectCommands(){
        //particle effectName
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("Play a particle effect"))
                .executor(this)
                .arguments(GenericArguments.string(Text.of("effect")))
                .build();
        Sponge.getCommandManager().register(Aurelios.INSTANCE, commandSpec, "particle");
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        Player player = (Player) commandSource;
        String effect = args.<String>getOne("effect").get().toLowerCase();
        AbstractEffect abstractEffect = null;
        ParticleData effectData = ParticleData.builder().addViewer(Managers.USER.findPlayer(player.getUniqueId()).get())
                    .center(player.getLocation().getPosition().add(0, 1, 0))
                .quantity(5)
                //.offsets(0.2, 0.2, 0.2)
                    .type(ParticleTypes.REDSTONE_DUST)
                    .addParticleOption(ParticleOptions.COLOR, Color.BLACK)
                    //.animationData(new ParticleData.AnimationData(15L, 20L, 0L))
                    .build();

        switch (effect){
            //case "atom": abstractEffect = new AtomEffect(effectData, PreloadedParticle.ATOM_2RR_1CR_0Y);
                //break;
            case "helix": {
                abstractEffect =
                    new HelixHorizontalEffect(1.0, 0.25, 2.5, 5, effectData,
                        //new EffectOptionIterate(),
                        new EffectOptionAnimationData(new AnimationData(-1L, 2L, 0L)),
                            new EffectOptionCasterRotation(Managers.USER.find(player.getUniqueId()).get()));

                ParticleData e2 = ParticleData.builder().addViewer(Managers.USER.findPlayer(player.getUniqueId()).get())
                        .center(player.getLocation().getPosition().add(0, 1, 0))
                        .quantity(5)
                        //.offsets(0.2, 0.2, 0.2)
                        .type(ParticleTypes.REDSTONE_DUST)
                        .addParticleOption(ParticleOptions.COLOR, Color.BLUE)
                        //.animationData(new ParticleData.AnimationData(15L, 20L, 0L))
                        .build();

                AbstractEffect effect1 = new HelixHorizontalEffect(1.0, 0.25, 1.25, 5, e2,
                        new EffectOptionAnimationData(new AnimationData(-1L, 2L, 0L)),
                new EffectOptionCasterRotation(Managers.USER.find(player.getUniqueId()).get()));
                effect1.start();
            }
                break;
            case "line": abstractEffect = new LineEffect(effectData, player.getLocation().getPosition().add(15, 15, 15));
                break;
            case "sphere": abstractEffect = new SphereEffect(effectData, PreloadedParticle.SPHERE_1R);
                break;
            case "parabola": abstractEffect = new ParabolicEffect(effectData, 10, 100,
                    new EffectOptionAnimationData(new AnimationData(100, 2L, 0L)),
                    //new EffectOptionStaticRotation(new Pair<>(45.0, 135.0)),
                    new EffectOptionIterate());
                break;
            //case "tornado": abstractEffect = new TornadoEffect(effectData, PreloadedParticle.TORNADO_35H_3R);
                //break;
            //case "helix2": abstractEffect = new HelixHorizontalEffect(effectData, PreloadedParticle.HELIX_HOR_10T_25HS_75R_15L, false);

        }

        abstractEffect.start();

        return CommandResult.success();
    }

}
