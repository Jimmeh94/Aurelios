package com.aurelios.server.game.ability.abilities.test;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.actions.UpdateAction;
import com.aurelios.server.game.ability.animation.Animation;
import com.aurelios.server.game.ability.animation.EffectAnimation;
import com.aurelios.server.game.ability.condition.AbilityCondition;
import com.aurelios.server.game.ability.condition.ConditionCollision;
import com.aurelios.server.game.ability.condition.ConditionFocus;
import com.aurelios.server.game.ability.condition.ConditionTimeLimit;
import com.aurelios.server.game.ability.condition.collision.CollisionAbility;
import com.aurelios.server.game.ability.condition.collision.CollisionEnvironment;
import com.aurelios.server.game.ability.condition.collision.CollisionUser;
import com.aurelios.server.game.ability.condition.collision.shapes.CollisionCylinder;
import com.aurelios.server.game.ability.condition.effects.ConditionEndEffectPlayMessage;
import com.aurelios.server.game.ability.movement.Targeting;
import com.aurelios.server.game.ability.movement.targeting.TargetingLocation;
import com.aurelios.server.game.ability.requirements.Requirement;
import com.aurelios.server.game.ability.requirements.StatRequirement;
import com.aurelios.server.game.user.User;
import com.aurelios.server.game.user.stats.Stat;
import com.aurelios.server.util.particles.ParticleData;
import com.aurelios.server.util.particles.ParticlePlayer;
import com.aurelios.server.util.particles.effects.HelixHorizontalEffect;
import com.aurelios.server.util.particles.effects.options.*;
import com.aurelios.server.util.text.Message;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FireDragonRoar extends Ability {

    public FireDragonRoar(User caster) {
        super(caster, Text.of(TextColors.RED, "Fire Dragon Roar"),
                Text.of(TextColors.DARK_RED, TextStyles.ITALIC, "Unleash the dragon's flame" +
                " burning within you"), 2L);
    }

    @Override
    protected List<Requirement> loadRequirements() {
        return Arrays.asList(new StatRequirement(caster, Stat.Type.MANA, 5.0));
    }

    @Override
    protected Targeting loadTargeting() {
        return new TargetingLocation(this, false, true, 50, false){

            @Override
            public void onReachTarget() {
                ParticlePlayer.display(ParticleData.builder()
                        .addViewers(getTargeting().getTracking().getCenter(), 100)
                        .center(getTargeting().getTargetLocation())
                        .quantity(8)
                        .offsets(2, 2, 2)
                        .type(ParticleTypes.HUGE_EXPLOSION)
                        .build());
            }
        };
    }

    @Override
    protected List<AbilityCondition> loadConditions() {
        return Arrays.asList(
                new ConditionCollision(
                        new CollisionCylinder(this, 1.5),
                        new CollisionEnvironment(this, false, BlockTypes.AIR, BlockTypes.STONE) {

                            @Override
                            protected void doEffect() {
                                playParticlesAtCollisionPoints(ParticleData.builder()
                                        .quantity(8)
                                        .offsets(2, 2, 2)
                                        .type(ParticleTypes.HUGE_EXPLOSION)
                                        .build());
                            }
                        },
                        new CollisionAbility(this, 50.0, false){

                            @Override
                            protected void doEffect() {
                                playParticlesAtCollisionPoints(ParticleData.builder()
                                        .quantity(8)
                                        .offsets(2, 2, 2)
                                        .type(ParticleTypes.HUGE_EXPLOSION)
                                        .build());
                            }
                        },
                        new CollisionUser(this, true, true, CollisionUser.UserCollisionTypes.ENEMY) {
                            @Override
                            protected void doEffect() {
                                playParticlesAtCollisionPoints(ParticleData.builder()
                                        .quantity(8)
                                        .offsets(2, 2, 2)
                                        .type(ParticleTypes.HUGE_EXPLOSION)
                                        .build()
                                );
                            }
                        }
                ),
                caster.isPlayer() ?
                new ConditionFocus(this, 1.0, Stat.Type.MANA,
                        new ConditionEndEffectPlayMessage(this,
                            Message.builder().addReceiver((Player) caster.getEntity())
                                    .addMessage(Text.of("FOCUS")).build()
                        )
                ) : new ConditionFocus(this, 1.0, Stat.Type.MANA),
                caster.isPlayer() ?
                new ConditionTimeLimit(40,
                        new ConditionEndEffectPlayMessage(this,
                            Message.builder().addReceiver((Player) caster.getEntity())
                                    .addMessage(Text.of("TIME")).build()
                        )
                ) : new ConditionTimeLimit(40)
        );
    }

    @Override
    protected Optional<Animation[]> loadAnimations() {
        return Optional.of(new Animation[]{
                new EffectAnimation(this,
                        new HelixHorizontalEffect(0.25, 0.75, 15,
                                ParticleData.builder()
                                    .addViewers(caster.getEntity().getLocation().getPosition(), 100)
                                    .center(caster.getEntity().getLocation().getPosition().add(0, 1, 0))
                                    .quantity(1)
                                    .ability(this)
                                    .build(),
                                new EffectOptionIterate(),
                                new EffectOptionCasterRotation(caster),
                                new EffectOptionCenterFollowCaster(caster),
                                new EffectOptionGrowWithAbility(this),
                                new EffectOptionGrowRadius(0.1, 0.75, 0.03),
                                new EffectOptionChainEffect(
                                    ParticleData.builder()
                                            .addViewers(caster.getEntity().getLocation().getPosition(), 100)
                                            .quantity(12)
                                            .offsets(0.6, 0.6, 0.6)
                                            .build(), EffectOptionChainEffect.Where.END
                                )
                        )
                ),
                new EffectAnimation(this, new HelixHorizontalEffect(0.1, 1.5, 2,
                        ParticleData.builder()
                                .addViewers(caster.getEntity().getLocation().getPosition(), 100)
                                .center(caster.getEntity().getLocation().getPosition().add(0, 1, 0))
                                .quantity(1)
                                .type(ParticleTypes.SMOKE)
                                .ability(this)
                                .build(),
                        new EffectOptionIterate(), new EffectOptionCasterRotation(caster), new EffectOptionCenterFollowCaster(caster),
                        new EffectOptionGrowWithAbility(this), new EffectOptionGrowRadius(0.2, 1.0, 0.03))
                ),
                new EffectAnimation(this, new HelixHorizontalEffect(0.5, 0.05, 1.0, 5,
                        ParticleData.builder()
                                .addViewers(caster.getEntity().getLocation().getPosition(), 100)
                                .center(caster.getEntity().getLocation().getPosition().add(0, 1, 0))
                                .quantity(1)
                                .type(ParticleTypes.FLAME)
                                .ability(this)
                                .build(),
                        new EffectOptionCasterRotation(caster), new EffectOptionCenterFollowCaster(caster))
                )
                /*new EffectAnimation(this, new SphereEffect(
                        ParticleData.builder()
                                .addViewers(caster.getEntity().getLocation().getPosition(), 100)
                                .quantity(5)
                                .offsets(0.3, 0.3, 0.3)
                                .ability(this)
                                .build(),
                        PreloadedParticle.SPHERE_1R)
                )*/
            }
        );
    }

    @Override
    protected Optional<UpdateAction> loadUpdateAction() {
        return Optional.empty();
    }
}
