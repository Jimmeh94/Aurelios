package com.aurelios.server.commands;

import com.aurelios.Aurelios;
import com.aurelios.server.game.user.User;
import com.aurelios.server.managers.Managers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;

public class TestCommands implements CommandExecutor {

    public TestCommands() {

        //parent command spec
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("Test commands"))
                .executor(this)
                .arguments(GenericArguments.string(Text.of("test")))
                .build();

        Sponge.getCommandManager().register(Aurelios.INSTANCE, commandSpec, "test");
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

        Player player = (Player)commandSource;
        User owner = Managers.USER.find(player.getUniqueId()).get();

        switch (args.<String>getOne("test").get()){
            case "direction":{
                for(Direction direction: Direction.values()){
                    Aurelios.INSTANCE.getLogger().info(direction.toString() + " " + direction.asOffset().toString());
                }
            }/*
            case "north":{

                AbstractEffect effect = new HelixEffectTest(
                        ParticleData.builder().addViewer(owner)
                                .center(owner.getPlayer().getLocation().getPosition().add(0, 1, 0))
                                .quantity(1)
                                .animationData(new ParticleData.AnimationData(15L, 5L, 0L))
                                .type(ParticleTypes.FLAME)
                                .build(),
                        PreloadedParticle.HELIX_NORTH);

                effect.start();
            }
                break;
            case "east":{
                AbstractEffect effect = new HelixEffectTest(
                        ParticleData.builder().addViewer(owner)
                                .center(owner.getPlayer().getLocation().getPosition().add(0, 1, 0))
                                .quantity(1)
                                .animationData(new ParticleData.AnimationData(15L, 5L, 0L))
                                .type(ParticleTypes.FLAME)
                                .build(),
                        PreloadedParticle.HELIX_EAST);

                effect.start();
            }
                break;
            case "south":{
                AbstractEffect effect = new HelixEffectTest(
                        ParticleData.builder().addViewer(owner)
                                .center(owner.getPlayer().getLocation().getPosition().add(0, 1, 0))
                                .quantity(1)
                                .animationData(new ParticleData.AnimationData(15L, 5L, 0L))
                                .type(ParticleTypes.FLAME)
                                .build(),
                        PreloadedParticle.HELIX_SOUTH);

                effect.start();
            }
                break;
            case "west":{
                AbstractEffect effect = new HelixEffectTest(
                        ParticleData.builder().addViewer(owner)
                                .center(owner.getPlayer().getLocation().getPosition().add(0, 1, 0))
                                .quantity(1)
                                .animationData(new ParticleData.AnimationData(15L, 5L, 0L))
                                .type(ParticleTypes.FLAME)
                                .build(),
                        PreloadedParticle.HELIX_WEST);

                effect.start();
            }
                break;
            case "ne":{
                AbstractEffect effect = new HelixEffectTest(
                        ParticleData.builder().addViewer(owner)
                                .center(owner.getPlayer().getLocation().getPosition().add(0, 1, 0))
                                .quantity(1)
                                .animationData(new ParticleData.AnimationData(60L, 5L, 0L))
                                .type(ParticleTypes.FLAME)
                                .build(),
                        PreloadedParticle.HELIX_NE);

                effect.start();
            }*/
        }

        return CommandResult.success();
    }
}
