package com.aurelios.server.commands;

import com.aurelios.Aurelios;
import com.aurelios.server.managers.Managers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class DialogueCommands implements CommandExecutor {

    public DialogueCommands() {

        //parent command spec
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("Dialogue commands"))
                .executor(this)
                .build();

        Sponge.getCommandManager().register(Aurelios.INSTANCE, commandSpec, "dialogue");
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

        Managers.USER.findPlayer(((Player)commandSource).getUniqueId()).get().display();

        return CommandResult.success();
    }
}
