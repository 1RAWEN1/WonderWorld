package com.mygdx.game.objects.decor.nature;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.scenes.GameScreen;

public class Algae extends Obstacle {
    public Algae(){
        super(new Rectangle3D(new Rectangle(0, 0, 1, 1), 0, 1));
        getRect().setMaterial(false);
        setTexture(new TextureRegion(GameScreen.loadTexture("decor\\vod.png")));

        setMaterials(new int[]{1, 91});

        setColor(new Color(0, 0.5f, 0.5f, 1));
    }
}
