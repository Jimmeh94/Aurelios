package com.aurelios.game.ability;

import com.aurelios.game.ability.actions.UpdateAction;
import com.aurelios.game.ability.animation.Animation;
import com.aurelios.game.ability.condition.AbilityCondition;
import com.aurelios.game.ability.condition.ConditionCollision;
import com.aurelios.game.ability.condition.ConditionTimeLimit;
import com.aurelios.game.ability.condition.collision.Collision;
import com.aurelios.game.ability.condition.collision.CollisionAbility;
import com.aurelios.game.ability.condition.effects.ConditionEndEffect;
import com.aurelios.game.ability.enums.AbilityConditionType;
import com.aurelios.game.ability.enums.AbilityStage;
import com.aurelios.game.ability.movement.Targeting;
import com.aurelios.game.ability.requirements.Requirement;
import com.aurelios.game.user.User;
import com.aurelios.game.user.UserPlayer;
import com.aurelios.managers.AbilityManager;
import com.aurelios.managers.Managers;
import com.aurelios.util.text.AltCodes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public abstract class Ability {

    protected User caster;
    protected AbilityStage stage = AbilityStage.PRECAST;
    protected Text name, description, chatRepresentation;
    protected Ability abilityContainer;

    //Pre-cast requirements
    protected List<Requirement> requirements;

    //Cast
    //protected CastType castType; (instant, timed (animation), action sequence)

    //Post-cast, types of targeting this ability has
    protected Targeting targeting;

    //Active update, for the update method called in AbilityManager
    protected Long delay, interval;
    protected Optional<Animation[]> animation;
    //Currently, condition are basically completion conditions alongside reaching the ability's target
    protected List<AbilityCondition> conditions;
    //This allows you to add more functionality during update, like draining a mana stat for a "focused" ability
    protected Optional<UpdateAction> updateAction = Optional.empty();

    //================================================================

    protected abstract List<Requirement> loadRequirements();
    protected abstract Targeting loadTargeting();
    protected abstract List<AbilityCondition> loadConditions();
    protected abstract Optional<Animation[]> loadAnimations();
    protected abstract Optional<UpdateAction> loadUpdateAction();

    public Ability(User caster, Text name, Text description, long interval){
        this.caster = caster;
        this.name = name;
        this.description = description;
        this.interval = interval;

        requirements = loadRequirements();
        targeting = loadTargeting();
        conditions = loadConditions();
        animation = loadAnimations();
        updateAction = loadUpdateAction();

        createChatRepresentation();
    }

    public void createChatRepresentation(){
        Text.Builder builder = Text.builder();
        builder.append(description);
        builder.append(Text.of('\n'));
        builder.append(Text.of('\n'));

        for(Requirement requirement: requirements){
            builder.append(Text.of(TextColors.GRAY, "Requirement: ")).append(requirement.getChatRepresentation());
            builder.append(Text.of('\n'));
        }

        builder.append(Text.of(TextColors.GRAY, "Targeting: ")).append(targeting.getChatRepresentation());
        builder.append(Text.of('\n'));
        builder.append(Text.of('\n'));

        for(AbilityCondition condition: conditions){
            builder.append(Text.of(TextColors.GRAY, AltCodes.BULLET_POINT.getSign() + " ")).append(condition.getChatRepresentation());
            builder.append(Text.of('\n'));
        }

        chatRepresentation = Text.builder().append(name).onHover(TextActions.showText(builder.build()))
                .build();
    }

    public void fire(){
        if(stage == AbilityStage.PRECAST){
            /**
             * ================================= Pre-cast stage ======================================
             */
            //Check that the requirements are met and make the costs
            for(Requirement requirement: requirements){
                if(!requirement.requirementsMet()){
                    requirement.printFailureMessage();
                    return;
                }
            }

            for(Requirement requirement: requirements){
                requirement.makeRequirementCost();
            }

            if(caster.isPlayer()){
                ((UserPlayer)caster).getSAureliosboard().updateSAureliosboard();
            }

            /**
             * ================================= Casting stage =======================================
             */
            stage = AbilityStage.CASTING;

            //cast the ability. This just deals with the immediate casting action

            /**
             * ================================= Post-cast stage =====================================
             */
            stage = AbilityStage.POSTCAST;

            //acquire targets
            targeting.determineTargets();
            //TODO perhaps want to make sure a User wasn't targeted twice, or just add extra flags to specific modes

            /**
             * ================================= Active update stage =================================
             */
            stage = AbilityStage.ACTIVE_UPDATE;

            Managers.ABILITY.add(new AbilityManager.Entry(this, interval));
            caster.addAbility(this);

            //If this ability has a time limit, start tracking the time
            if(hasCondition(AbilityConditionType.TIME_LIMIT_REACHED)){
                ((ConditionTimeLimit)getCondition(AbilityConditionType.TIME_LIMIT_REACHED).get()).start();
            }

            targeting.getTracking().setCenter(caster.getEntity().getLocation().getPosition().clone().add(0, 1, 0));

            update();
        }
    }

    public Optional<AbilityCondition> getCondition(AbilityConditionType type) {
        for(AbilityCondition condition: conditions){
            if(condition.getConditionType() == type)
                return Optional.of(condition);
        }
        return Optional.empty();
    }

    public boolean hasCondition(AbilityConditionType type) {
        for(AbilityCondition condition: conditions){
            if(condition.getConditionType() == type)
                return true;
        }
        return false;
    }

    public void stop(AbilityConditionType cancelType){
        System.out.println(cancelType.toString());
        if(stage == AbilityStage.STOPPED)
            return;

        stage = AbilityStage.STOPPED;
        if(cancelType == AbilityConditionType.REACHED_TARGET){
            targeting.onReachTarget();
            Managers.ABILITY.remove(this);

        } else {
            for (AbilityCondition condition : conditions) {
                if (condition.getConditionType() == cancelType) {
                    if(condition instanceof ConditionCollision){
                        ConditionCollision collision = (ConditionCollision) condition;
                        for(Collision c: collision.getCollisions()){
                            if(c.getCollisionReport().isPresent()){
                                System.out.println(c.getCollisionReport().get().getClass().getCanonicalName());
                            }
                        }
                    }
                    if(condition.getCancelEffects() != null){
                        for(ConditionEndEffect effect: condition.getCancelEffects()){
                            effect.doEffect();
                        }
                    }

                }
            }

            if(cancelType != AbilityConditionType.DEATH){
                caster.removeAbility(this);
            }
            Managers.ABILITY.remove(this);
        }

        targeting.getTracking().reset();
    }

    public void update(){
        if(updateAction.isPresent() && updateAction.get().getWhen() == UpdateAction.UpdateActionWhen.FIRST){
            updateAction.get().doAction();
        }

        boolean skipTargetingUpdate = false;
        if(hasCondition(AbilityConditionType.COLLISION)){
            ConditionCollision collision = (ConditionCollision) getCondition(AbilityConditionType.COLLISION).get();
            for(Collision collision1: collision.getCollisions()){
                if(collision1 instanceof CollisionAbility){
                    if(collision.isColliding()){
                        skipTargetingUpdate = true;
                    }
                }
            }
        }

        /**
         * This basically is saying that if this ability isn't colliding with another, continue moving
         * towards target. Else, the ability is "frozen" in its tracks
         */
        if(!skipTargetingUpdate) {
            if (updateAction.isPresent() && updateAction.get().getWhen() == UpdateAction.UpdateActionWhen.OVERWRITE_TARGETING) {
                updateAction.get().doAction();
            } else targeting.update();
        } else {
        }

        if(animation.isPresent()){
            for(Animation a: animation.get()){
                a.draw();
            }
        }

        if(updateAction.isPresent() && updateAction.get().getWhen() == UpdateAction.UpdateActionWhen.LAST){
            updateAction.get().doAction();
        }

        //check finishing conditions (collision, reach target, interval max in runnable), apply effect if finished and necessary
        for(AbilityCondition condition: conditions){
            if(condition.isComplete()){
                System.out.println(condition.getClass().getCanonicalName());
                stop(condition.getConditionType());
            }
        }
        if(stage != AbilityStage.STOPPED && targeting.hasReachedTarget()) {
            if (targeting.shouldEndAbilityHere()) {
                stop(AbilityConditionType.REACHED_TARGET);
            } else if(targeting.shouldPlayEffectAtTarget()){
                targeting.onReachTarget();
            }
        }
    }

    public User getCaster() {
        return caster;
    }

    public Targeting getTargeting() {
        return targeting;
    }

    public Ability getContainer() {
        return abilityContainer;
    }

    public Text getChatRepresentation() {
        return chatRepresentation;
    }

    public Long getInterval() {
        return interval;
    }

    public Text getName() {
        return name;
    }
}
