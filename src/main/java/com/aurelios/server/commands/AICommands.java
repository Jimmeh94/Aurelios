package com.aurelios.server.commands;

import com.aurelios.Aurelios;
import com.aurelios.server.game.user.ai.NPC;
import com.aurelios.server.game.user.ai.nms.tasks.EntityAIMoveTo;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.misc.EntityUtils;
import com.aurelios.server.util.text.Message;
import com.aurelios.server.util.text.Messager;
import net.minecraft.entity.EntityCreature;
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
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class AICommands implements CommandExecutor {

    /**
     * /ai <create> [amount]
     */

    public AICommands() {

        //parent command spec
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("AI commands"))
                .executor(this)
                .arguments(GenericArguments.string(Text.of("mode")),
                        GenericArguments.optionalWeak(GenericArguments.integer(Text.of("amount"))))
                .build();

        Sponge.getCommandManager().register(Aurelios.INSTANCE, commandSpec, "ai", "npc");
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(args.<String>getOne("mode").get().equalsIgnoreCase("create")){
            int amount = 1;
            if(args.<Integer>hasAny("amount")){
                amount = args.<Integer>getOne("amount").get();
            }
            for(int i = 0; i < amount; i++){
                Managers.AI.createNPC(Managers.NODES.getObjects().get(0));
                Messager.sendMessage(Message.builder().addReceiver((Player)src)
                                        .addMessage(Text.of(TextColors.GREEN, "NPC Created!"), Messager.Prefix.SUCCESS)
                                        .build());
            }

            //Print npc data
            for(int i = 0; i < amount; i++){
                Managers.AI.getObjects().get(Managers.AI.getObjects().size() - 1 - i).getMetaData().print();
            }
        } else if(args.<String>getOne("mode").get().equalsIgnoreCase("simulate")){
            Managers.AI.simulate();
        }  else if(args.<String>getOne("mode").get().equalsIgnoreCase("display")){
            Managers.AI.printRelationships();
        } else if(args.<String>getOne("mode").get().equalsIgnoreCase("spawn")){
            Location<World> spawn = ((Player)src).getLocation();
            for(NPC npc: Managers.AI.getObjects()){
                Living entity = (Living) spawn.getExtent().createEntity(EntityTypes.VILLAGER, spawn.getPosition());
                entity.offer(Keys.DISPLAY_NAME, Text.of(npc.getMetaData().getFullName()));
                npc.attachEntity(entity);
                EntityCreature c = (EntityCreature)entity;
                EntityUtils.clearTasks(c);
                c.tasks.addTask(0, new EntityAIMoveTo(npc));
                spawn.getExtent().spawnEntity(entity);
            }

        }

        return CommandResult.success();
    }
}
