package com.aurelios.common.util.particles;

import com.aurelios.common.util.particles.effects.AbstractEffect;
import com.aurelios.common.util.particles.effects.SphereEffect;

public class PreloadedParticle {

        /** Atom Effects **/
        /*ATOM_2RR_1CR_0Y(new AtomEffect(2, 1, 0)),
        ATOM_3RR_1CR_0Y(new AtomEffect(3, 1, 0)),
        ATOM_4RR_1CR_0Y(new AtomEffect(4, 1, 0)),
        ATOM_5RR_1CR_0Y(new AtomEffect(5, 1, 0)),
        ATOM_3RR_2CR_0Y(new AtomEffect(3, 2, 0)),
        ATOM_4RR_3CR_0Y(new AtomEffect(4, 3 ,0)),
        ATOM_5RR_4CR_0Y(new AtomEffect(5, 4, 0)),
        ATOM_6RR_5CR_0Y(new AtomEffect(6, 4, 0)),*/

        /** Helix Effects **/
        /*HELIX_3T_25HS_75R_15L(new HelixEffect(3, .25, 0.75, 15)),
        HELIX_35T_25HS_75R_15L(new HelixEffect(3.5, .25, 0.75, 15)),
        HELIX_4T_25HS_75R_15L(new HelixEffect(4, .25, 0.75, 15)),
        HELIX_45T_25HS_75R_15L(new HelixEffect(4.5, .25, 0.75, 15)),
        HELIX_5T_25HS_75R_15L(new HelixEffect(5, .25, 0.75, 15)),
        HELIX_55T_25HS_75R_15L(new HelixEffect(5.5, .25, 0.75, 15)),
        HELIX_6T_25HS_75R_15L(new HelixEffect(6, .25, 0.75, 15)),
        HELIX_65T_25HS_75R_15L(new HelixEffect(6.5, .25, 0.75, 15)),
        HELIX_7T_25HS_75R_15L(new HelixEffect(7, .25, 0.75, 15)),
        HELIX_75T_25HS_75R_15L(new HelixEffect(7.5, .25, 0.75, 15)),
        HELIX_8T_25HS_75R_15L(new HelixEffect(8, .25, 0.75, 15)),
        HELIX_85T_25HS_75R_15L(new HelixEffect(8.5, .25, 0.75, 15)),
        HELIX_9T_25HS_75R_15L(new HelixEffect(9, .25, 0.75, 15)),
        HELIX_95T_25HS_75R_15L(new HelixEffect(9.5, .25, 0.75, 15)),
        HELIX_10T_25HS_75R_15L(new HelixEffect(10, .25, 0.75, 15)),*/

        /** Horizontal Helix Effects **/
        //public static final HelixHorizontalEffect HELIX_HOR_10T_25HS_75R_15L = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.DOWN);

        /*public static final HelixHorizontalEffect HELIX_NORTH_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.NORTH);
        public static final HelixHorizontalEffect HELIX_NORTH_NORTHEAST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.NORTH_NORTHEAST);
        public static final HelixHorizontalEffect HELIX_NORTHEAST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.NORTHEAST);
        public static final HelixHorizontalEffect HELIX_EAST_NORTHEAST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.EAST_NORTHEAST);
        public static final HelixHorizontalEffect HELIX_EAST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.EAST);
        public static final HelixHorizontalEffect HELIX_EAST_SOUTHEAST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.EAST_SOUTHEAST);
        public static final HelixHorizontalEffect HELIX_SOUTHEAST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.SOUTHEAST);
        public static final HelixHorizontalEffect HELIX_SOUTH_SOUTHEAST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.SOUTH_SOUTHEAST);
        public static final HelixHorizontalEffect HELIX_SOUTH_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.SOUTH);
        public static final HelixHorizontalEffect HELIX_SOUTH_SOUTHWEST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.SOUTH_SOUTHWEST);
        public static final HelixHorizontalEffect HELIX_SOUTHWEST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.SOUTHWEST);
        public static final HelixHorizontalEffect HELIX_WEST_SOUTHWEST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.WEST_SOUTHWEST);
        public static final HelixHorizontalEffect HELIX_WEST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.WEST);
        public static final HelixHorizontalEffect HELIX_WEST_NORTHWEST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.WEST_NORTHWEST);
        public static final HelixHorizontalEffect HELIX_NORTHWEST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.NORTHWEST);
        public static final HelixHorizontalEffect HELIX_NORTH_NORTHWEST_075_15 = new HelixHorizontalEffect(50, 0.25, 0.75, 15, Direction.NORTH_NORTHWEST);

        public static final HelixHorizontalEffect HELIX_NORTHEAST_075_2 = new HelixHorizontalEffect(50, 0.25, 1.5, 2, Direction.NORTHEAST);*/

        /** Sphere Effects **/
        public static final SphereEffect SPHERE_025R = new SphereEffect(0.25);
        public static final SphereEffect SPHERE_05R = new SphereEffect(0.5);
        public static final SphereEffect SPHERE_1R = new SphereEffect(1);
        /*SPHERE_15R(new SphereEffect(1.5)),
        SPHERE_2R(new SphereEffect(2)),
        SPHERE_25R(new SphereEffect(2.5)),
        SPHERE_3R(new SphereEffect(3)),
        SPHERE_35R(new SphereEffect(3.5)),
        SPHERE_4R(new SphereEffect(4)),
        SPHERE_45R(new SphereEffect(4.5)),
        SPHERE_5R(new SphereEffect(5)),
        SPHERE_55R(new SphereEffect(5.5)),
        SPHERE_6R(new SphereEffect(6)),
        SPHERE_65R(new SphereEffect(6.5)),
        SPHERE_7R(new SphereEffect(7)),
        SPHERE_75R(new SphereEffect(7.5)),
        SPHERE_8R(new SphereEffect(8)),
        SPHERE_855R(new SphereEffect(8.5)),
        SPHERE_9R(new SphereEffect(9)),
        SPHERE_95R(new SphereEffect(9.5)),
        SPHERE_10R(new SphereEffect(10));*/

        /** Tornado Effects **/
        //height, height step, max radius, lines
        /*TORNADO_3H_3R(new TornadoEffect(3, 0.5, 3, 50)),
        TORNADO_35H_3R(new TornadoEffect(3.5, 0.5, 3, 50)),
        TORNADO_4H_3R(new TornadoEffect(4, 0.5, 3, 50)),
        TORNADO_45H_3R(new TornadoEffect(4.5, 0.5, 3, 50)),
        TORNADO_5H_3R(new TornadoEffect(5, 0.5, 3, 50)),
        TORNADO_55H_3R(new TornadoEffect(5.5, 0.5, 3, 50)),
        TORNADO_6H_3R(new TornadoEffect(6, 0.5, 3, 50)),
        TORNADO_65H_3R(new TornadoEffect(6.5, 0.5, 3, 50)),
        TORNADO_7H_3R(new TornadoEffect(7, 0.5, 3, 50)),
        TORNADO_75H_3R(new TornadoEffect(7.5, 0.5, 3, 50)),
        TORNADO_8H_3R(new TornadoEffect(8, 0.5, 3, 50)),
        TORNADO_85H_3R(new TornadoEffect(8.5, 0.5, 3, 50)),
        TORNADO_9H_3R(new TornadoEffect(9, 0.5, 3, 50)),
        TORNADO_95H_3R(new TornadoEffect(9.5, 0.5, 3, 50)),
        TORNADO_10H_3R(new TornadoEffect(10, 0.5, 3, 50));*/

        private AbstractEffect abstractEffect;

        PreloadedParticle(AbstractEffect effect){this.abstractEffect = effect;}

        public AbstractEffect getEffect(){return abstractEffect;}


}
