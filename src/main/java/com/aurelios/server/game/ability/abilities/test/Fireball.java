package com.aurelios.server.game.ability.abilities.test;

import com.aurelios.server.game.ability.Ability;
import com.aurelios.server.game.ability.actions.UpdateAction;
import com.aurelios.server.game.ability.animation.Animation;
import com.aurelios.server.game.ability.animation.AnimationTrail;
import com.aurelios.server.game.ability.animation.EffectAnimation;
import com.aurelios.server.game.ability.condition.AbilityCondition;
import com.aurelios.server.game.ability.condition.ConditionCollision;
import com.aurelios.server.game.ability.condition.ConditionTimeLimit;
import com.aurelios.server.game.ability.condition.collision.CollisionAbility;
import com.aurelios.server.game.ability.condition.collision.CollisionEnvironment;
import com.aurelios.server.game.ability.condition.collision.CollisionUser;
import com.aurelios.server.game.ability.condition.collision.shapes.CollisionSphere;
import com.aurelios.server.game.ability.movement.Targeting;
import com.aurelios.server.game.ability.movement.targeting.TargetingLocation;
import com.aurelios.server.game.ability.requirements.Requirement;
import com.aurelios.server.game.ability.requirements.StatRequirement;
import com.aurelios.server.game.user.User;
import com.aurelios.server.game.user.stats.Stat;
import com.aurelios.server.util.particles.ParticleData;
import com.aurelios.server.util.particles.ParticlePlayer;
import com.aurelios.server.util.particles.PreloadedParticle;
import com.aurelios.server.util.particles.effects.SphereEffect;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fireball extends Ability {

    public Fireball(User caster) {
        super(caster, Text.of(TextColors.RED, "Fireball"),
                Text.of(TextColors.DARK_RED, TextStyles.ITALIC, "Shoot a fireball"),2L);
    }

    @Override
    protected List<Requirement> loadRequirements() {
        return Arrays.asList(new StatRequirement(caster, Stat.Type.MANA, 5.0));
    }

    @Override
    protected Targeting loadTargeting() {
        return new TargetingLocation(this, true, true, 100, true, 1.0) {

            @Override
            public void onReachTarget() {
                ParticlePlayer.display(ParticleData.builder()
                        .addViewers(getTracking().getCenter(),100)
                        .center(getTargetLocation())
                        .quantity(3)
                        .offsets(0.5, 0.5, 0.5)
                        .type(ParticleTypes.HUGE_EXPLOSION)
                        .build());
            }
        };
    }

    @Override
    protected List<AbilityCondition> loadConditions() {
        return Arrays.asList(
                new ConditionCollision(
                    new CollisionSphere(this, 2.0),
                    new CollisionEnvironment(this, true, BlockTypes.AIR, BlockTypes.STONE){

                        @Override
                        protected void doEffect() {

                        }
                    },
                    new CollisionAbility(this, 5.0, true){

                        @Override
                        protected void doEffect() {
                            playParticlesAtCollisionPoints(ParticleData.builder()
                                    .quantity(8)
                                    .offsets(2, 2, 2)
                                    .type(ParticleTypes.HUGE_EXPLOSION)
                                    .build());
                        }
                    },
                    new CollisionUser(this, true, false, CollisionUser.UserCollisionTypes.ENEMY){

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
                new ConditionTimeLimit(5)
        );
    }

    @Override
    protected Optional<Animation[]> loadAnimations() {
        return Optional.of(new Animation[]{
                new EffectAnimation(this,
                        new SphereEffect(ParticleData.builder()
                                .addViewers(caster.getEntity().getLocation().getPosition(), 100)
                                .quantity(10)
                                .ability(this)
                                .build(),
                                PreloadedParticle.SPHERE_025R)),
                new AnimationTrail(this, 5, ParticleTypes.LARGE_SMOKE, 25, 4)}
        );
    }

    @Override
    protected Optional<UpdateAction> loadUpdateAction() {
        return Optional.empty();
    }
}
