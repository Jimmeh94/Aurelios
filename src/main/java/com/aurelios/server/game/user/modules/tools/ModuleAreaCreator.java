package com.aurelios.server.game.user.modules.tools;

import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.modules.UserModuleTypes;
import com.flowpowered.math.vector.Vector3d;

import javax.swing.text.Position;

public class ModuleAreaCreator extends ModuleTool {

    private double radius = -1;
    private String name;
    private int aiCap;
    private Vector3d corner1, corner2;

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

    public Vector3d getCorner1() {
        return corner1;
    }

    public Vector3d getCorner2() {
        return corner2;
    }

    public void setCorner1(Vector3d corner1) {
        this.corner1 = corner1;
    }

    public void setCorner2(Vector3d corner2) {
        this.corner2 = corner2;
    }

    public boolean isCircle(){
        return radius != -1 && (corner1 == null && corner2 == null);
    }
}
