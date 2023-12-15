package com.mygdx.game.objects.characters.withInventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.objects.inventory.Inventory;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.objects.inventory.WithInventory;

public class ObsWithInventory extends Obstacle implements WithInventory {
    private boolean lookInv;
    Item fuel;
    Item prevTargetItem;
    Item targetItem;
    private final Inventory inventory = new Inventory();

    public ObsWithInventory(String picName, TextureRegion textureRegion, Rectangle3D rect, int[] materials) {
        super(picName, textureRegion, rect, materials);
    }

    public ObsWithInventory(TextureRegion textureRegion, Rectangle3D rect) {
        super(textureRegion, rect);
    }

    public ObsWithInventory(Rectangle3D rect) {
        super(rect);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean isLookInv() {
        return lookInv;
    }

    public void setLookInv(boolean lookInv) {
        this.lookInv = lookInv;
    }
}
