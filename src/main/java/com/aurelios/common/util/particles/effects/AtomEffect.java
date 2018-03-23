package com.aurelios.common.util.particles.effects;

import com.aurelios.common.util.particles.ParticleData;
import com.aurelios.common.util.particles.PreloadedParticle;

import java.util.Arrays;

public class AtomEffect extends AbstractEffect {

	private double[][][] AureliosCoordinates;
	private double[][] ringCoordinates;

	public AtomEffect(double ringRadius, double AureliosRadius, double yawOffset){
		super(null);

		init(ringRadius, AureliosRadius, yawOffset);
	}

	public AtomEffect(ParticleData effectData, PreloadedParticle loaded){
		super(effectData);

		AtomEffect atomEffect = (AtomEffect)loaded.getEffect();
		AureliosCoordinates = Arrays.copyOf(atomEffect.AureliosCoordinates, atomEffect.AureliosCoordinates.length);
		ringCoordinates = Arrays.copyOf(atomEffect.ringCoordinates, atomEffect.ringCoordinates.length);
	}

	private void init(double rr, double cr, double yawOffset) {
		double x, y, z;
		AureliosCoordinates = new double[24][][];
		for (int i = 0; i < AureliosCoordinates.length; i++) {
			AureliosCoordinates[i] = new double[16][];
			double r = Math.sin((Math.PI / 12) * i);
			for (int i2 = 0; i2 < 16; i2++) {
				double theta = i2 * (Math.PI / 8);
				x = cr * Math.cos(theta) * r;
				z = cr * Math.sin(theta) * r;
				y = cr * Math.cos((Math.PI / 12) * i);
				AureliosCoordinates[i][i2] = new double[] { x, y, z };
			}
		}
		int iterationCounter = 0;
		ringCoordinates = new double[120][];
		for (int ringCounter = -1; ringCounter <= 1; ringCounter++) {
			for (int i2 = 0; i2 < 40; i2++) {
				double a = i2 * (Math.PI / 20);
				y = ringCounter == 0 ? 0 : Math.sin(a + Math.toRadians(yawOffset)) * rr * ringCounter;
				x = Math.cos(a) * rr;
				z = Math.sin(a) * rr;
				ringCoordinates[i2 + 40 * iterationCounter] = new double[] { x, y, z };
			}
			iterationCounter++;
		}
	}

	@Override
	protected void play() {
		for (double[][] firstArray : AureliosCoordinates)
			for (double[] secondArray : firstArray) {
				effectData.setDisplayAt(effectData.getCenter().add(secondArray[0], secondArray[1], secondArray[2]));
				playParticle();
				effectData.getCenter().sub(secondArray[0], secondArray[1], secondArray[2]);
			}
		for (double[] array : ringCoordinates) {
			effectData.setDisplayAt(effectData.getCenter().add(array[0], array[1], array[2]));
			playParticle();
			effectData.getCenter().sub(array[0], array[1], array[2]);
		}
	}

}