package com.aurelios.server.game.dialogue.test;

import com.aurelios.server.game.dialogue.Dialogue;
import com.aurelios.server.game.dialogue.DialogueDisplayer;
import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.text.Message;
import com.aurelios.server.util.text.Messager;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;
import java.util.function.Consumer;

public class OldMan implements DialogueDisplayer{

    private final Dialogue withWhat = Dialogue.builder()
            .openingSentence(Text.of(TextColors.WHITE, "[Old Man] ", TextColors.GRAY, "I lost my glasses. Will you help me find them?"))
            .choice(new Dialogue.Choice(Optional.empty(), Text.builder("Yes, I will").color(TextColors.GRAY).onClick(TextActions.executeCallback(new Consumer<CommandSource>() {
                @Override
                public void accept(CommandSource commandSource) {
                    Player player = (Player) commandSource;

                    Messager.sendMessage(Message.builder().addReceiver(player)
                            .addMessage(Text.of(TextColors.WHITE, "[Old Man] ", TextColors.GRAY, "Oh thank you!"), Messager.Prefix.SUCCESS).build());
                }
            })).onHover(TextActions.showText(Text.of(TextColors.GREEN, "Accept the old man's quest"))).build()))
            .choice(new Dialogue.Choice(Optional.empty(), Text.builder("No, I can't").color(TextColors.GRAY).onClick(TextActions.executeCallback(new Consumer<CommandSource>() {
                @Override
                public void accept(CommandSource commandSource) {
                    Player player = (Player) commandSource;

                    Messager.sendMessage(Message.builder().addReceiver(player)
                            .addMessage(Text.of(TextColors.WHITE, "[Old Man] ", TextColors.GRAY, "Oh... maybe next time then :("), Messager.Prefix.ERROR).build());
                }
            })).onHover(TextActions.showText(Text.of(TextColors.RED, "Deny the old man's quest"))).build()))
            .build();

    private final Dialogue opener = Dialogue.builder()
            .openingSentence(Text.of(TextColors.WHITE, "[Old Man] ", TextColors.GRAY, "Will you help me?"))
            .choice(new Dialogue.Choice(Optional.empty(), Text.builder("Sure!").color(TextColors.GRAY).onClick(TextActions.executeCallback(new Consumer<CommandSource>() {
                @Override
                public void accept(CommandSource commandSource) {
                    Player player = (Player) commandSource;

                    Messager.sendMessage(Message.builder().addReceiver(player)
                            .addMessage(Text.of(TextColors.WHITE, "[Old Man] ", TextColors.GRAY, "Oh thank you!"), Messager.Prefix.SUCCESS).build());
                }
            })).onHover(TextActions.showText(Text.of(TextColors.GREEN, "Help the old man"))).build()))
            .choice(new Dialogue.Choice(Optional.empty(), Text.builder("With what?").color(TextColors.GRAY).onClick(TextActions.executeCallback(new Consumer<CommandSource>() {
                @Override
                public void accept(CommandSource commandSource) {
                    withWhat.display(Managers.USER.findPlayer(((Player)commandSource).getUniqueId()).get());
                }
            })).onHover(TextActions.showText(Text.of(TextColors.GOLD, "Inquire more from the old man"))).build()))
            .choice(new Dialogue.Choice(Optional.empty(), Text.builder("I can't right now!").color(TextColors.GRAY).onClick(TextActions.executeCallback(new Consumer<CommandSource>() {
                @Override
                public void accept(CommandSource commandSource) {
                    Player player = (Player) commandSource;

                    Messager.sendMessage(Message.builder().addReceiver(player)
                            .addMessage(Text.of(TextColors.WHITE, "[Old Man] ", TextColors.GRAY, "Oh... maybe next time then :("), Messager.Prefix.ERROR).build());
                }
            })).onHover(TextActions.showText(Text.of(TextColors.RED, "Decline the old man"))).build()))
            .build();

    @Override
    public void displayDialogue(UserPlayer user) {
        opener.display(user);
    }
}
