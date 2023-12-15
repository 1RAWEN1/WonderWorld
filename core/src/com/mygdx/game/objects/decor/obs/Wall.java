package com.mygdx.game.objects.decor.obs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.scenes.GameScreen;

public class Wall extends Obstacle {
    public Wall(int xSize, int ySize, int zSize) {
        super(new Rectangle3D(new Rectangle(0, 0, xSize, ySize), 0, zSize));

        if(getRect().getHeight() > 5 && zSize == 60) {
            setRectDelta(ySize / 2);
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\Inside_A4.png"), 0, ySize > 5 ? 96 - ySize : 88, xSize, ySize + zSize));

            setMaterials(new int[]{20, 100});
        }
        else if(xSize == 96 && ySize == 5) {
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\backWall.png"), 0, 8, 96, 62));

            setMaterials(new int[]{25, 100, 5, 91});
        }
        else if(xSize == 37 && ySize == 5) {
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\Outside_A3.png"), 14, 63, 37, 63));

            setMaterials(new int[]{5, 100, 5, 91});
        }
        else if(xSize == 32 && ySize == 5) {
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\door.png"), 0, 0, 32, 63));
            setRectDeltaZ(56);
            getRect().setzSize(4);

            getRect().setDoor(true);

            setMaterials(new int[]{10, 91, 5, 100});
        }
        else if(xSize == 96 && zSize == 30){
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\roof.png"), 0, 0, 159, 129));
            setRectDelta(32);
            setScaleX(116f / 159);
            setScaleY(109f / 129);

            setMaterials(new int[]{15, 100});
        }
    }

    public Wall(int xSize, int ySize, int zSize, boolean inverse) {
        super(new Rectangle3D(new Rectangle(0, 0, xSize, ySize), 0, zSize));

        if(xSize == 32 && ySize == 5 && !inverse) {
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\backWall.png"), 32, 8, 32, 62));

            setMaterials(new int[]{5, 100, 5, 91});
        }
        else if(xSize == 32 && ySize == 5) {
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\backWall.png"), 0, 8, 32, 62));

            setMaterials(new int[]{10, 100});
        }
    }
}
