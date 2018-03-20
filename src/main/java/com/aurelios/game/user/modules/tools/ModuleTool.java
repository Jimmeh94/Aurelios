package com.aurelios.game.user.modules.tools;

import com.aurelios.game.user.UserPlayer;
import com.aurelios.game.user.modules.UserModule;
import com.aurelios.game.user.modules.UserModuleTypes;

public abstract class ModuleTool extends UserModule {

    public ModuleTool(UserPlayer owner, UserModuleTypes type) {
        super(owner, type);
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void enable() {
        super.enable();

        for(UserModule module: getOwner().getModules()){
            if(module instanceof ModuleTool && module != this && module.isEnabled()){
                module.disable();
            }
        }
    }
}
