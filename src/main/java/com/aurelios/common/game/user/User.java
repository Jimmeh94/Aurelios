package com.aurelios.common.game.user;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.game.ability.enums.AbilityConditionType;
import com.aurelios.common.game.environment.nodes.Area;
import com.aurelios.common.game.environment.nodes.Node;
import com.aurelios.common.game.environment.nodes.PointOfInterest;
import com.aurelios.common.game.environment.nodes.WildernessNode;
import com.aurelios.common.game.user.stats.Stat;
import com.aurelios.common.game.user.stats.StatCollection;
import com.aurelios.common.managers.Managers;
import org.spongepowered.api.entity.living.Living;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An User is simply any "living" thing in the world that needs this sort of data. Isn't necessarily only players.
 */
public class User {

    private UUID uuid;
    private StatCollection stats;
    private Area currentArea;
    private Living entity;
    private List<Ability> activeAbilities;

    public User(Living entity){
        Managers.USER.add(this);

        this.uuid = entity.getUniqueId();
        this.entity = entity;

        stats = new StatCollection(this);
        stats.add(new Stat(Stat.Type.MANA, 100, 100));
        stats.add(new Stat(Stat.Type.HEALTH, 100, 100));

        if(!(this instanceof UserPlayer))
            updateCurrentArea();

        activeAbilities = new CopyOnWriteArrayList<>();
    }

    /**
     * This will mainly be used for NPCs
     * @param entity
     */
    public void migrateEntity(Living entity){
        this.uuid = entity.getUniqueId();
        this.entity = entity;
    }

    public boolean isPlayer(){
        return this instanceof UserPlayer;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void tick() {
        updateCurrentArea();
    }

    protected void updateCurrentArea(){
        Optional<Node> node = Managers.NODES.findNode(entity.getLocation());

        if(node.isPresent()){
            //included the below if statement so that the extra code wouldn't run if the player
            //hasn't entered a different area from the one they're already in.
            //Perhaps move to an event based system for entering/exiting nodes to replace the if statement
            //if(currentArea == null || (currentArea != null && currentArea != node.get())) {

                if(!(node.get() instanceof WildernessNode)){
                    currentArea = node.get().getContainingArea(entity.getLocation()).get();
                } else currentArea = node.get();

                if(this instanceof UserPlayer){
                    ((UserPlayer)this).getScoreboard().updateScoreboard();
                }
            //}
        }
    }

    public StatCollection getStats() {
        return stats;
    }

    public void cleanUp() {
        Managers.USER.remove(this);
    }

    public Node getCurrentNode() {
        if(currentArea != null){
            if(currentArea instanceof Node){
                return (Node) currentArea;
            } else return ((PointOfInterest)currentArea).getParent();
        } else return null;
    }

    public Living getEntity() {
        return entity;
    }

    public void addAbility(Ability ability) {
        activeAbilities.add(ability);
    }

    public void removeAbility(Ability ability){
        activeAbilities.remove(ability);
    }

    public void endAllAbilities(){
        for(Ability ability: activeAbilities){
            ability.stop(AbilityConditionType.DEATH);
        }
    }

    public Area getCurrentArea() {
        return currentArea;
    }
}
