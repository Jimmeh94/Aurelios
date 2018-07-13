package com.aurelios.server.game.environment.nodes;

import com.aurelios.Aurelios;
import com.aurelios.server.game.environment.nodes.shapes.Shape;
import com.aurelios.server.game.user.ai.NPC;
import com.aurelios.server.game.user.ai.traits.Role;
import com.aurelios.server.managers.Managers;
import com.google.common.collect.Lists;
import com.mcsimonflash.sponge.teslalibs.inventory.Element;
import com.mcsimonflash.sponge.teslalibs.inventory.Layout;
import com.mcsimonflash.sponge.teslalibs.inventory.View;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A city/area in the world
 */

public class Node extends Area{

    protected List<PointOfInterest> children = new CopyOnWriteArrayList<>();

    //This is the amount of AI that will ideally be alive in this node at one time
    private int aiCap;

    private View npcMenu;
    private View poiMenu;

    //protected abstract void loadChildren();

    public Node(UUID uuid, Text displayName, Shape shape, int aiCap, PointOfInterest... children) {
        super(displayName, shape, uuid);
        this.aiCap = aiCap;

        if(children != null){
            Collections.addAll(this.children, children);
        }

        //this.children.add(new PointOfInterest(Text.of("POI One"), new ShapeCircle(new Vector3d(0, 0, 0), 15), this));

      //  loadChildren();


        npcMenu = View.builder().archetype(InventoryArchetypes.CHEST).property(InventoryTitle.of(Text.builder().append(displayName)
                .append(Text.of(" - NPCs")).build()))
                .build(Aurelios.INSTANCE.PLUGIN_CONTAINER);

        poiMenu = View.builder().archetype(InventoryArchetypes.CHEST).property(InventoryTitle.of(Text.builder().append(displayName)
                .append(Text.of(" - POIs")).build()))
                .build(Aurelios.INSTANCE.PLUGIN_CONTAINER);
    }

    public Optional<Area> getContainingArea(Entity p){
        return getContainingArea(p.getLocation());
    }

    public boolean isWithin(Entity player) {
        return isWithin(player.getLocation());
    }

    public boolean isWithin(Location location){
        if (shape.isWithin(location)) {
            return true;
        } else {
            for (PointOfInterest area : children) {
                if (area.getShape().isWithin(location)) {
                    return true;
                }
            }
            return false;
        }
    }

    public Optional<Area> getContainingArea(Location location) {
        Optional<Area> give = Optional.empty();

        if(shape.isWithin(location)){
            give = Optional.of(this);

            /**
             * This will narrow down the area to the "smallest" child
             */
            for(PointOfInterest area: children){
                if(area.getShape().isWithin(location)){
                    return Optional.of(area);
                }
            }
        }

        return give;
    }

    public List<PointOfInterest> getChildren() {
        return children;
    }

    public void addChild(PointOfInterest poi) {
        children.add(poi);
    }

    public void tick(){
        int alive = 0;
        for(NPC npc: Managers.AI.getObjects()){
            if(npc.getMetaData().getHome() == this){
                alive++;
            }
        }
        if(alive < aiCap){
            do {
                Managers.AI.createNPC(this);
                alive++;
            } while(alive < aiCap);
        }

        updateNpcMenu();
    }

    public int getAiCap() {
        return aiCap;
    }

    public List<PointOfInterest> getPOIsWithRole(Role role) {
        List<PointOfInterest> possible = new ArrayList<>();
        for(PointOfInterest child: children){
            if(child.hasRole(role)){
                possible.add(child);
            }
        }
        return possible;
    }

    public void updateNpcMenu(){
        List<NPC> npcs = Managers.AI.getNPCsInNode(this);

        Layout layout;
        Map<Integer, Element> elements = new HashMap<>();
        for(int i = 0; i < Math.min(npcs.size(), 53); i++){
            ItemStack item = ItemStack.builder().itemType(ItemTypes.SKULL).build();
            item.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, npcs.get(i).getMetaData().getFullName()));
            item.offer(Keys.ITEM_LORE, npcs.get(i).getMetaData().getMetaDataAsList());
            elements.put(i, Element.of(item));
        }
        layout = Layout.builder().setAll(elements).build();
        //npcMenu.define(layout);

        npcMenu.update(layout);
    }

    public void updatePOIMenu(){
        Layout layout;
        Map<Integer, Element> elements = new HashMap<>();
        for(int i = 0; i < Math.min(children.size(), 53); i++){
            ItemStack item = ItemStack.builder().itemType(ItemTypes.GRASS).build();
            item.offer(Keys.DISPLAY_NAME, children.get(i).getDisplayName());
            item.offer(Keys.ITEM_LORE, Arrays.asList(Text.of(children.get(i).getAvailableRolesAsString())));
            elements.put(i, Element.of(item));
        }
        layout = Layout.builder().setAll(elements).build();

        poiMenu.update(layout);
    }

    public void displayPOIMenu(Player player){
        poiMenu.open(player);
    }

    public void displayNPCMenu(Player player){
        npcMenu.open(player);
    }
}
