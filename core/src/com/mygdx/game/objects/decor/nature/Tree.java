package com.mygdx.game.objects.decor.nature;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.scenes.GameScreen;

public class Tree extends Obstacle {
    private int treeType = 1;
    private final int type;
    public Tree(float temperature, Cell myCell){
        super(new Rectangle3D(new Rectangle(0, 0, 20, 10), 0, 60));
        TextureRegion texture;
        this.myCell = myCell;
        String picName;
        if(temperature <= -10){
            type = 1;
            texture = new TextureRegion(GameScreen.loadTexture("decor\\winterTree.png"), 3, 160, 61, 64);
            treeType = 2;
        }
        else if(temperature >= 30){
            int delta = Math.random() < 0.4 ? 32 : 0;
            if(delta == 0){
                type = 2;
            }
            else{
                type = 3;
            }
            texture = GameScreen.loadRegion("decor\\desert_decor.png", 63 + delta, 126, 32, 64);
            treeType = 3;
        }
        else{
            picName = "decor\\tree-" + (int)(1.5 + (int)(Math.min(30, Math.max(temperature, 0)) / 31) + Math.random()) +  ".png";
            texture = GameScreen.loadRegion(picName);

            if(picName.equals("decor\\tree-1.png")){
                treeType = 2;
                type = 4;
            }
            else{
                type = 5;
            }
        }
        setTexture(texture);
        getRect().setCircle(true);

        setRectDelta(3);

        setMaterials(new int[]{7, 91});
    }

    public Tree(int type){
        super(new Rectangle3D(new Rectangle(0, 0, 20, 10), 0, 60));
        TextureRegion texture;
        if(type == 1){
            texture = new TextureRegion(GameScreen.loadTexture("decor\\winterTree.png"), 3, 160, 61, 64);
            treeType = 2;
        }
        else if(type == 2 || type == 3){
            int delta = 0;
            if(type == 3){
                delta = 32;
            }
            texture = GameScreen.loadRegion("decor\\desert_decor.png", 63 + delta, 126, 32, 64);
            treeType = 3;
        }
        else{
            texture = GameScreen.loadRegion("decor\\tree-" + (type - 3) + ".png");

            if(type - 3 == 1){
                treeType = 2;
            }
        }
        this.type = type;
        setTexture(texture);
        getRect().setCircle(true);

        setRectDelta(3);
    }

    public int getType() {
        return type;
    }

    public int getTreeType() {
        return treeType;
    }

    public void setTreeType(int treeType) {
        this.treeType = treeType;
    }
}
