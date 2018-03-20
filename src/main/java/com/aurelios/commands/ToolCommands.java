package com.aurelios.commands;

import com.aurelios.Aurelios;
import com.aurelios.game.user.UserPlayer;
import com.aurelios.game.user.modules.tools.ModuleAreaCreator;
import com.aurelios.game.user.modules.UserModule;
import com.aurelios.game.user.modules.UserModuleTypes;
import com.aurelios.game.user.modules.tools.ModulePOICreator;
import com.aurelios.managers.Managers;
import com.aurelios.util.text.Message;
import com.aurelios.util.text.Messager;
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
import org.spongepowered.api.text.format.TextColors;

public class ToolCommands implements CommandExecutor {

    /**
     * /tool node|poi [radius] [name]
     */

    public ToolCommands() {

        //parent command spec
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("Tool commands"))
                .executor(this)
                .arguments(GenericArguments.string(Text.of("type")),
                        GenericArguments.doubleNum(Text.of("radius")),
                        GenericArguments.string(Text.of("name")),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("aiCap/roles"))))
                .build();

        Sponge.getCommandManager().register(Aurelios.INSTANCE, commandSpec, "tool");
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

        Player player = (Player)commandSource;
        UserPlayer owner = (UserPlayer) Managers.USER.find(player.getUniqueId()).get();

        if(args.hasAny("type")){
            switch (args.<String>getOne("type").get()){
                case "node": {
                    if(owner.hasModule(UserModuleTypes.NODE_CREATOR)){
                        UserModule module = owner.getModule(UserModuleTypes.NODE_CREATOR).get();
                        if(args.<Double>getOne("radius").isPresent()){
                            if(!module.isEnabled()){
                                module.enable();
                            }
                            ((ModuleAreaCreator)module).setRadius(args.<Double>getOne("radius").get());
                            Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer())
                                    .addMessage(Text.of(TextColors.GREEN, "Radius set to " + args.<Double>getOne("radius").get()), Messager.Prefix.SUCCESS)
                                    .build());
                        }
                        if(args.<String>getOne("name").isPresent()){
                            if(!module.isEnabled()){
                                module.enable();
                            }
                            ((ModuleAreaCreator)module).setName(args.<String>getOne("name").get());
                            Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer())
                                    .addMessage(Text.of(TextColors.GREEN, "Name set to " + args.<String>getOne("name").get()), Messager.Prefix.SUCCESS)
                                    .build());
                        }
                        if(args.<String>getOne("aiCap/roles").isPresent()){
                            if(!module.isEnabled()){
                                module.enable();
                            }
                            ((ModuleAreaCreator)module).setAiCap(Integer.parseInt(args.<String>getOne("aiCap/roles").get()));
                            Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer())
                                    .addMessage(Text.of(TextColors.GREEN, "Ai cap set to " + args.<String>getOne("aiCap/roles").get()), Messager.Prefix.SUCCESS)
                                    .build());
                        }
                    } else Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer())
                            .addMessage(Text.of(TextColors.RED, "You can't access this!"), Messager.Prefix.ERROR)
                            .build());
                }
                    break;
                case "poi": {
                    if(owner.hasModule(UserModuleTypes.POI_CREATOR)){
                        UserModule module = owner.getModule(UserModuleTypes.POI_CREATOR).get();
                        if(args.<Double>getOne("radius").isPresent()){
                            if(!module.isEnabled()){
                                module.enable();
                            }
                            ((ModulePOICreator)module).setRadius(args.<Double>getOne("radius").get());
                            Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer())
                                    .addMessage(Text.of(TextColors.GREEN, "Radius set to " + args.<Double>getOne("radius").get()), Messager.Prefix.SUCCESS)
                                    .build());
                        }
                        if(args.<String>getOne("name").isPresent()){
                            if(!module.isEnabled()){
                                module.enable();
                            }
                            ((ModulePOICreator)module).setName(args.<String>getOne("name").get());
                            Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer())
                                    .addMessage(Text.of(TextColors.GREEN, "Name set to " + args.<String>getOne("name").get()), Messager.Prefix.SUCCESS)
                                    .build());
                        }
                        if(args.<String>getOne("aiCap/roles").isPresent()){
                            if(!module.isEnabled()){
                                module.enable();
                            }
                            ((ModulePOICreator)module).setRoles(args.<String>getOne("aiCap/roles").get());
                            Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer())
                                    .addMessage(Text.of(TextColors.GREEN, "Roles set!"), Messager.Prefix.SUCCESS)
                                    .build());
                        }
                    } else Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer())
                            .addMessage(Text.of(TextColors.RED, "You can't access this!"), Messager.Prefix.ERROR)
                            .build());
                }
            }
        } else printHelpMessage(player);

        return CommandResult.success();
    }


    private void printHelpMessage(Player player){
        Messager.sendMessage(Message.builder().addReceiver(player)
                .addMessage(Text.of(TextColors.GOLD, "===================================="))
                .addMessage(Text.of(TextColors.GRAY, "Tool Commands:"))
                .addAsChild(Text.of(TextColors.GRAY, "Tips: <> = required, [] = optional"), TextColors.GOLD)
                .addAsChild(Text.of(TextColors.GRAY, "/tool <tool type> <enable|disable>"), TextColors.GOLD)
                .addMessage(Text.of(TextColors.GOLD, "===================================="))
                .build());
    }
}
