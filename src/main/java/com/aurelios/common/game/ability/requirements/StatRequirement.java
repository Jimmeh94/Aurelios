package com.aurelios.common.game.ability.requirements;

import com.aurelios.common.game.user.User;
import com.aurelios.common.game.user.UserPlayer;
import com.aurelios.common.game.user.stats.Stat;
import com.aurelios.common.util.text.Message;
import com.aurelios.common.util.text.Messager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StatRequirement implements Requirement {

    private User owner;
    private Stat.Type type;
    private double required;

    public StatRequirement(User owner, Stat.Type type, double required) {
        this.owner = owner;
        this.type = type;
        this.required = required;
    }

    public Stat.Type getType() {
        return type;
    }

    public double getRequired() {
        return required;
    }

    @Override
    public boolean requirementsMet() {
        if(owner.getStats().has(type)){
            if(owner.getStats().find(type).get().getAmount() >= required){
                return true;
            } else return false;
        } else return false;
    }

    @Override
    public void makeRequirementCost() {
        owner.getStats().find(type).get().subtract(required);
    }

    @Override
    public void printFailureMessage() {
        if(owner.isPlayer()){
            Messager.sendMessage(Message.builder().addReceiver(((UserPlayer)owner).getPlayer())
                    .addMessage(Text.of(TextColors.RED, "You don't have enough " + type.toString() + "!"), Messager.Prefix.ERROR).build());
        }
    }

    @Override
    public Text getChatRepresentation() {
        return Text.of(TextColors.GOLD, required + " " + type.toString());
    }
}
