package com.aurelios.util.particles.effects;


import com.aurelios.Aurelios;
import com.aurelios.util.particles.AnimationData;
import com.aurelios.util.particles.ParticleData;
import com.aurelios.util.particles.ParticlePlayer;
import com.aurelios.util.particles.effects.options.EffectOption;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEffect {

	protected ParticleData effectData;
	protected List<EffectOption> effectOptions = new ArrayList<>();

	public AbstractEffect(ParticleData effectData, EffectOption... options) {
		this.effectData = effectData;
		if(options != null){
			effectOptions = Arrays.asList(options);
		}
	}

	public AbstractEffect(ParticleData effectData, List<EffectOption> effectOptions) {
		this.effectData = effectData;
		this.effectOptions = effectOptions;
	}

	protected <T> Optional<T> getOption(EffectOption.EffectOptionTypes types){
		for(EffectOption option: effectOptions){
			if(option.getType() == types){
				return Optional.of((T) option);
			}
		}
		return Optional.empty();
	}

	/**
	 * An abstract method used to display the animation.
	 */
	protected abstract void play();

	/**
	 * Starts the runnable, which makes the effect display itself every interval.
	 * This would be used outside of the effect being in an ability
	 * 
	 * @return The current instance of the effect to allow chaining of methods.
	 */
	public AbstractEffect start() {
		Optional<EffectOption> option = getOption(EffectOption.EffectOptionTypes.ANIMATION_DATA);
		if(!option.isPresent()){
			return this;
		}

		AnimationData data = (AnimationData) option.get().getValue();
		Task.Builder taskBuilder = data.getTaskBuilder();
		data.setTask(taskBuilder.delayTicks(data.getDelay()).intervalTicks(data.getInterval()).execute(
				new Runnable() {
					int c = 0;

					@Override
					public void run() {
						prePlay();
						c++;
						if (c >= data.getCancel() && data.getCancel() != -1)
							stop();
					}
				}
		).submit(Aurelios.INSTANCE));

		return this;
	}

	public void prePlay(){
		syncAbilityCenter();
		play();
	}

	/**
	 * This will ensure that the center the effect uses is copied from the updated center of the ability it represents
	 */
	protected void syncAbilityCenter(){
		if(effectData.getAbility().isPresent()){
			effectData.setCenter(getEffectData().getAbility().get().getTargeting().getTracking().getCenter().clone());
		}
	}

	/**
	 * Stops the effect from automaticly displaying.
	 * 
	 * @return The current instance of the effect, to allow 'chaining' of
	 *         methods.
	 */
	public AbstractEffect stop() {
		Optional<EffectOption> option = getOption(EffectOption.EffectOptionTypes.ANIMATION_DATA);
		if(!option.isPresent()){
			return this;
		}

		AnimationData data = (AnimationData) option.get().getValue();

		Task task = data.getTask();
		if (task == null)
			return this;
		try {
			task.cancel();
		} catch (IllegalStateException exc) {
		}
		return this;
	}

	/**
	 * Spawns a particle using the set particle effect.
	 */
	protected void playParticle(){
		ParticlePlayer.display(effectData);
	}

	public ParticleData getEffectData() {
		return effectData;
	}

	protected Vector3d rotateAroundAxisX(Vector3d v, double cos, double sin) {
		double y = v.getY() * cos - v.getZ() * sin;
		double z = v.getY() * sin + v.getZ() * cos;
		return new Vector3d(v.getX(), y, z);
	}

	protected Vector3d rotateAroundAxisY(Vector3d v, double cos, double sin) {
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return new Vector3d(x, v.getY(), z);
	}

	protected Vector3d rotateAroundAxisZ(Vector3d v, double cos, double sin) {
		double x = v.getX() * cos - v.getY() * sin;
		double y = v.getX() * sin + v.getY() * cos;
		return new Vector3d(x, y, v.getZ());
	}

	/*public float[] vectorToYawPitch(Vector3d v) {
		Location loc = new Location(null, 0, 0, 0);
		loc.setDirection(v);
		//return new float[] { loc.getYaw(), loc.getPitch() };
		return new float[] { v.getYaw(), loc.getPitch() };
	}*/

	public Vector3d yawPitchToVector(float yaw, float pitch) {
		yaw += 90;
		return new Vector3d(Math.cos(Math.toRadians(yaw)), Math.sin(Math.toRadians(pitch)),
				Math.sin(Math.toRadians(yaw)));
	}
}