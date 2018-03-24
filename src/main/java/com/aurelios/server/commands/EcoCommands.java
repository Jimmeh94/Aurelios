package com.aurelios.server.commands;

import com.aurelios.Aurelios;
import com.aurelios.server.game.user.UserPlayer;
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

import java.util.Optional;

/**
 * /eco, /economy, /$
 *
 * Arguments:
 * balance [player]
 * pay player amount
 * request player amount
 */

public class EcoCommands implements CommandExecutor {

    public EcoCommands(){
        //balance
        CommandSpec balance = CommandSpec.builder()
                .description(Text.of("Check a player's account balance"))
                .executor(new Balance())
                .arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .build();

        //pay
        CommandSpec pay = CommandSpec.builder()
                .description(Text.of("Make a payment or send money"))
                .executor(new Pay())
                .arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.doubleNum(Text.of("amount"))))
                                .build();

        //request
        CommandSpec request = CommandSpec.builder()
                .description(Text.of("Make a money request"))
                .executor(new Request())
                .arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.doubleNum(Text.of("amount"))))
                        .build();

        //help
        CommandSpec help = CommandSpec.builder()
                .description(Text.of("Help with economy commands"))
                .executor(this)
                .build();

        //parent command spec
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("Economy commands"))
                .executor(this)
                .child(balance, "balance", "bal", "amount")
                .child(pay, "pay", "send", "give", "transfer")
                .child(request, "request", "ask")
                .child(help, "help", "commands", "list", "tips")
                .build();

        Sponge.getCommandManager().register(Aurelios.INSTANCE, commandSpec, "eco", "economy", "$");
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

        if(commandSource instanceof Player){
            Player player = (Player) commandSource;

            Messager.sendMessage(Message.builder().addReceiver(player)
                            .addMessage(Text.of(TextColors.GOLD, "===================================="))
                            .addMessage(Text.of(TextColors.GRAY, "Economy Commands:"))
                            .addAsChild(Text.of(TextColors.GRAY, "Tips: <> = required, [] = optional"), TextColors.GOLD)
                            .addAsChild(Text.of(TextColors.GRAY, "/eco bal|balance|amount [player]"), TextColors.GOLD)
                            .addAsChild(Text.of(TextColors.GRAY, "/eco pay|send|give|transfer <player> <amount>"), TextColors.GOLD)
                            .addAsChild(Text.of(TextColors.GRAY, "/eco request|ask <player> <amount>"), TextColors.GOLD)
                            .addMessage(Text.of(TextColors.GOLD, "===================================="))
                            .build());
        }

        return CommandResult.success();
    }

    public static class Pay implements CommandExecutor{

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

            if(commandSource instanceof Player){
                Player player = (Player)commandSource;

                /**
                 * Pay
                 */
                if(args.<Player>getOne("player").isPresent()){
                    Optional<Player> target = Sponge.getServer().getPlayer(args.<Player>getOne("player").get().getUniqueId());
                    if(target.isPresent()){
                        Optional<UserPlayer> user = Managers.USER.findPlayer(target.get().getUniqueId());
                        if(user.isPresent()){
                            if(args.<Double>getOne("amount").isPresent()){
                                Managers.ECO.sendMoney(Managers.USER.findPlayer(player.getUniqueId()).get().getAccount(),
                                        user.get().getAccount(),
                                        args.<Double>getOne("amount").get());
                            } else {
                                Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "Correct usage: /eco pay|send|transfer|give <player> <amount>"), Messager.Prefix.ERROR).build());
                            }
                        } else {
                            Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "User wasn't found: " + args.<Player>getOne("player").get().getName()), Messager.Prefix.ERROR).build());
                        }
                    } else {
                        Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "Player wasn't found: " + args.<Player>getOne("player").get().getName()), Messager.Prefix.ERROR).build());
                    }
                } else {
                    Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "Correct usage: /eco pay|send|transfer|give <player> <amount>"), Messager.Prefix.ERROR).build());
                }
            }

            return CommandResult.success();
        }
    }

    public static class Request implements CommandExecutor{

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

            if(commandSource instanceof Player) {
                Player player = (Player) commandSource;

                /**
                 * Payment requests
                 */
                if(args.<Player>getOne("player").isPresent()) {
                    Optional<Player> target = Sponge.getServer().getPlayer(args.<Player>getOne("player").get().getUniqueId());
                    if (target.isPresent()) {
                        Optional<UserPlayer> user = Managers.USER.findPlayer(target.get().getUniqueId());
                        if (user.isPresent()) {
                            if (args.<Double>getOne("amount").isPresent()) {
                                if (user.get().getPlayerKeys().ECO_REQUESTS_AUTO_DENY.getValue()) {
                                    Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "This player isn't accepting money requests"), Messager.Prefix.ERROR).build());
                                } else {
                                    Managers.ECO.sendMoney(Managers.USER.findPlayer(player.getUniqueId()).get().getAccount(),
                                            user.get().getAccount(),
                                            args.<Double>getOne("amount").get());
                                }
                            } else {
                                Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "Correct usage: /eco request|ask <player> <amount>"), Messager.Prefix.ERROR).build());
                            }
                        } else {
                            Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "User wasn't found: " + args.<Player>getOne("player").get().getName()), Messager.Prefix.ERROR).build());
                        }
                    } else {
                        Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "Player wasn't found: " + args.<Player>getOne("player").get().getName()), Messager.Prefix.ERROR).build());
                    }
                } else {
                    Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "Correct usage: /eco request|ask <player> <amount>"), Messager.Prefix.ERROR).build());
                }
            }
            return CommandResult.success();
        }
    }

    public static class Balance implements CommandExecutor{

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

            if(commandSource instanceof Player){
                Player player = (Player) commandSource;

                /**
                 * Balance
                 */
                if(args.<Player>getOne("player").isPresent()){
                    Optional<Player> target = Sponge.getServer().getPlayer(args.<Player>getOne("player").get().getUniqueId());
                    if(target.isPresent()){
                        Optional<UserPlayer> user = Managers.USER.findPlayer(target.get().getUniqueId());
                        if(user.isPresent()){
                            if(user.get().getPlayerKeys().ECO_OTHERS_SEE_BALANCE.getValue()){
                                user.get().getAccount().showBalanceTo(player);
                            } else {
                                Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "You don't have permission to view this player's balance"), Messager.Prefix.ERROR).build());
                            }
                        } else {
                            Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "User wasn't found: " + args.<Player>getOne("player").get().getName()), Messager.Prefix.ERROR).build());
                        }
                    } else {
                        Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "Player wasn't found: " + args.<Player>getOne("player").get().getName()), Messager.Prefix.ERROR).build());
                    }
                } else {
                    Managers.USER.findPlayer(player.getUniqueId()).get().getAccount().showBalance();
                }
            }

            return CommandResult.success();
        }
    }
}
