package com.aurelios.server.game.user.ai.traits.personality;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

public abstract class Personality {

    private final Personalities type;
    protected List<Personality> compatiblePersonalities;
    /**
     * When the simulation is ran and personalities are interacting, this determines how much the trust in that
     * relationship increases/decreases
     */
    private int lowestTrustOutcome, highestTrustOutcome;

    protected abstract void generateCompatiblePersonalities();

    public static Personality getPersonalityFromType(Personalities type){
        try {
            return (Personality) type.getClazz().getConstructors()[0].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Personality getRandomPersonality(){
        Random random = new Random();
        Personalities temp = Personalities.values()[random.nextInt(Personalities.values().length)];

        try {
            return (Personality) temp.getClazz().getConstructors()[0].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Personality(Personalities type, int lowTrust, int highTrust){
        this.type = type;
        this.lowestTrustOutcome = lowTrust;
        this.highestTrustOutcome = highTrust;
        generateCompatiblePersonalities();
    }

    /**
     * When the simulation is ran, this returns the amount of trust to increase/decrease the relationship by
     * @return
     */
    public int getTrustIncrement(){
        int range = highestTrustOutcome - lowestTrustOutcome;
        Random random = new Random();
        return random.nextInt(range) + 1 - lowestTrustOutcome;
    }

    public Personalities getType() {
        return type;
    }

    public List<Personality> getCompatiblePersonalities() {
        return compatiblePersonalities;
    }

    public String getStringName(){
        return type.toString();
    }
}
