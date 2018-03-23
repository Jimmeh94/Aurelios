package com.aurelios.common.managers;

import com.aurelios.Aurelios;
import com.aurelios.common.ServerProxy;
import com.aurelios.common.game.environment.nodes.Node;
import com.aurelios.common.game.environment.nodes.WildernessNode;
import com.aurelios.common.util.database.MongoUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.Optional;
import java.util.UUID;

public class NodeManager extends Manager<Node> {

    private final WildernessNode wilderness = new WildernessNode(Text.of("Wilderness"));

    public NodeManager(){
        //instantiate all nodes here
    }

    public void load(){
        Aurelios.INSTANCE.getMongoUtils().loadData(MongoUtils.COLLECTION_NODES);
    }

    public Optional<Node> findNode(Location location){
        Optional<Node> give = Optional.empty();
        for(Node node: objects){
            if(node.isWithin(location)){
                give = Optional.of(node);
                break;
            }
        }

        if(!give.isPresent()){
            give = Optional.of(wilderness);
        }
        return give;
    }

    public void tick() {
        for(Node node: objects){
            node.tick();
        }
    }

    public Optional<Node> findNode(UUID uuid) {
        for(Node node: objects){
            if(node.getUuid().compareTo(uuid) == 0){
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }
}
