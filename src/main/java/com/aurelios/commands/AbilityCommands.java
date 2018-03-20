package com.aurelios.commands;

import com.aurelios.Aurelios;
import com.aurelios.game.ability.abilities.test.FireDragonRoar;
import com.aurelios.game.ability.abilities.test.Fireball;
import com.aurelios.game.user.User;
import com.aurelios.game.user.stats.Stat;
import com.aurelios.managers.Managers;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * /eco, /economy, /$
 *
 * Arguments:
 * balance [player]
 * pay player amount
 * request player amount
 */

public class AbilityCommands implements CommandExecutor {

    public AbilityCommands(){

        //parent command spec
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("Ability commands"))
                .executor(this)
                .arguments(GenericArguments.string(Text.of("ability")))
                .build();

        Sponge.getCommandManager().register(Aurelios.INSTANCE, commandSpec, "ability");
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

        if(commandSource instanceof Player){
            Player player = (Player) commandSource;

            if(args.hasAny("ability")){

                switch (args.<String>getOne("ability").get()){
                    case "fireball": {
                        Fireball fireball = new Fireball(Managers.USER.find(player.getUniqueId()).get());
                        fireball.fire();
                    }
                    break;
                    case "roar": {
                        FireDragonRoar roar = new FireDragonRoar(Managers.USER.find(player.getUniqueId()).get());
                        roar.fire();
                    }
                    break;
                    case "chat": {
                        //FireDragonRoar roar = new FireDragonRoar(Managers.USER.getValue().find(player.getUniqueId()).get());
                        //Fireball fireball = new Fireball(Managers.USER.getValue().find(player.getUniqueId()).get());

                        //Messager.sendMessage(Message.builder().addReceiver(player)
                          //      .addMessage(roar.getAbility().getChatRepresentation(), Messager.Prefix.ABILITY).build());
                        //Messager.sendMessage(Message.builder().addReceiver(player)
                         //       .addMessage(fireball.getAbility().getChatRepresentation(), Messager.Prefix.ABILITY).build());
                    }
                    break;
                    case "fight": {
                        Vector3d first = player.getLocation().getPosition().add(-20, 0, 20),
                            second = player.getLocation().getPosition().sub(20, 0, 20);

                        ArmorStand one = (ArmorStand) Sponge.getServer().getWorld("world").get()
                                .createEntity(EntityTypes.ARMOR_STAND, first);

                        ArmorStand two = (ArmorStand) Sponge.getServer().getWorld("world").get()
                                .createEntity(EntityTypes.ARMOR_STAND, second);

                        one.offer(Keys.HAS_GRAVITY, false);
                        two.offer(Keys.HAS_GRAVITY, false);

                        one.offer(Keys.DISPLAY_NAME, Text.of("One"));
                        one.offer(Keys.CUSTOM_NAME_VISIBLE, true);

                        two.offer(Keys.DISPLAY_NAME, Text.of("Two"));
                        two.offer(Keys.CUSTOM_NAME_VISIBLE, true);

                        Sponge.getServer().getWorld("world").get()
                                .spawnEntity(one);
                        Sponge.getServer().getWorld("world").get()
                                .spawnEntity(two);

                        User f = new User(one);
                        new User(two);

                        f.getStats().find(Stat.Type.MANA).get().addToMax(100);
                        f.getStats().find(Stat.Type.MANA).get().addToAmount(100);

                        one.setHeadRotation(new Vector3d(180, 4, 0));
                        two.setHeadRotation(new Vector3d(0, 4, 0));

                        Fireball fireball = new Fireball(f);
                        fireball.fire();

                        Fireball fireball2 = new Fireball(Managers.USER.find(two.getUniqueId()).get());
                        fireball2.fire();

                        /*one.remove();
                        two.remove();*/
                    }
                }
            }
        }

        return CommandResult.success();
    }
}
