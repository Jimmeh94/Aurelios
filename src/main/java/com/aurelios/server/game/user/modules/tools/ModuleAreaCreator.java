package com.aurelios.server.game.user.modules.tools;

import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.modules.UserModuleTypes;
import com.aurelios.server.util.text.Message;
import com.aurelios.server.util.text.Messager;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.swing.text.Position;
import java.util.Optional;

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
        Messager.broadcastMessage(Text.of(TextColors.GREEN, "Radius set to " + radius), Optional.of(Messager.Prefix.SUCCESS));
    }

    public double getRadius() {
        return radius;
    }

    public void setName(String name) {
        this.name = name;
        Messager.broadcastMessage(Text.of(TextColors.GREEN, "Name set to " + name), Optional.of(Messager.Prefix.SUCCESS));
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
        Messager.broadcastMessage(Text.of(TextColors.GREEN, "Corner 1 set!"), Optional.of(Messager.Prefix.SUCCESS));
    }

    public void setCorner2(Vector3d corner2) {
        this.corner2 = corner2;
        Messager.broadcastMessage(Text.of(TextColors.GREEN, "Corner 2 set!"), Optional.of(Messager.Prefix.SUCCESS));
    }

    public boolean isCircle(){
        return radius != -1 && (corner1 == null && corner2 == null);
    }
}
