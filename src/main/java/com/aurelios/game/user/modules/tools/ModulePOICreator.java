package com.aurelios.game.user.modules.tools;

import com.aurelios.game.user.UserPlayer;
import com.aurelios.game.user.ai.traits.Role;
import com.aurelios.game.user.modules.UserModuleTypes;

public class ModulePOICreator extends ModuleTool {

    private double radius;
    private String name;
    private Role[] roles;

    public ModulePOICreator(UserPlayer owner) {
        super(owner, UserModuleTypes.POI_CREATOR);
    }

    @Override
    public String getDisabledMessage() {
        return "POI Creator disabled!";
    }

    @Override
    public String getEnabledMessage() {
        return "POI Creator enabled!";
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRoles(String roles) {
        String[] temp = roles.split(",");
        this.roles = new Role[temp.length];
        for(int i = 0; i < temp.length; i++){
            this.roles[i] = Role.valueOf(temp[i].toUpperCase());
        }
    }

    public Role[] getRoles() {
        return roles;
    }
}
