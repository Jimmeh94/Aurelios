package com.aurelios.server.game.user.modules.tools;

import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.modules.UserModuleTypes;

public class ModuleAreaCreator extends ModuleTool {

    private double radius = 10;
    private String name;
    private int aiCap;

    public ModuleAreaCreator(UserPlayer owner) {
        super(owner, UserModuleTypes.NODE_CREATOR);
    }

    @Override
    public String getDisabledMessage() {
        return "Area creator is disabled!";
    }

    @Override
    public String getEnabledMessage() {
        return "Area creator is enabled!";
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

    public void setAiCap(int aiCap) {
        this.aiCap = aiCap;
    }

    public int getAiCap() {
        return aiCap;
    }
}
