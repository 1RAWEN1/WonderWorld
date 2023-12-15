package com.mygdx.game.objects.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.withInventory.Chest;
import com.mygdx.game.objects.characters.withInventory.Interior;
import com.mygdx.game.objects.characters.withInventory.Oven;
import com.mygdx.game.objects.characters.withInventory.Workbench;
import com.mygdx.game.objects.characters.withInventory.AlchemyTable;
import com.mygdx.game.objects.decor.obs.Block;
import com.mygdx.game.objects.decor.obs.CaveWall;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.objects.decor.obs.Wall;
import com.mygdx.game.scenes.GameScreen;

public class BuildingSlot extends Actor {
    private final Texture texture = GameScreen.loadTexture("inventory\\slot.png");
    private int type;
    private Obstacle obstacle;

    public BuildingSlot(int type) {
        this.type = type;
        updateObj();
    }

    public void updateObj(){
        switch (type){
            case 1:
                obstacle = new Obstacle("decor\\fence.png", new TextureRegion(GameScreen.loadTexture("decor\\fence.png")), new Rectangle3D(new Rectangle(0, 0, 32, 20), 0, 30), new int[]{5, 91});
                break;
            case 2:
                obstacle = new Obstacle("decor\\house.png", new TextureRegion(GameScreen.loadTexture("decor\\house.png")), new Rectangle3D(new Rectangle(0, 0, 32, 32), 0, 0), new int[]{3, 91});
                break;
            case 3:
                obstacle = new Wall(37, 5, 60);
                break;
            case 4:
                obstacle = new Wall(32, 5, 60);
                break;
            case 5:
                obstacle = new Wall(5, 32, 60);
                break;
            case 6:
                obstacle = new Wall(96, 64, 30);
                break;
            case 7:
                obstacle = new Interior();
                break;
            case 8:
                obstacle = new Workbench();
                break;
            case 9:
                obstacle = new Oven(1);
                break;
            case 10:
                obstacle = new Chest();
                break;
            case 11:
                obstacle = new CaveWall(0);
                break;
            case 12:
                obstacle = new Wall(32, 5, 60, false);
                break;
            case 13:
                obstacle = new Wall(32, 5, 60, true);
                break;
            case 14:
                obstacle = new AlchemyTable();
                break;
            case 15:
                obstacle = new Oven(2);
                break;
            case 16:
                obstacle = new Block();
                break;
        }
    }

    public Obstacle getBuildingObject() {
        return obstacle;
    }

    @Override
    public void act(float delta) {
        if(GameScreen.player.isSeeBuilding() && Gdx.input.isTouched() && touchThis()){
            GameScreen.player.setSeeBuilding(false);

            setStruct();
        }
    }

    public void setStruct(){
        GameScreen.buildingObject.setStruct(obstacle, type);
        GameScreen.buildingObject.canChange = false;

        updateObj();
    }

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX();
        float touchY = GameScreen.getMouseY();

        return touchX >= (getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2) && touchX <= getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2 + texture.getWidth()
                && touchY >= (getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2) && touchY <= getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2 + texture.getHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setScale(1);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY());

        int size = Math.max(obstacle.getTexture().getRegionWidth(), obstacle.getTexture().getRegionHeight());
        setScale(Math.min(1, (float) (texture.getWidth() - 10) / size));

        batch.draw(obstacle.getTexture(), getX() - (obstacle.getTexture().getRegionWidth() * getScaleX() / 2) + (texture.getWidth() / 2),
                    getY() - (obstacle.getTexture().getRegionHeight() * getScaleY() / 2) + (texture.getHeight() / 2), getOriginX(), getOriginY(),
                obstacle.getTexture().getRegionWidth(), obstacle.getTexture().getRegionHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
