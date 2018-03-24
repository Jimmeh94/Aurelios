package com.aurelios.server.util.particles.effects;

import com.aurelios.server.util.misc.Pair;
import com.aurelios.server.util.particles.ParticleData;
import com.aurelios.server.util.particles.effects.options.*;
import com.flowpowered.math.TrigMath;
import com.flowpowered.math.vector.Vector3d;

import java.util.Optional;

public class HelixHorizontalEffect extends AbstractEffect {

	private double[][][] coordinates;
	private int lines;
	private int spinner;
	private int circleCoordinates;
	private int iterator = 1;
	private double length, lengthStep, radius;

	/**
	 * This constructor is assuming you give it an EffectOptionGrowWithAbility,
	 * meaning passing in a length is pointless
	 * @param lengthStep
	 * @param radius
	 * @param lines
	 * @param effectData
	 * @param options
	 */
	public HelixHorizontalEffect(double lengthStep, double radius, int lines, ParticleData effectData, EffectOption... options) {
		super(effectData, options);

		this.lengthStep = lengthStep;
		this.radius = radius;

		init(null);
		setLines(lines);
	}

	public HelixHorizontalEffect(double length, double lengthStep, double radius, int lines, ParticleData effectData, EffectOption... options) {
		super(effectData, options);

		this.length = length;
		this.lengthStep = lengthStep;
		this.radius = radius;

		init(null);
		setLines(lines);
	}

	private void init(EffectOptionRotation rotation) {
		circleCoordinates = 120;

		Optional<EffectOption> option = getOption(EffectOption.EffectOptionTypes.GROW_WITH_ABILLITY);
		if(option.isPresent()){
			length = ((EffectOptionGrowWithAbility)option.get()).getLength();
		}

		option = getOption(EffectOption.EffectOptionTypes.GROW_RADIUS);
		if(option.isPresent()){
			radius = ((EffectOptionGrowRadius)option.get()).getStartRadius();
		}

		coordinates = new double[(int) (length / lengthStep) + 1][][];

		if(rotation != null){
			Pair<Double, Double> yAxis = rotation.getYAxis();
			Pair<Double, Double> zAxis = rotation.getZAxis();

			Vector3d add;

			for (int i = 0; i < length / lengthStep; i++) {
				coordinates[i] = new double[circleCoordinates][];
				int i2 = 0;

				double z = lengthStep * i;

				for (double a = 0; a < TrigMath.PI * 2; a += TrigMath.PI / (circleCoordinates / 2)) {
					double x = TrigMath.cos(a) * radius;
					double y = TrigMath.sin(a) * radius;

					add = rotateAroundAxisX(new Vector3d(x, y, z), zAxis.getFirst(), zAxis.getSecond());
					add = rotateAroundAxisY(add, yAxis.getFirst(), yAxis.getSecond());

					coordinates[i][i2++] = new double[] { add.getX(), add.getY(), add.getZ()};
				}

				if(option.isPresent()){
					radius += ((EffectOptionGrowRadius)option.get()).getStep();
					if(radius >= ((EffectOptionGrowRadius)option.get()).getEndRadius()){
						radius = ((EffectOptionGrowRadius)option.get()).getEndRadius();
					}
				}
			}
		} else {
			for (int i = 0; i < length / lengthStep; i++) {
				coordinates[i] = new double[circleCoordinates][];
				int i2 = 0;
				double z = lengthStep * i;

				for (double a = 0; a < TrigMath.PI * 2; a += TrigMath.PI / (circleCoordinates / 2)) {
					double x = TrigMath.cos(a) * radius;
					double y = TrigMath.sin(a) * radius;
					coordinates[i][i2++] = new double[] {x, y, z};
				}

				if(option.isPresent()){
					radius += ((EffectOptionGrowRadius)option.get()).getStep();
					if(radius >= ((EffectOptionGrowRadius)option.get()).getEndRadius()){
						radius = ((EffectOptionGrowRadius)option.get()).getEndRadius();
					}
				}
			}
		}
	}

	@Override
	public void prePlay(){
		Optional<EffectOption> option;
		option = getOption(EffectOption.EffectOptionTypes.SLEEP);
		if(option.isPresent()){
			if(((EffectOptionSleep)option.get()).isSleeping()){
				return;
			}
		}


		option = getOption(EffectOption.EffectOptionTypes.ROTATION_CASTER);
		if(option.isPresent()){
			init(((EffectOptionCasterRotation)option.get()));
		} else {
			option = getOption(EffectOption.EffectOptionTypes.STATIC_ROTATION);
			if(option.isPresent()){
				init(((EffectOptionStaticRotation)option.get()));
			}
		}

		option = getOption(EffectOption.EffectOptionTypes.ITERATE);
		if(option.isPresent()){
			iterator = ((EffectOptionIterate)option.get()).getIterator(effectData);
			iterator /= lengthStep;
			iteratePlay();
		} else {
			play();
		}
	}

	public void iteratePlay(){
		int i = 0;
		spinner++;
		Optional<EffectOption> option = getOption(EffectOption.EffectOptionTypes.CENTER_FOLLOW_CASTER);
		Optional<EffectOption> extra = getOption(EffectOption.EffectOptionTypes.CHAIN_EFFECT);

		for (double[][] array2d : coordinates) {
			if(i == iterator){
				return;
			}

			if (array2d == null) continue;
			for (int line = 0; line < lines; line++) {
				int stepPerLine = circleCoordinates / lines;
				double[] array = array2d[(((stepPerLine * line) % circleCoordinates) + (i * 2 % circleCoordinates)
						+ (circleCoordinates - 1 - ((spinner) % circleCoordinates))) % circleCoordinates];

				if(option.isPresent()){
					effectData.setDisplayAt(((EffectOptionCenterFollowCaster)option.get()).getDisplayAt(array[0], array[1], array[2]));
				} else {
					effectData.setDisplayAt(effectData.getCenter().add(array[0], array[1], array[2]));
				}

				playParticle();

				if(extra.isPresent()){
					EffectOptionChainEffect chain = ((EffectOptionChainEffect)extra.get());

					if(chain.shouldPlayHere(i, iterator)){
						chain.playEffect(effectData.getDisplayAt());
					}
				}
				effectData.getCenter().sub(array[0], array[1], array[2]);
			}
			i++;
		}
	}

	@Override
	protected void play() {
		int i = 0;
		spinner++;

		for (double[][] array2d : coordinates) {
			if (array2d == null) continue;
			for (int line = 0; line < lines; line++) {
				int stepPerLine = circleCoordinates / lines;
				double[] array = array2d[(((stepPerLine * line) % circleCoordinates) + (i * 2 % circleCoordinates)
						+ (circleCoordinates - 1 - ((spinner) % circleCoordinates))) % circleCoordinates];
				effectData.setDisplayAt(effectData.getCenter().add(array[0], array[1], array[2]));
				playParticle();
				effectData.getCenter().sub(array[0], array[1], array[2]);
			}
			i++;
		}
	}

	public HelixHorizontalEffect setLines(int i) {
		lines = i;
		return this;
	}

}