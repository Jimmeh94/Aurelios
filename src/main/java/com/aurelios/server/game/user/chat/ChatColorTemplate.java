package com.aurelios.server.game.user.chat;

import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class ChatColorTemplate{

    public static final ChatColorTemplate GRAY = new ChatColorTemplate(TextColors.GRAY, TextColors.GRAY, TextColors.GRAY);

    private TextColor prefix, name, message;

    ChatColorTemplate(TextColor... colors){
        this.prefix = colors[0];
        this.name = colors[1];
        this.message = colors[2];
    }

    public TextColor getPrefix() {
        return prefix;
    }

    public TextColor getName() {
        return name;
    }

    public TextColor getMessage() {
        return message;
    }
}
