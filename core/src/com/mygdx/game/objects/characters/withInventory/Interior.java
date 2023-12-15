package com.mygdx.game.objects.characters.withInventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.scenes.GameScreen;

public class Interior extends Obstacle {
    public Interior(){
        super(new TextureRegion(GameScreen.loadTexture("decor\\inside_b.png"), 200, 303, 49, 82),
                new Rectangle3D(new Rectangle(0, 0, 32, 40), 0, 7));

        setScaleX(32 / 49f);
        setScaleY(64 / 82f);
        setRectDelta(22);

        setSlimeCof(0.7f);

        setMass(200);

        setMaterials(new int[]{10, 91});

        setStatic(false);
    }

    @Override
    public void act(float delta) {
        updateZ();

        physics();
    }
}
