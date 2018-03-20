package com.aurelios.game.ability.requirements;

import org.spongepowered.api.text.Text;

/**
 * This is a general requirement check for active-cast abilities, meaning the player has to cast it
 */
public interface Requirement {

    boolean requirementsMet();

    void makeRequirementCost();

    void printFailureMessage();

    Text getChatRepresentation();
}
