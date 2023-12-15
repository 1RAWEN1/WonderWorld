package com.mygdx.game.objects.characters.withInventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class Chest extends ObsWithInventory {
    public static boolean hasAxe = false;
    public Chest(){
        super(new TextureRegion(GameScreen.loadTexture("characters\\chest.png"), 0, 0, 31, 28),
                new Rectangle3D(new Rectangle(0, 0, 30, 10), 0, 15));

        setBounds(0,0,31, 28);

        setMass(100);

        setRectDelta(5);

        if(hasAxe && Math.random() < 0.2){
            int resType = (int)(1 + (Math.random() * 2 + 0.2));

            Item item = new Item(resType);
            getInventory().add(item, this);
        }
        else if(!hasAxe){
            hasAxe = true;
            Item item = new Item(2);
            getInventory().add(item, this);
        }

        int addItems = (int)(Math.random() * 5 + 0.5);
        for(int i = 0; i < addItems; i++) {
            int itemIndex = 91 + (int)(Math.random() * 2 + 0.5);
            if(Math.random() < 0.2){
                itemIndex = 97;
            }
            Item item = new Item(itemIndex);
            if(getInventory().getItemsSize() + item.getItemSize() <= getInventory().getInventorySize())
                getInventory().add(item, this);
        }

        setMaterials(new int[]{3, 91, 1, 94});

        setStatic(false);
    }

    @Override
    public void act(float delta) {
        if (isLookInv())
            getTexture().setRegion(31, 0, 31, 28);
        else
            getTexture().setRegion(0, 0, 31, 28);

        setLookInv(false);

        updateZ();

        physics();
    }
}
