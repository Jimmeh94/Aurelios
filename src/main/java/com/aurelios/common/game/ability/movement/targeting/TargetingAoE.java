package com.aurelios.common.game.ability.movement.targeting;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.game.ability.movement.Targeting;
import com.aurelios.common.game.user.User;
import com.aurelios.common.managers.Managers;
import com.aurelios.common.util.misc.LocationUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is an Area of Effect. Enemies or allies within the target area will be affected
 */
public abstract class TargetingAoE extends TargetingLocation{

    private double radius;
    private Targeting.TargetingFlag flag;
    private List<User> targets;

    public TargetingAoE(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget, double distance, boolean staticLocation,
               double radius, Targeting.TargetingFlag flag) {
        super(parent, endAbilityHere, playEffectAtTarget, distance, staticLocation);
        this.radius = radius;
        this.flag = flag;
    }

    public TargetingAoE(Ability parent, boolean endAbilityHere, boolean playEffectAtTarget, double distance, boolean staticLocation,
               double movementScale, double radius, Targeting.TargetingFlag flag) {
        super(parent, endAbilityHere, playEffectAtTarget, distance, staticLocation, movementScale);
        this.radius = radius;
        this.flag = flag;
    }

    @Override
    public void findTarget() {
        targets = new CopyOnWriteArrayList<>();
        for(Player player: Sponge.getServer().getOnlinePlayers()){
            if(LocationUtils.withinDistance(player.getLocation().getPosition(), target, radius)){
                //if(player.getLocation().getPosition().distance(target) <= radius){
                if(flag == Targeting.TargetingFlag.ENEMY || flag == Targeting.TargetingFlag.EITHER){
                    targets.add(Managers.USER.find(player.getUniqueId()).get());
                }
            }
        }
    }

    @Override
    public Text getChatRepresentation() {
        return Text.of(TextColors.GOLD, "AoE - " + distance + " blocks, " + radius + " radius");
    }

    public List<User> getTargets() {
        return targets;
    }
}
