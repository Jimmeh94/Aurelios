package com.aurelios.managers;

import com.aurelios.Aurelios;
import com.aurelios.game.environment.nodes.Node;
import com.aurelios.game.user.ai.NPC;
import com.aurelios.game.user.ai.Relationship;
import com.aurelios.util.database.MongoUtils;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class AIManager extends Manager<NPC>{
    
    private List<Relationship> relationships = new CopyOnWriteArrayList<>();

    private static DayOfWeek currentDayOfWeek = DayOfWeek.MONDAY;

    public static DayOfWeek getCurrentDayOfWeek() {
        return currentDayOfWeek;
    }

    private void addRelationship(NPC one, NPC two){
        if(!doesRelationshipExist(one, two)){
            relationships.add(new Relationship(one.getMetaData().getUuid(), two.getMetaData().getUuid()));
        }
    }

    private boolean doesRelationshipExist(NPC one, NPC two) {
        for(Relationship relationship: relationships){
            if(relationship.is(one, two)){
                return true;
            }
        }
        return false;
    }

    public Optional<Relationship> findRelationship(NPC one, NPC two){
        for(Relationship relationship: relationships){
            if(relationship.is(one, two)){
                return Optional.of(relationship);
            }
        }
        return Optional.empty();
    }

    public void createNPC(Node node){
        objects.add(new NPC(node));
    }

    public UUID getNextAvailableUUID(){
        return UUID.randomUUID();
    }

    public boolean exists(NPC npc){
        for(NPC n: objects){
            if(n.equals(npc)){
                return true;
            }
        }
        return false;
    }

    public boolean isValidFirstName(String first){
        for(NPC n: objects){
            if(n.getMetaData().getFirstName().equalsIgnoreCase(first)){
                return false;
            }
        }
        return true;
    }

    public boolean isValidLastName(String last){
        for(NPC n: objects){
            if(n.getMetaData().getLastName().equalsIgnoreCase(last)){
                return false;
            }
        }
        return true;
    }

    /**
     * Begins simulation to form/grow relationships between NPCs
     */
    public void simulate(){
        for(NPC npc: objects){
            for(NPC npc2: objects){
                if(npc.getMetaData().getUuid() == npc2.getMetaData().getUuid() || npc.getMetaData().getUuid().equals(npc2.getMetaData().getUuid())){
                    continue;
                } else {
                    if(npc.getMetaData().isCompatibleWith(npc2)){
                        addRelationship(npc, npc2);
                    }
                }
            }
        }

        for(Relationship relationship: relationships){
            relationship.simulate();
        }
    }

    public List<Relationship> getRelationships(NPC npc) {
        List<Relationship> give = new ArrayList<>();

        for(Relationship r: relationships){
            if(r.has(npc)){
                give.add(r);
            }
        }
        return give;
    }

    public Optional<NPC> findNPC(UUID npcOne) {
        for(NPC npc: objects){
            if(npc.getMetaData().getUuid().compareTo(npcOne) == 0){
                return Optional.of(npc);
            }
        }
        return Optional.empty();
    }

    public void printRelationships() {
        for(Relationship relationship: relationships){
            relationship.print();
        }
    }

    public void tick() {
        for(NPC npc: objects){
            npc.tick();
        }
    }

    public void load() {
        Aurelios.INSTANCE.getMongoUtils().loadData(MongoUtils.COLLECTION_NPCS);
    }

    public void regenerateDailySchedules() {
        for(NPC npc: objects){
            npc.getDailySchedule().generateSchedule();
        }
    }

    public void despawn() {
        for(NPC npc: getObjects()){
            if(npc.getUser() != null){
                if(npc.getUser().getEntity() != null){
                    npc.getUser().getEntity().remove();
                }
            }
        }
    }
}
