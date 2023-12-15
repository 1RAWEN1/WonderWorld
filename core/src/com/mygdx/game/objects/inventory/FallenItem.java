package com.mygdx.game.objects.inventory;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.StaticObject;

public class FallenItem extends MyActor implements StaticObject {
    Item item;
    public FallenItem(Item item) {
        super(item.getItemTexture(),
                new Rectangle3D(new Rectangle(0, 0, item.getItemTexture().getRegionWidth(), 10), 0, item.getItemTexture().getRegionHeight()));

        this.item = item;
        setColor(item.getColor());

        if(item.getScale() < 1) {
            setScaleX(item.getScale());
            setScaleY(item.getScale());
        }
    }

    public void take(){
        remove();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
