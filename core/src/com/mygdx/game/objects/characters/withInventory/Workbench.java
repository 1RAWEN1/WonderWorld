package com.mygdx.game.objects.characters.withInventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

import java.util.ArrayList;

public class Workbench extends ObsWithInventory {
    public final ArrayList<int[]> crafts = new ArrayList<>();
    public final ArrayList<Integer> size = new ArrayList<>();
    public final ArrayList<Integer> craftItems = new ArrayList<>();
    public Workbench(){
        super(new TextureRegion(GameScreen.loadTexture("characters\\woodshop.png"), 479, 256, 32, 32),
                new Rectangle3D(new Rectangle(0, 0, 32, 20), 0, 10));

        setMass(70);

        setRectDelta(10);

        getInventory().addNeedRes(0);

        //knife
        crafts.add(new int[]{1, 91, 1, 94});
        craftItems.add(1);
        //axe
        crafts.add(new int[]{2, 91, 1, 94});
        craftItems.add(2);
        crafts.add(new int[]{2, 91, 1, 100});
        craftItems.add(2);
        //pickaxe
        crafts.add(new int[]{2, 91, 2, 94});
        craftItems.add(3);
        crafts.add(new int[]{2, 91, 2, 100});
        craftItems.add(3);

        //helm
        crafts.add(new int[]{4, 94});
        craftItems.add(61);
        //chest
        crafts.add(new int[]{6, 94});
        craftItems.add(62);
        //glovers
        crafts.add(new int[]{5, 94});
        craftItems.add(63);
        //pants
        crafts.add(new int[]{2, 94});
        craftItems.add(64);
        //shadow perl
        crafts.add(new int[]{1, 101, 1, 103});
        craftItems.add(105);
        //bow
        crafts.add(new int[]{3, 91});
        craftItems.add(4);
        //arrow
        crafts.add(new int[]{1, 91, 1, 107});
        craftItems.add(106);
        //chitin shard
        crafts.add(new int[]{1, 102});
        craftItems.add(107);
        //shield
        crafts.add(new int[]{1, 91, 3, 94});
        craftItems.add(31);
        //flack
        crafts.add(new int[]{3, 102});
        craftItems.add(97);

        for(int i = 0; i < craftItems.size(); i++){
            size.add(0);
        }

        setMaterials(new int[]{2, 91, 2, 100});

        setStatic(false);
    }

    public Workbench(int type){
        super(new TextureRegion(GameScreen.loadTexture("characters\\woodshop.png"), 479, 256, 32, 32),
                new Rectangle3D(new Rectangle(0, 0, 32, 20), 0, 10));

        getInventory().addNeedRes(0);

        //axe
        crafts.add(new int[]{2, 91, 1, 100});
        craftItems.add(2);
        //pickaxe
        crafts.add(new int[]{2, 91, 2, 100});
        craftItems.add(3);

        for(int i = 0; i < craftItems.size(); i++){
            size.add(0);
        }
    }

    public Workbench(TextureRegion textureRegion, Rectangle3D rect){
        super(textureRegion, rect);
    }

    public ArrayList<Integer> getSize() {
        return size;
    }

    public static Workbench craftTable = null;
    @Override
    public void act(float delta) {
        if (isLookInv()) {
            for (int j = 0; j < crafts.size(); j++) {
                int[] recipe = crafts.get(j);
                boolean canCraft = true;
                size.set(j, 0);
                for (int i = 1; i < recipe.length; i += 2) {
                    int n = 0;
                    for (Item item : GameScreen.player.getInventory().getAllItems()) {
                        if (item.getIndex() == recipe[i]) {
                            size.set(j, size.get(j) + item.getItemSize());
                            n++;
                            if (n >= recipe[i - 1]) {
                                break;
                            }
                        }
                    }
                    if (n < recipe[i - 1]) {
                        canCraft = false;
                        break;
                    }
                }

                int n = getInventory().hasItem(craftItems.get(j));
                if (canCraft && n == -1) {
                    Item item = new Item(craftItems.get(j));

                    if(j == 2 || j == 4){
                        item.setMaxEndurance(50);
                        item.setEndurance(50);
                    }

                    getInventory().getAllItems().add(item);
                } else if (!canCraft && n != -1) {
                    getInventory().getAllItems().remove(n);
                }
            }
        }

        setLookInv(false);

        physics();

        updateZ();
    }

    public void checkCrafts(){
        for (int j = 0; j < crafts.size(); j++) {
            int[] recipe = crafts.get(j);
            boolean canCraft = true;
            size.set(j, 0);
            for (int i = 1; i < recipe.length; i += 2) {
                int n = 0;
                for (Item item : GameScreen.player.getInventory().getAllItems()) {
                    if (item.getIndex() == recipe[i]) {
                        size.set(j, size.get(j) + item.getItemSize());
                        n++;
                        if (n >= recipe[i - 1]) {
                            break;
                        }
                    }
                }
                if (n < recipe[i - 1]) {
                    canCraft = false;
                    break;
                }
            }

            int n = getInventory().hasItem(craftItems.get(j));
            if (canCraft && n == -1) {
                Item item = new Item(craftItems.get(j));

                item.setMaxEndurance(50);
                item.setEndurance(50);

                getInventory().getAllItems().add(item);
            } else if (!canCraft && n != -1) {
                getInventory().getAllItems().remove(n);
            }
        }
    }

    public void wasteResources(int itemIndex){
        int index = 0;
        for(int k = 0; k < craftItems.size(); k++)
            if(itemIndex == craftItems.get(k)) {
                index = k;
                break;
            }

        for(int i = 1; i < crafts.get(index).length; i+=2){
            int n = 0;
            for (int j = 0; j < GameScreen.player.getInventory().getAllItems().size(); j++) {
                if (GameScreen.player.getInventory().getAllItems().get(j).getIndex() == crafts.get(index)[i]) {
                    GameScreen.player.getInventory().getAllItems().remove(j);
                    j--;
                    n++;
                    if (n >= crafts.get(index)[i - 1]) {
                        break;
                    }
                }
            }
        }
    }

    public int getItemDelta(int itemIndex){
        int index = 0;
        for(int k = 0; k < craftItems.size(); k++)
            if(itemIndex == craftItems.get(k)) {
                index = k;
                break;
            }
        return size.get(index);
    }
}
