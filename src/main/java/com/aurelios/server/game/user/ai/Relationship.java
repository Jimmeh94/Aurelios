package com.aurelios.server.game.user.ai;

import com.aurelios.server.managers.Managers;

import java.util.Optional;
import java.util.UUID;

public class Relationship {

    private UUID npcOne, npcTwo;
    private int trust; //from 0-100 (0 being strangers, 100 being best friends)

    public Relationship(UUID npcOne, UUID npcTwo) {
        this.npcOne = npcOne;
        this.npcTwo = npcTwo;
        trust = 1;
    }

    public void incrementTrust(){
        trust++;
        checkTrust();
    }

    private void checkTrust() {
        if(trust > 100){
            trust = 100;
        }

        if(trust < 0){
            trust = 0;
        }
    }

    public void incrementTrust(int a){
        trust += a;
        checkTrust();
    }

    public void decrementTrust(){
        trust--;
        checkTrust();
    }

    public void decrementTrust(int a){
        trust -= a;
        checkTrust();
    }

    public UUID getNpcOne() {
        return npcOne;
    }

    public UUID getNpcTwo() {
        return npcTwo;
    }

    public int getTrust() {
        return trust;
    }

    public boolean is(NPC one, NPC two) {
        if(one.getMetaData().getUuid().compareTo(npcOne) == 0 || one.getMetaData().getUuid().compareTo(npcTwo) == 0){
            if(two.getMetaData().getUuid().compareTo(npcOne) == 0 || two.getMetaData().getUuid().compareTo(npcTwo) == 0){
                return true;
            }
        }
        return false;
    }

    public boolean has(NPC npc) {
        return npc.getMetaData().getUuid().compareTo(npcOne) == 0 || npc.getMetaData().getUuid().compareTo(npcTwo) == 0;
    }

    public void print() {
        Optional<NPC> npc = Managers.AI.findNPC(npcOne);
        Optional<NPC> npc2 = Managers.AI.findNPC(npcTwo);

        if(npc.isPresent() && npc2.isPresent()){
            System.out.println("- relationship: " + npc.get().getMetaData().getFullName() + ", " + npc2.get().getMetaData().getFullName() + ": " + trust);
        }
    }

    public void simulate() {
        incrementTrust();
    }
}
