package com.aurelios.common.util.particles.effects;

import com.aurelios.common.util.misc.LocationUtils;
import com.aurelios.common.util.particles.ParticleData;
import com.flowpowered.math.vector.Vector3d;

import java.util.List;

public class LineEffect extends AbstractEffect {
	
	private Vector3d target;
	private List<Vector3d> draw;
	private int display = 0;
	
	/**
	 * Displays a line of particles between 2 specified locations.
	 * @param target The end location of the line.
	 */
	public LineEffect(ParticleData effectData, Vector3d target) {
		super(effectData);
		this.target = target;
		draw = LocationUtils.getConnectingLine(effectData.getCenter(), target, 1.0);
	}
	
	@Override
	protected void play() {
		/*for(int i = 0; i <= display; i++){
			effectData.setDisplayAt(draw.get(i));
			playParticle();
		}
		if(display < draw.size() - 1)
			display++;*/
		Vector3d v = target.sub(effectData.getCenter());
		for (double i = 0; i < v.length(); i += 0.5) {
			effectData.setDisplayAt(effectData.getCenter().clone().add(v.clone().normalize().mul(i)));
			playParticle();
		}
	}

}