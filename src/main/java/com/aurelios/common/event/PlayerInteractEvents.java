package com.aurelios.common.event;

import com.aurelios.common.game.environment.nodes.Node;
import com.aurelios.common.game.environment.nodes.PointOfInterest;
import com.aurelios.common.game.environment.nodes.shapes.ShapeCircle;
import com.aurelios.common.game.user.UserPlayer;
import com.aurelios.common.game.user.modules.tools.ModuleAreaCreator;
import com.aurelios.common.game.user.modules.tools.ModulePOICreator;
import com.aurelios.common.game.user.modules.UserModuleTypes;
import com.aurelios.common.managers.Managers;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

public class PlayerInteractEvents {

    @Listener
    public void onInteract(InteractBlockEvent.Primary event, @Root Player player){
        event.setCancelled(true);
        UserPlayer userPlayer = (UserPlayer) Managers.USER.find(player.getUniqueId()).get();
        if(userPlayer.hasModule(UserModuleTypes.NODE_CREATOR)){
            ModuleAreaCreator areaCreator = (ModuleAreaCreator) userPlayer.getModule(UserModuleTypes.NODE_CREATOR).get();
            ModulePOICreator poiCreator = (ModulePOICreator) userPlayer.getModule(UserModuleTypes.POI_CREATOR).get();

            if(userPlayer.getModule(UserModuleTypes.NODE_CREATOR).get().isEnabled()) {
                Managers.NODES.add(new Node(UUID.randomUUID(), Text.of(areaCreator.getName()), new ShapeCircle(
                        event.getTargetBlock().getLocation().get().getPosition(), areaCreator.getRadius(), true), areaCreator.getAiCap()));
                areaCreator.disable();
            } else if(userPlayer.hasModule(UserModuleTypes.POI_CREATOR) && userPlayer.getModule(UserModuleTypes.POI_CREATOR).get().isEnabled()){
                Optional<Node> node = Managers.NODES.findNode(event.getTargetBlock().getLocation().get());
                if(node.isPresent()){
                    node.get().addChild(new PointOfInterest(UUID.randomUUID(), Text.of(poiCreator.getName()),
                            new ShapeCircle(event.getTargetBlock().getLocation().get().getPosition(),
                                    ((ModulePOICreator)userPlayer.getModule(UserModuleTypes.POI_CREATOR).get()).getRadius(), false),
                            node.get(), poiCreator.getRoles() == null ? null : poiCreator.getRoles()));
                    poiCreator.disable();
                }
            }
        }


    }

    @Listener
    public void onBreak(ChangeBlockEvent.Break event){
        //event.setCancelled(true);
    }

}
