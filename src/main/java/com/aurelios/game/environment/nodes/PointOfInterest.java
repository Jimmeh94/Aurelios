package com.aurelios.game.environment.nodes;

import com.aurelios.game.environment.nodes.shapes.Shape;
import com.aurelios.game.user.ai.traits.Role;
import com.aurelios.util.misc.StringUtils;
import org.spongepowered.api.text.Text;

import java.util.UUID;

/**
 * Points of interest are specific locations within a node. Examples:
 * taverns, public sitting areas, blacksmith, stables, etc.
 */
public class PointOfInterest extends Area{

    private Node parent;

    //This is used for Tasks in the AI. This lets it know what roles can find something to do here.
    //For example, a Blacksmith POI would have a Blacksmith Role
    private Role[] availableRoles;

    public PointOfInterest(UUID uuid, Text displayName, Shape shape, Node parent, Role... availableRoles) {
        super(displayName, shape, uuid);
        this.parent = parent;
        this.availableRoles = availableRoles;
    }

    public PointOfInterest(UUID uuid, Text displayName, Shape shape, Node parent, String availableRoles) {
        super(displayName, shape, uuid);
        this.parent = parent;
        if(availableRoles != null) {
            String[] temp = availableRoles.split(",");
            this.availableRoles = new Role[temp.length];
            for (int i = 0; i < temp.length; i++) {
                this.availableRoles[i] = Role.valueOf(temp[i].toUpperCase());
            }
        }
    }

    public boolean hasRole(Role role){
        if(availableRoles == null){
            return false;
        } else {
            for(Role r: availableRoles){
                if(r == role){
                    return true;
                }
            }
        }
        return false;
    }

    public Node getParent() {
        return parent;
    }

    public Role[] getAvailableRoles() {
        return availableRoles;
    }

    public String getAvailableRolesAsString() {
        String give = "";
        if(availableRoles != null) {
            for (int i = 0; i < availableRoles.length; i++) {
                give += StringUtils.enumToString(availableRoles[i], false);
                if(i < availableRoles.length - 1){
                    give += ",";
                }
            }
        }
        return give;
    }
}
