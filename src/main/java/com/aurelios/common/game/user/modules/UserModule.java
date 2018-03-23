package com.aurelios.common.game.user.modules;

import com.aurelios.common.game.user.UserPlayer;
import com.aurelios.common.util.text.Message;
import com.aurelios.common.util.text.Messager;
import org.spongepowered.api.text.Text;

/**
 * Modules are extensible functionality. The idea behind modules
 * is that it becomes incredibly easy to enable and disabled features
 * for players on a whim. This could be helpful for moderation of players, etc.
 */
public abstract class UserModule {

    private boolean enabled = false;
    private UserPlayer owner;
    private UserModuleTypes type;

    public abstract String getDisabledMessage();
    public abstract String getEnabledMessage();

    public UserModule(UserPlayer owner, UserModuleTypes type) {
        this.owner = owner;
        this.type = type;
    }

    public void disable(){
        enabled = false;
        Messager.sendMessage(Message.builder()
                .addReceiver(owner.getPlayer())
                .addMessage(Text.of(getDisabledMessage()), Messager.Prefix.INFO)
                .build());
    }

    public void enable(){
        enabled = true;
        Messager.sendMessage(Message.builder()
                .addReceiver(owner.getPlayer())
                .addMessage(Text.of(getEnabledMessage()), Messager.Prefix.INFO)
                .build());
    }

    public void toggle(){
        if(enabled){
            disable();
        } else enable();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UserPlayer getOwner() {
        return owner;
    }

    public UserModuleTypes getType() {
        return type;
    }
}
