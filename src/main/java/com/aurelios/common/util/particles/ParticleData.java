package com.aurelios.common.util.particles;

import com.aurelios.common.game.ability.Ability;
import com.aurelios.common.game.environment.nodes.Node;
import com.aurelios.common.game.user.UserPlayer;
import com.aurelios.common.managers.Managers;
import com.aurelios.common.util.misc.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleOption;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleData {

    public static Builder builder(){return new Builder();}

    private List<UserPlayer> viewers;
    private int quantity;
    private double xOffset, yOffset, zOffset;
    private boolean randomizeOffsetDirections;
    private ParticleType type;
    private Optional<Map<ParticleOption, Object>> particleOptions;
    private Optional<Vector3d> velocity;
    private Vector3d displayAt, center;
    private Optional<Ability> container;

    protected ParticleData(Builder builder) {
        this.viewers = builder.viewers;
        this.quantity = builder.quantity;
        this.xOffset = builder.xOffset;
        this.yOffset = builder.yOffset;
        this.zOffset = builder.zOffset;
        this.randomizeOffsetDirections = builder.randomizeOffsetDirections;
        this.particleOptions = builder.particleOptions;
        this.type = builder.type;
        this.velocity = builder.velocity;
        this.displayAt = builder.displayAt;
        this.center = builder.center;
        this.container = builder.container;

        if(center == null && displayAt != null){
            center = displayAt.clone();
        } else if(center != null && displayAt == null){
            displayAt = center.clone();
        }
    }

    public void setViewers(Vector3d position, double radius){
        if(position == null)
            return;

        for(Player player: Sponge.getServer().getOnlinePlayers()){
            if(LocationUtils.withinDistance(player.getLocation().getPosition(), position, radius)){
            //if(player.getLocation().getPosition().distance(position) <= radius){
                Optional<UserPlayer> user = Managers.USER.findPlayer(player.getUniqueId());
                if(user.isPresent()){
                    viewers.add(user.get());
                }
            }
        }
        return;
    }

    public void setCenter(Vector3d center) {
        this.center = center;
    }

    public void setDisplayAt(Vector3d displayAt) {
        this.displayAt = displayAt;
    }

    public Vector3d getCenter() {
        return center;
    }

    public ParticleType getType() {
        return type;
    }

    public Vector3d getOffsetVector(){
        if(randomizeOffsetDirections){
            return new Vector3d(xOffset * LocationUtils.getRandomNegOrPos(), yOffset * LocationUtils.getRandomNegOrPos(), zOffset * LocationUtils.getRandomNegOrPos());
        } else {
            return new Vector3d(xOffset, yOffset, zOffset);
        }
    }

    public List<UserPlayer> getViewers() {
        return viewers;
    }

    public int getQuantity() {
        return quantity;
    }

    public Optional<Map<ParticleOption, Object>> getParticleOptions() {
        return particleOptions;
    }

    public Optional<Vector3d> getVelocity() {
        return velocity;
    }

    public Vector3d getDisplayAt() {
        if(displayAt == null){
            displayAt = center.clone();
        }
        return displayAt;
    }

    public Optional<Ability> getAbility() {
        return container;
    }

    public void setDisplayAt(double v, double v1, double v2) {
        displayAt = new Vector3d(v, v1, v2);
    }

    /**
     * Builder ===========================================================
     */
    public static class Builder{
        private List<UserPlayer> viewers = new CopyOnWriteArrayList();
        private int quantity = 3;
        private double xOffset, yOffset, zOffset;
        private boolean randomizeOffsetDirections;
        private Optional<Map<ParticleOption, Object>> particleOptions = Optional.empty();
        private ParticleType type = ParticleTypes.FLAME;
        private Optional<Vector3d> velocity = Optional.empty();
        private Vector3d displayAt, center;
        private Optional<Ability> container = Optional.empty();

        public ParticleData build(){
            return new ParticleData(this);
        }

        public Builder center(Vector3d v){
            this.center = v;
            return this;
        }

        public Builder displayAt(Vector3d v){
            this.displayAt = v;
            return this;
        }

        public Builder velocity(Vector3d v){
            this.velocity = Optional.of(v);
            return this;
        }

        public Builder type(ParticleType type){
            this.type = type;
            return this;
        }

        public Builder addViewers(Node area){
            for(Player player: Sponge.getServer().getOnlinePlayers()){
                if(area.isWithin(player)){
                    Optional<UserPlayer> user = Managers.USER.findPlayer(player.getUniqueId());
                    if(user.isPresent()){
                        viewers.add(user.get());
                    }
                }
            }
            return this;
        }

        public Builder addViewers(Vector3d position, double radius){
            if(position == null)
                return this;
            
            for(Player player: Sponge.getServer().getOnlinePlayers()){
                if(LocationUtils.withinDistance(player.getLocation().getPosition(), position, radius)){
                //if(player.getLocation().getPosition().distance(position) <= radius){
                    Optional<UserPlayer> user = Managers.USER.findPlayer(player.getUniqueId());
                    if(user.isPresent()){
                        viewers.add(user.get());
                    }
                }
            }
            return this;
        }

        public Builder addViewer(UserPlayer user){
            if(!viewers.contains(user)){
                viewers.add(user);
            }
            return this;
        }

        public Builder addParticleOption(ParticleOption option, Object c){
            if(!particleOptions.isPresent()){
                particleOptions = Optional.of(new HashMap<>());
            }
            particleOptions.get().put(option, c);
            return this;
        }

        public Builder randomizeOffsets(boolean a){
            randomizeOffsetDirections = a;
            return this;
        }

        public Builder offsets(double x, double y, double z){
            this.xOffset = x;
            this.yOffset = y;
            this.zOffset = z;
            return this;
        }

        public Builder quantity(int quantity){
            this.quantity = quantity;
            return this;
        }

        /**
         * This will keep this ParticleData's center synced with the ability's center
         * @param container
         * @return
         */
        public Builder ability(Ability container) {
            this.container = Optional.of(container);
            return this;
        }
    }

}
