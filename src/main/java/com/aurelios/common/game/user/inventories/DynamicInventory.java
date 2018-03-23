package com.aurelios.common.game.user.inventories;

import com.aurelios.Aurelios;
import com.aurelios.common.game.user.UserPlayer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DynamicInventory {

    /**
     * Linked inventories to make a menu of sorts
     *
     * 54 slots available
     * 45 - 53 is for buttons
     *
     * 45 46 47 48 | 49 | 50 51 52 53
     */

    //TODO use data to add T item to the ItemStack passed into addItem, that way you can easily get
    //TODO the object the itemstack represents

    protected List<Inventory> pages;
    protected Text title;
    protected Map<ItemStack, InventoryCallback> mapping;

    public DynamicInventory(Text title){
        this.title = title;

        pages = new ArrayList<>();
        mapping = new HashMap<>();
        addPage();
    }

    public void handle(ItemStack clicked, UserPlayer user){
        for(ItemStack itemStack: mapping.keySet()){
            if(itemStack.get(Keys.DISPLAY_NAME).isPresent()){
                if(clicked.get(Keys.DISPLAY_NAME).isPresent()){
                    if(itemStack.get(Keys.DISPLAY_NAME).get().toPlain().equalsIgnoreCase(clicked.get(Keys.DISPLAY_NAME).get().toPlain())){
                        mapping.get(itemStack).action(user);
                    }
                }
            }
        }
    }

    public boolean has(Inventory targetInventory) {
        for(Inventory inventory: pages){
            if(inventory.getName().get().equalsIgnoreCase(targetInventory.getName().get())){
                return true;
            }
        }
        return false;
    }

    private void addPage(){
        pages.add(Inventory.builder().of(InventoryArchetypes.DOUBLE_CHEST)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.builder().append(title).append(Text.of(" - Page " + pages.size() + 1)).build()))
                .build(Aurelios.INSTANCE));

        GridInventory inv = pages.get(pages.size() - 1).query(GridInventory.class);

        if(pages.size() > 1){
            ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
            itemStack.offer(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, "<-- Previous Page"));
            inv.set(1, 5, itemStack); //should be slot 46, for previous page button
        }

        ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, "Next Page -->"));
        inv.set(7, 5, itemStack); //should be slot 52, for next page button
    }

    private void removePage(){pages.remove(pages.size() - 1);}

    public void addItem(ItemStack itemStack, InventoryCallback item){
        if(pageFull()){
            addPage();
        }

        /*List<Player> currentlyOpen = new ArrayList<>();

        for(Player player: Sponge.getServer().getOnlinePlayers()){
            if(player.getOpenInventory().isPresent()){
                if(pages.contains(player.getOpenInventory().get())){
                    currentlyOpen.add(player);
                }
            }
        }*/

        GridInventory inv = pages.get(pages.size() - 1).query(GridInventory.class);
        if(inv.totalItems() > 0) {
            int currentRow = pages.size() == 1 ? ((inv.totalItems() - 1) / 9) : ((inv.totalItems() - 2) / 9);
            int index = pages.size() == 1 ? ((inv.totalItems() - 1) - currentRow * 9) : ((inv.totalItems() - 2) - currentRow * 9);
            inv.set(index, currentRow, itemStack);
        } else inv.set(0, 0, itemStack);

        mapping.put(itemStack, item);

        //update(currentlyOpen);
    }

    private boolean pageFull() {
        if(pages.size() == 1){
            return pages.get(pages.size() - 1).query(GridInventory.class).totalItems() - 1 == 44;
        }
        return pages.get(pages.size() - 1).query(GridInventory.class).totalItems() - 2 == 44;
    }

    public void displayTo(UserPlayer user){user.getPlayer().openInventory(pages.get(0));}

    public void nextPage(UserPlayer user, Inventory currentPage){
        int index = pages.indexOf(currentPage);
        if(pages.size() > index + 1){
            user.getPlayer().openInventory(pages.get(index++));
        }
    }

    public void previousPage(UserPlayer user, Inventory currentPage){
        int index = pages.indexOf(currentPage);
        if(index != 0){
            user.getPlayer().openInventory(pages.get(index--));
        }
    }

    public void remove(ItemStack t) {
        if(mapping.containsKey(t)){
            for(Inventory inventory: pages){
                //update inventory
                /*if(inventory.poll()){
                    GridInventory g = inventory.query(GridInventory.class);
                    g.
                }*/
            }
            mapping.remove(t);
        }
    }
}
