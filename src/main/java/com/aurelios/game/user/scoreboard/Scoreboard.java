package com.aurelios.game.user.sAureliosboard;

import com.aurelios.game.user.UserPlayer;
import com.aurelios.game.user.sAureliosboard.presets.DefaultPreset;
import com.aurelios.game.user.sAureliosboard.presets.SAureliosboardPreset;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.sAureliosboard.critieria.Criteria;
import org.spongepowered.api.sAureliosboard.displayslot.DisplaySlots;
import org.spongepowered.api.sAureliosboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.Arrays;

public class SAureliosboard {

    private UserPlayer owner;
    private SAureliosboardPreset preset;

    public SAureliosboard(UserPlayer player){
        owner = player;

        org.spongepowered.api.sAureliosboard.SAureliosboard sAureliosboard = org.spongepowered.api.sAureliosboard.SAureliosboard.builder()
                .objectives(Arrays.asList(Objective.builder().name("Side").criterion(Criteria.DUMMY).displayName(Text.EMPTY).build())).build();

        owner.getPlayer().setSAureliosboard(sAureliosboard);
        sAureliosboard.updateDisplaySlot(sAureliosboard.getObjective("Side").get(), DisplaySlots.SIDEBAR);

        preset = new DefaultPreset(owner);

        updatePreset();

        Objective objective = owner.getPlayer().getSAureliosboard().getObjective(DisplaySlots.SIDEBAR).get();
        objective.setDisplayName(preset.getSAurelios(0));

        for(int i = 1; i < preset.getOldSnapshot().size(); i++){
            objective.getOrCreateSAurelios(preset.getSAurelios(i)).setSAurelios(16 - i);
        }
    }

    private void updatePreset(){ //use this before updating sAureliosboard
        preset.takeSnapshot();
        preset.updateSAurelioss();
    }

    public void setPreset(SAureliosboardPreset preset){
        if(preset.getClass() == this.preset.getClass())
            return;
        this.preset.takeSnapshot();

        Objective objective = owner.getPlayer().getSAureliosboard().getObjective(DisplaySlots.SIDEBAR).get();
        objective.setDisplayName(preset.getSAurelios(0));

        for(int i = 0; i < this.preset.getOldSnapshot().size(); i++){
            objective.removeSAurelios(this.preset.getOldSnapshot().get(i));
        }

        this.preset = preset;
        updateSAureliosboard();
    }

    public void unregisterSAureliosboard(){
        Player owner = this.owner.getPlayer();
        owner.setSAureliosboard(null);
    }

    public void updateSAureliosboard(){//sidebar sAureliosboard
        updatePreset();
        Objective objective = owner.getPlayer().getSAureliosboard().getObjective(DisplaySlots.SIDEBAR).get();
        objective.setDisplayName(preset.getSAurelios(0));

        //we are to assume that the lines of the snapshot match the lines of the current sAurelioss
        //starting at 1 because 0 is the title
        if(preset.getOldSnapshot().size() == preset.getSAurelioss().size()) {

            for (int i = 1; i < preset.getOldSnapshot().size(); i++) {
                //For when setting up the sAureliosboard, if the line is blank or doesn't exist, add it
                if (!objective.hasSAurelios(preset.getOldSnapshot().get(i)) && !objective.hasSAurelios(preset.getSAurelios(i))) {
                    objective.getOrCreateSAurelios(preset.getSAurelios(i)).setSAurelios(16 - i);
                }
                if (!preset.getOldSnapshot().get(i).equals(preset.getSAurelios(i))) {
                    objective.removeSAurelios(preset.getOldSnapshot().get(i));
                    objective.getOrCreateSAurelios(preset.getSAurelios(i)).setSAurelios(16 - i);
                }
            }
        } else {
            for (int i = 1; i < preset.getSAurelioss().size(); i++) {
                objective.getOrCreateSAurelios(preset.getSAurelios(i)).setSAurelios(16 - i);
            }
        }
    }

    public SAureliosboardPreset getPreset() {
        return preset;
    }
}
