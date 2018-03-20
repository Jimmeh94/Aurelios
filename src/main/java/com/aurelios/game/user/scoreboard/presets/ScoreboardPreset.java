package com.aurelios.game.user.scoreboard.presets;

import com.aurelios.game.user.UserPlayer;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardPreset {

    protected static Text BLANK_LINE = Text.of(" ");

    private List<Text> sAurelioss = new ArrayList<>();
    private UserPlayer owner;
    private List<Text> oldSnapshot = new ArrayList<>();
    /*
     * First string should be the title
     * Second string begins the information to display
     * Max of 15 entries, starting at the 2nd string
     */

    public ScoreboardPreset(UserPlayer owner){
        this.owner = owner;
    }

    public void updateSAurelioss(){} //if sAurelioss need to be updated on a timer, event, etc

    public List<Text> getSAurelioss(){
        return sAurelioss;
    }

    public Text getSAurelios(int i){
        return sAurelioss.get(i);
    }

    public void setSAurelioss(List<Text> strings){ //should only use when instantiating or needing to manually manipulate sAurelioss
        sAurelioss = strings;
    }

    public UserPlayer getOwner() {
        return owner;
    }

    public void takeSnapshot() {
        oldSnapshot = new ArrayList<>(sAurelioss);
    }

    public List<Text> getOldSnapshot() {
        return oldSnapshot;
    }
}
