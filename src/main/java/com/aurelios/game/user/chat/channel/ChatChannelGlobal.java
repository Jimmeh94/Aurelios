package com.aurelios.game.user.chat.channel;

import com.aurelios.game.user.UserPlayer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class ChatChannelGlobal extends ChatChannel {

    private static final Text prefix = Text.of(TextColors.GRAY, "(Global) ");

    public ChatChannelGlobal(String key) {
        super(key, true, prefix);

        passwordProtected = false;
    }

    public ChatChannelGlobal(String key, UserPlayer owner) {
        super(key, true, prefix, owner);
    }

    public ChatChannelGlobal(String key, UserPlayer... members) {
        super(key, true, prefix, members);
    }

    public ChatChannelGlobal(String key, List<UserPlayer> members) {
        super(key, true, prefix, members);
    }

}
