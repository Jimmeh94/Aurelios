package com.aurelios.server.managers;

import com.aurelios.Aurelios;
import com.aurelios.server.game.environment.nodes.Node;
import com.aurelios.server.game.environment.nodes.WildernessNode;
import com.aurelios.server.util.database.MongoUtils;
import com.mcsimonflash.sponge.teslalibs.inventory.Element;
import com.mcsimonflash.sponge.teslalibs.inventory.Layout;
import com.mcsimonflash.sponge.teslalibs.inventory.View;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class NodeManager extends Manager<Node> {

    private View nodeMenu;

    private final WildernessNode wilderness = new WildernessNode(Text.of("Wilderness"));

    public NodeManager(){
        //instantiate all nodes here
        nodeMenu = View.builder().archetype(InventoryArchetypes.CHEST).property(InventoryTitle.of(Text.of("Node Creation Menu")))
                .build(Aurelios.INSTANCE.PLUGIN_CONTAINER);

        Layout layout;
        Map<Integer, Element> elements = new HashMap<>();

        //Shape, circle
        ItemStack item = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).quantity(8).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "Shape: Circle, Radius: 8"));
        elements.put(0, Element.of(item));

        ItemStack item2 = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).quantity(15).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "Shape: Circle, Radius: 15"));
        elements.put(1, Element.of(item2));

        ItemStack item3 = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).quantity(30).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "Shape: Circle, Radius: 30"));
        elements.put(2, Element.of(item3));

        //Shape, rectangle, set corner 1, set corner 2
        ItemStack item4 = ItemStack.builder().itemType(ItemTypes.STONE).quantity(1).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "Shape: Rectangle, Corner 1"));
        elements.put(9, Element.of(item4));

        ItemStack item5 = ItemStack.builder().itemType(ItemTypes.STONE).quantity(1).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "Shape: Rectangle, Corner 2"));
        elements.put(10, Element.of(item5));

        //Close
        ItemStack item6 = ItemStack.builder().itemType(ItemTypes.BARRIER).quantity(1).build();
        item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "Close, then use command to name and aiCap"));
        elements.put(18, Element.of(item5));



        layout = Layout.builder().setAll(elements).build();
        nodeMenu.update(layout);
    }

    public void displayNodeMenu(Player player){
        nodeMenu.open(player);
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
