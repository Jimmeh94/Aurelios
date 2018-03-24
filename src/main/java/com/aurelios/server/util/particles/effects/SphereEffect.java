package com.aurelios.server.util.particles.effects;

import com.aurelios.server.util.particles.ParticleData;
import com.aurelios.server.util.particles.effects.options.EffectOption;

import java.util.Arrays;

public class SphereEffect extends AbstractEffect {

	private double[][][] sphereCoordinates;

	public SphereEffect(ParticleData effectData, SphereEffect particle, EffectOption... options) {
		super(effectData, options);

		sphereCoordinates = Arrays.copyOf(particle.sphereCoordinates, particle.sphereCoordinates.length);
	}

	public SphereEffect(double radius){
		super(null);

		init(radius);
	}

	private void init(double radius) {
		sphereCoordinates = new double[24][][];
		for (int i = 0; i < sphereCoordinates.length; i++) {
			sphereCoordinates[i] = new double[16][];
			double x, y, z;
			double r = Math.sin((Math.PI / 12) * i);
			for (int i2 = 0; i2 < 16; i2++) {
				double theta = i2 * (Math.PI / 8);
				x = radius * Math.cos(theta) * r;
				z = radius * Math.sin(theta) * r;
				y = radius * Math.cos((Math.PI / 12) * i);
				sphereCoordinates[i][i2] = new double[] { x, y, z };
			}
		}
	}

	@Override
	protected void play() {
		for (int i = 0; i < sphereCoordinates.length; i++)
			for (int i2 = 0; i2 < sphereCoordinates[i].length; i2++) {
				effectData.setDisplayAt(effectData.getCenter().add(sphereCoordinates[i][i2][0],
						sphereCoordinates[i][i2][1], sphereCoordinates[i][i2][2]));
				playParticle();
				effectData.getCenter().sub(sphereCoordinates[i][i2][0], sphereCoordinates[i][i2][1], sphereCoordinates[i][i2][2]);
			}
	}

}