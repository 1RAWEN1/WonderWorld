package com.mygdx.game.objects.characters.withInventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.scenes.GameScreen;

public class AlchemyTable extends Workbench {
    public AlchemyTable(){
        super(new TextureRegion(GameScreen.loadTexture("characters\\woodshop.png"), 328, 127, 35, 55),
                new Rectangle3D(new Rectangle(0, 0, 32, 32), 0, 30));

        setMass(100);

        setRectDelta(15);

        getInventory().addNeedRes(0);

        setMaterials(new int[]{5, 91, 5, 102});

        setStatic(false);

        //regen
        crafts.add(new int[]{1, 99, 1, 98, 1, 103});
        craftItems.add(104);
    }
}
