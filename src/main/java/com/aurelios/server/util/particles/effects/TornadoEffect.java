package com.aurelios.server.util.particles.effects;

import com.aurelios.server.util.particles.ParticleData;
import com.aurelios.server.util.particles.PreloadedParticle;
import org.spongepowered.api.util.Direction;

import java.util.Arrays;
import java.util.Optional;

public class TornadoEffect extends AbstractEffect {

	private double[][][] coordinates;
	private int spinner;
	private int lines;

	public TornadoEffect(double height, double heightStep, double maxRadius, int lines, Optional<Direction> direction) {
		super(null);

		spinner = 0;
		init(height, heightStep, maxRadius);
		setLines(lines);
	}

	public TornadoEffect(ParticleData effectData, PreloadedParticle loaded){
		super(effectData);

		TornadoEffect tornadoEffect = (TornadoEffect)loaded.getEffect();
		coordinates = Arrays.copyOf(tornadoEffect.coordinates, tornadoEffect.coordinates.length);
		lines = tornadoEffect.lines;
		spinner = 0;
	}

	private void init(double height, double heightStep, double maxRadius) {
		coordinates = new double[(int) (height / heightStep) + 1][][];
		double radiusIncreasePerHeight = maxRadius / height;
		int i1 = 0, i2 = 0;
		for (double y = 0; y < height; y += heightStep) {
			double radius = radiusIncreasePerHeight * y;
			coordinates[i1] = new double[61][];
			for (double a = 0; a < Math.PI * 2; a += Math.PI / 30) {
				double x, z;
				x = Math.cos(a) * radius;
				z = Math.sin(a) * radius;
				coordinates[i1][i2] = new double[] { x, y, z };
				i2++;
			}
			i2 = 0;
			i1++;
		}
	}

	@Override
	protected void play() {
		spinner++;
		for (int i = 0; i < coordinates.length; i++) {
			// i = current height.
			for (int line = 0; line < lines; line++) {
				int stepPerLine = 60 / lines;
				if (coordinates[i] == null)
					continue;
				double[] coordinateArray = coordinates[i][(((stepPerLine * line) % 60) + (i * 2 % 60)
						+ (59 - ((spinner) % 60))) % 60];
				effectData.setDisplayAt(effectData.getCenter().add(coordinateArray[0], coordinateArray[1], coordinateArray[2]));
				playParticle();
				effectData.getCenter().sub(coordinateArray[0], coordinateArray[1], coordinateArray[2]);
			}
		}
	}

	public TornadoEffect setLines(int lines) {
		this.lines = lines;
		return this;
	}

}