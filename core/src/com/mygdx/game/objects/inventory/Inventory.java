package com.mygdx.game.objects.inventory;

import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.characters.Creature;

import java.util.ArrayList;

public class Inventory {
    private final ArrayList<Integer> needRes = new ArrayList<>();
    private Item[] mainSlots = new Item[6];
    private ArrayList<Item> items = new ArrayList<>();

    private int inventorySize = 100;

    public void add(Item item, MyActor cr){
        if(needRes.size() == 0 || isNeedRes(item)) {
            if (item != null && getItemsSize() + item.getItemSize() <= inventorySize) {
                items.add(item);
            } else if(item != null){
                FallenItem fallenItem = new FallenItem(item);
                fallenItem.setX(cr.getRect().getX() + (float) (cr.getRect().getWidth() * Math.random()));
                fallenItem.setY(cr.getRect().getY() + (float) (cr.getRect().getHeight() * Math.random()));
                cr.getStage().addActor(fallenItem);

                fallenItem.sort();
            }
        }
    }

    public boolean isNeedRes(Item item){
        if(item != null)
            for (Integer needRes1 : needRes)
                if (item.getIndex() == needRes1)
                    return true;

        return false;
    }

    public void dropItem(int i, Creature cr){
        if(i < getAllItems().size()) {
            Item item = getAllItems().get(i);
            FallenItem fallenItem = new FallenItem(item);
            fallenItem.setX(cr.getRect().getX() + (float) (cr.getRect().getWidth() * Math.random()));
            fallenItem.setY(cr.getRect().getY() + (float) (cr.getRect().getHeight() * Math.random()));
            cr.getStage().addActor(fallenItem);

            fallenItem.sort();

            getAllItems().remove(i);
        }
    }

    public int hasItem(int index){
        for(int i = 0; i < getAllItems().size(); i++){
            if(getAllItems().get(i).getIndex() == index)
                return i;
        }
        return -1;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(int inventorySize) {
        this.inventorySize = inventorySize;
    }

    public int getItemsSize(){
        int sum = 0;
        for (Item item : items) if(item != null ) sum += item.getItemSize();
        return sum;
    }

    public void setMainSlots(Item[] mainSlots) {
        this.mainSlots = mainSlots;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public int getIndexInMainHand(){
        return mainSlots[0] != null ? mainSlots[0].getIndex() : 0;
    }

    public int getIndexInSecHand(){
        return mainSlots[5] != null ? mainSlots[5].getIndex() : 0;
    }

    public int getTypeOfResICanGet(){
        return mainSlots[0] != null ? mainSlots[0].getCanGet() : 0;
    }

    public Item[] getItems(){
        return mainSlots;
    }

    public ArrayList<Item> getAllItems(){
        return items;
    }

    public Item getItem(int index){
        if(index < 0){
            return getItems()[-index - 1];
        }
        else if(index < getAllItems().size()){
            return getAllItems().get(index);
        }
        else{
            return null;
        }
    }

    public void addNeedRes(int res){
        needRes.add(res);
    }

    public void clearNeedRes(){
        needRes.clear();
    }

    public boolean setItem(int index, Item item, int sizeDelta){
        if(item == null || needRes.size() == 0 || isNeedRes(item)) {
            if (item != null && item.getItemSize() + getItemsSize() - sizeDelta <= getInventorySize() ||
            item != null && index < 0) {
                if (index == -6 && item.isCanBeInSecHand() ||
                        index > -6 && index < -1 && item.isArmor() && item.getArmorSlot() == -(index + 1) ||
                        index == -1 && item.isCanBeInMainHand() ||
                        index >= 0) {
                    if (index < 0) {
                        getItems()[-index - 1] = item;
                        return true;
                    } else if (index < getAllItems().size()) {
                        getAllItems().set(index, item);
                        return true;
                    } else {
                        getAllItems().add(item);
                        return true;
                    }
                } else {
                    System.out.println(1);
                    return false;
                }
            } else if (item == null) {
                if (index < 0) {
                    getItems()[-index - 1] = null;
                    return true;
                } else if (index < getAllItems().size()) {
                    getAllItems().remove(index);
                    return true;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
        else{
            return false;
        }
    }
}
