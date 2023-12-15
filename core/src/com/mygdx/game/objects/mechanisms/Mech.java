package com.mygdx.game.objects.mechanisms;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.scenes.GameScreen;

public class Mech extends Obstacle {
    public int power;

    public Mech(TextureRegion textureRegion, Rectangle3D rect, int[] materials) {
        super(textureRegion, rect);

        setRectDelta(textureRegion.getRegionHeight() / 2);

        GameScreen.mechs.add(this);
        for (Mech mech : GameScreen.mechs){
            if(mech instanceof Node && mech != this){
                ((Node) mech).check();
            }
        }

        setMaterials(materials);
    }
}
