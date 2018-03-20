package com.aurelios.game.user.stats;

import java.util.Optional;

public class Stat {

    private double amount;
    private double max;

    private Type type;

    private Optional<Recovery> recovery;

    public Stat(Type type, double amount, double max){
        this.type = type;

        this.amount = amount;
        this.max = max;
    }

    public void addRecovery(Recovery recovery){
        this.recovery = Optional.of(recovery);
    }

    public void recover(){
        if(recovery.isPresent()){
            recovery.get().recover();
        }
    }

    public double getAmount() {
        return amount;
    }

    public double getMax() {
        return max;
    }

    public void addToMax(double amount){
        max += amount;
    }

    public void addToAmount(double amount){
        if(this.amount < max){
            this.amount += amount;
            if(this.amount > max){
                this.amount = max;
            }
        }
    }

    public Type getType() {
        return type;
    }

    public void subtract(double required) {
        amount -= required;
    }

    public boolean canAfford(double amount) {
        return this.amount >= amount;
    }

    /**
     * --------------------------------------
     */

    public static class Recovery {

        private Stat owner;
        private double interval;

        public Recovery(Stat stat, double interval){
            this.owner = stat;
            this.interval = interval;
        }

        public void recover(){
            owner.addToAmount(interval);
        }

    }

    public enum Type {
        HEALTH,
        MANA,
        STAMINA,
        CRIT_MELEE,
        CRIT_RANGE,
        CRIT_MAGIC,
        RECOVERY_HEALTH,
        RECOVERY_MANA,
        RECOVERY_STAMINA;

        @Override
        public String toString(){
            String input = super.toString().toLowerCase();
            input.replace('_', ' ');
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }
    }

}
