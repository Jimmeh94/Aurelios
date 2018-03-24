package com.aurelios.server.commands;

import com.aurelios.Aurelios;
import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.modules.UserModuleTypes;
import com.aurelios.server.game.user.modules.poll.Poll;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.text.Message;
import com.aurelios.server.util.text.Messager;
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

public class PollCommands implements CommandExecutor {

    /**
     * /poll help
     * /poll create <title>
     * /poll add <"item"|string>
     * /poll cancel
     * /poll finish
     * /poll menu/all
     */

    public PollCommands(){
        //create
        CommandSpec create = CommandSpec.builder()
                .description(Text.of("Create a poll"))
                .executor(new Create())
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("title")))
                .build();

        //add
        CommandSpec add = CommandSpec.builder()
                .description(Text.of("Add an item to the poll"))
                .executor(new Add())
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("item")))
                .build();

        //parent command spec
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("Poll commands"))
                .executor(this)
                .arguments(GenericArguments.string(Text.of("guide")))
                .child(create, "create", "make", "new")
                .child(add, "add")
                .build();

        Sponge.getCommandManager().register(Aurelios.INSTANCE, commandSpec, "poll");
    }


    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

        if(commandSource instanceof Player){
            Player player = (Player)commandSource;
            UserPlayer user = Managers.USER.findPlayer(player.getUniqueId()).get();
            Poll.PollBuilder builder = (Poll.PollBuilder) user.getModule(UserModuleTypes.POLL_BUILDER).get();

            if(args.<String>hasAny("guide")){

                switch (args.<String>getOne("guide").get()){
                    case "finish": {
                        if(user.getModule(UserModuleTypes.POLL_BUILDER).get().isEnabled()){
                            if(builder.getChoices() != null && builder.getChoices().size() > 1){
                                builder.timeLength(1).build();
                                Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                                        .addMessage(Text.of(TextColors.GREEN, "Your poll has been finished and broadcasted!"), Messager.Prefix.SUCCESS)
                                        .build());
                            } else {
                                Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                                        .addMessage(Text.of(TextColors.RED, "Use /poll add <item | sentence> to add choices to this poll!"), Messager.Prefix.ERROR)
                                        .addAsChild(Text.of(TextColors.GRAY, "You must have at least 2 choices to finish the poll"), TextColors.GOLD)
                                        .build());
                            }
                        }
                    }
                    break;
                    case "help": {
                        Messager.sendMessage(Message.builder().addReceiver(player)
                                .addMessage(Text.of(TextColors.GOLD, "===================================="))
                                .addMessage(Text.of(TextColors.GRAY, "Poll Commands:"))
                                .addAsChild(Text.of(TextColors.GRAY, "Tips: <> = required, [] = optional"), TextColors.GOLD)
                                .addAsChild(Text.of(TextColors.GRAY, "/poll create <title>"), TextColors.GOLD)
                                .addAsChild(Text.of(TextColors.GRAY, "/poll add <item | type a possible answer>"), TextColors.GOLD)
                                .addAsChild(Text.of(TextColors.GRAY, "/poll finish"), TextColors.GOLD)
                                .addAsChild(Text.of(TextColors.GRAY, "/poll cancel"), TextColors.GOLD)
                                .addAsChild(Text.of(TextColors.GRAY, "/poll menu"), TextColors.GOLD)
                                .addAsChild(Text.of(TextColors.GRAY, "/poll history"), TextColors.GOLD)
                                .addAsChild(Text.of(TextColors.GRAY, "/poll update"), TextColors.GOLD)
                                .addMessage(Text.of(TextColors.GOLD, "===================================="))
                                .build());
                    }
                    break;
                    case "stop": {
                        if(Managers.POLL.isRunningPoll(user)){
                            Managers.POLL.getPoll(user).get().stop();
                        }
                    }
                    break;
                    case "update": {
                        if(Managers.POLL.isRunningPoll(user)){
                            Managers.POLL.getPoll(user).get().displayResultChat(user);
                        } else {
                            Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                                    .addMessage(Text.of(TextColors.RED, "You don't have an active poll right now!"), Messager.Prefix.ERROR).build());
                        }
                    }
                    break;
                    case "history": {
                        Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                                .addMessage(Text.of(TextColors.GRAY, "We should connect this to their online profile where we store old poll results"), Messager.Prefix.INFO).build());
                    }
                }
            }
        }

        return CommandResult.success();
    }

    public static class Add implements CommandExecutor{

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

            if(commandSource instanceof Player){
                Player player = (Player)commandSource;

                UserPlayer user = Managers.USER.findPlayer(player.getUniqueId()).get();

                if(user.getModule(UserModuleTypes.POLL_BUILDER).get().isEnabled()){
                    Poll.PollBuilder builder = ((Poll.PollBuilder)user.getModule(UserModuleTypes.POLL_BUILDER).get());
                    if(builder.getTitle() == null){
                        Aurelios.INSTANCE.getLogger().info("========= ITES NULL ++++++++++++++");
                    }
                    if(((Poll.PollBuilder)user.getModule(UserModuleTypes.POLL_BUILDER)
                            .get())
                            .getTitle().isEmpty()) {
                        Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                                .addMessage(Text.of(TextColors.RED, "Use /poll create <title> to start a poll!"),
                                        Messager.Prefix.ERROR).build());
                        return CommandResult.success();
                    }
                } else {
                    return CommandResult.success();
                }

                Poll.PollBuilder builder = (Poll.PollBuilder) user.getModule(UserModuleTypes.POLL_BUILDER).get();

                if(builder.getChoices() != null && builder.getChoices().size() == 5){
                    Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                            .addMessage(Text.of(TextColors.RED, "You already have 5 choices and can't add any more"),
                                    Messager.Prefix.ERROR).build());
                } else {
                    String add = args.<String>getOne("item").get();
                    if(add.equalsIgnoreCase("item")){

                    } else {
                        builder.choice(add);

                        if(builder.getChoices().size() > 1){
                            Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                                    .addMessage(Text.of(TextColors.GREEN, "Choice added to poll: " + add), Messager.Prefix.SUCCESS)
                                    .addAsChild(Text.of(TextColors.GRAY, "Use '/poll add <item or type an answer>' to add another choice to the poll"), TextColors.GOLD)
                                    .addAsChild(Text.of(TextColors.GRAY, "You can add up to " + (5 - builder.getChoices().size()) + " more choices"), TextColors.GOLD)
                                    .addAsChild(Text.of(TextColors.GRAY, "Use '/poll finish' to complete and broadcast the poll"), TextColors.GOLD)
                                    .build());
                        } else {
                            Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                                    .addMessage(Text.of(TextColors.GREEN, "Choice added to poll: " + add), Messager.Prefix.SUCCESS)
                                    .addAsChild(Text.of(TextColors.GRAY, "Use '/poll add <item or type an answer>' to add another choice to the poll"), TextColors.GOLD)
                                    .addAsChild(Text.of(TextColors.GRAY, "You can add up to " + (5 - builder.getChoices().size()) + " more choices"), TextColors.GOLD)
                                    .build());
                        }
                    }
                }
            }

            return CommandResult.success();
        }
    }

    public static class Create implements CommandExecutor{

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

            if(commandSource instanceof Player){
                Player player = ((Player)commandSource);
                UserPlayer user = Managers.USER.findPlayer(player.getUniqueId()).get();

                if(user.hasModule(UserModuleTypes.POLL_BUILDER)){
                    if(!user.getModule(UserModuleTypes.POLL_BUILDER).get().isEnabled()){
                        Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                                .addMessage(Text.of(TextColors.RED, "You can't start a poll right now"), Messager.Prefix.ERROR).build());
                    }
                } else {
                    Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                            .addMessage(Text.of(TextColors.RED, "You can't start a poll right now"), Messager.Prefix.ERROR).build());
                }

                if(Managers.POLL.isRunningPoll(user)){
                    Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                            .addMessage(Text.of(TextColors.RED, "You cannot have more than 1 active poll! Use /poll stop to cancel your current poll"),
                                    Messager.Prefix.ERROR).build());
                    return CommandResult.success();
                }

                if(!args.hasAny("title")){
                    Messager.sendMessage(Message.builder().addReceiver(user.getPlayer())
                            .addMessage(Text.of(TextColors.RED, "Include a title for the poll: /poll create <title>"), Messager.Prefix.ERROR)
                            .build());
                    return CommandResult.success();
                }

                Poll.PollBuilder builder = ((Poll.PollBuilder) user.getModule(UserModuleTypes.POLL_BUILDER).get());
                builder.clear();
                builder.title(args.<String>getOne("title").get());

                Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.GREEN, "Poll created: " + builder.getTitle()), Messager.Prefix.SUCCESS)
                        .addAsChild(Text.of(TextColors.GRAY, "Use '/poll add <item or type an answer>' to add a choice to the poll"), TextColors.GOLD)
                        .addAsChild(Text.of(TextColors.GRAY, "You can have up to 5 choices"), TextColors.GOLD).build());
            }

            return CommandResult.success();
        }
    }
}
