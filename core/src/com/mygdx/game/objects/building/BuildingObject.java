package com.mygdx.game.objects.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.*;
import com.mygdx.game.objects.decor.obs.CaveWall;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.objects.inventory.FallenItem;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

import java.util.ArrayList;

public class BuildingObject extends Creature {
    private int buildingType;
    private int[] materials;
    private boolean isStatic = true;
    private Obstacle building;
    public final ArrayList<NumberButton1> nButtons = new ArrayList<>();
    public BuildingObject() {
        super(new TextureRegion(GameScreen.loadTexture("decor\\winterTree.png"), 0, 0, 0, 0), new Rectangle3D(new Rectangle(0, 0, 0, 0), 0, 1));
        setCanLookInv(false);
        getRect().setMaterial(false);
        setMass(1);
        setVisible(false);

        if(buildings.size() == 0) {
            for(int i = 1; i < 17; i++)
                buildings.add(new BuildingSlot(i));
        }

        if(nButtons.size() == 0) {
            for(int i = 1; i < 4; i++) {
                String str = "";
                switch (i) {
                    case 1:
                        str = "X size: ";
                        break;
                    case 2:
                        str = "Y size: ";
                        break;
                    case 3:
                        str = "Z size: ";
                        break;
                }
                nButtons.add(new NumberButton1(i, str, -300 + 300 * i, 300));
            }
        }
    }

    public int getBuildingType() {
        return buildingType;
    }

    public Obstacle getBuilding() {
        return building;
    }

    public void setStruct(Obstacle obs, int type){
        if(building != null) {
            boolean buildingIsFinish = true;
            for (int i = 0; i < materials.length; i += 2) {
                if (materials[i] > 0) {
                    buildingIsFinish = false;
                    break;
                }
            }

            if(!buildingIsFinish && !(building instanceof CaveWall)){
                building.remove();

                for (int i = 0; i < materials.length; i += 2) {
                    for(int j = 0; j < building.getMaterials()[i] - materials[i]; j++){
                        Item item = new Item(materials[i + 1]);
                        FallenItem fallenItem = new FallenItem(item);
                        fallenItem.setX(getRect().getX() + (float) (getRect().getWidth() * Math.random()));
                        fallenItem.setY(getRect().getY() + (float) (getRect().getHeight() * Math.random()));
                        getStage().addActor(fallenItem);

                        fallenItem.sort();
                    }
                }
            }
            else if(!buildingIsFinish && materials[0] != 10){
                building.getRect().setMaterial(true);

                setNullStruct();
            }
        }
        buildingType = type;
        building = obs;
        isStatic = false;

        checkMaterials(obs);

        getStage().addActor(building);
        building.getRect().setMaterial(false);
    }

    public void checkMaterials(Obstacle obs){
        materials = new int[obs.getMaterials().length];
        System.arraycopy(obs.getMaterials(), 0, materials, 0, obs.getMaterials().length);
        getInventory().clearNeedRes();

        itemTextures.clear();

        for(int i = 0; i < materials.length; i += 2) {
            getInventory().addNeedRes(materials[i + 1]);

            Item item = new Item(materials[i + 1]);
            itemTextures.add(item);
        }
    }

    public void setBuilding(Obstacle building) {
        this.building = building;
    }

    public void update(int i){
        if(building instanceof CaveWall){
            ((CaveWall) building).setHeight1(10 - materials[0]);
        }

        if(materials[i] == 0){
            itemTextures.remove((i / 2) - (materials.length / 2) + itemTextures.size() / 2);
        }
    }

    public int[] getMaterials(){
        return materials;
    }

    private boolean isTouch;

    public void setTouch(boolean touch) {
        isTouch = touch;
    }

    public boolean canChange;

    @Override
    public void act(float delta) {
        if(GameScreen.player.isSeeBuilding()) {
            for(int i = 0; i < buildings.size(); i++){
                float x = GameScreen.player.getCentralX() - 350 + 70 * (i % 10);
                float y = GameScreen.player.getY() + GameScreen.player.getZ() + 150 - 70 * (i / 10);

                buildings.get(i).setPosition(x, y);

                buildings.get(i).setVisible(true);
            }

            for(NumberButton1 b : nButtons) {
                b.setPosition(GameScreen.player.getCentralX() - 350 + b.deltaX, GameScreen.player.getY() + GameScreen.player.getZ() + 150 - b.deltaY);
                b.setVisible(true);
            }
        }
        else{
            for (BuildingSlot building : buildings) {
                building.setVisible(false);
            }

            for(NumberButton1 b : nButtons)
                b.setVisible(false);
        }

        if(building != null) {
            setVisible(true);

            building.setTransparency(0.5f);

            if (isStatic) {
                building.setZ(getZ());
                building.setzSpeed(0);
                if (!(building instanceof CaveWall)) {
                    boolean buildingIsFinish = true;
                    for (int i = 0; i < materials.length; i += 2) {
                        if (materials[i] > 0) {
                            buildingIsFinish = false;
                            break;
                        }
                    }

                    if (buildingIsFinish) {
                        building.getRect().setMaterial(true);

                        setNullStruct();
                    }
                }
            } else {
                Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                building.getRect().setMaterial(true);
                if (!building.onGround() && cell != null)
                    building.setZ(cell.getNormalZ());
                MyActor obs = building.getOneIntersectingObject(Obstacle.class);
                while (obs != null) {
                    building.setZ(obs.getZ() + obs.getRectDeltaZ() + obs.getRect().getzSize());
                    obs = building.getOneIntersectingObject(Obstacle.class);
                }
                building.getRect().setMaterial(false);

                if (isTouch && !Gdx.input.isTouched() && !isTouching(MyActor.class) && getZ() >= 0) {
                    isStatic = true;
                } else if (!isStatic) {
                    float touchX = GameScreen.getMouseX();
                    float touchY = GameScreen.getMouseY();
                    float rot = (float) (180 * Math.atan2(touchY - getStage().getViewport().getWorldHeight() / 2, touchX - getStage().getViewport().getWorldWidth() / 2) / Math.PI);
                    GameScreen.player.setRot(Math.abs(rot) <= 45 ? 3 : rot > 45 && rot <= 135 ? 0 : Math.abs(rot) > 135 ? 1 : 2);

                    int dist = 30 + (int) Math.sqrt(Math.pow(getRect().getWidth() / 2, 2) + Math.pow(getRect().getHeight() / 2, 2));
                    setCentralX((float) (GameScreen.player.getCentralX() + (Math.cos(Math.toRadians(rot)) * dist)));
                    setY((float) (GameScreen.player.getRectCenterY() + (Math.sin(Math.toRadians(rot)) * dist)));
                }

                building.setCentralX(getX());
                building.setY(getY());
                building.sort();
                building.setzSpeed(0);
                setZ(building.getZ());

                if(!Gdx.input.isTouched())
                    canChange = true;

                if(canChange)
                    isTouch = Gdx.input.isTouched();

                sort();
            }
        }

        if(GameScreen.isKeyDown(8)){
            setNullStruct();
        }
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public void setMaterials(int[] materials) {
        this.materials = materials;

        itemTextures.clear();

        for(int i = 0; i < materials.length; i += 2) {
            getInventory().addNeedRes(materials[i + 1]);

            Item item = new Item(materials[i + 1]);
            itemTextures.add(item);
        }
    }

    public void setNullStruct(){
        buildingType = 0;
        isStatic = true;
        building = null;
        canChange = false;
        setVisible(false);
        setRect(new Rectangle3D(new Rectangle(0, 0, 0, 0), 0, 0));
        getRect().setMaterial(false);

        setMass(1);

        getInventory().clearNeedRes();

        itemTextures.clear();

    }

    private final ArrayList<Item> itemTextures = new ArrayList<>();
    public static final ArrayList<BuildingSlot> buildings = new ArrayList<>();
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        /*batch.setColor(color.r, color.g, color.b, 0.5f);
        if(!(building instanceof CaveWall)) {
            batch.draw(getTexture(), getX(), getY() + getZ(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
        else{
            int y = 0;
            getTexture().setRegion(0, 106, 55, 8);
            draw(y, batch);
            y += 8 * getScaleY();

            getTexture().setRegion(0, 47, 55, 59);
            for(int i = 0; i < ((CaveWall) building).getHeight1() / 59; i++){
                draw(y, batch);
                y += 59 * getScaleY();
            }

            getTexture().setRegion(0, 47, 59, ((CaveWall) building).getHeight1() % 59);
            draw(y, batch);
            y += (((CaveWall) building).getHeight1() % 59) * getScaleY();

            getTexture().setRegion(0, 0, 55, 47);
            draw(y, batch);
        }*/

        batch.setColor(color.r, color.g, color.b, 1);
        float x = getX() + getTexture().getRegionWidth() * getScaleX() / 2 - (15 + 20 * (itemTextures.size() / 2)) / 2;
        for(Item item : itemTextures){
            float cof = 15f / Math.max(item.getItemTexture().getRegionWidth(), item.getItemTexture().getRegionHeight());
            batch.draw(item.getItemTexture(), x, getY() + getZ() + 50, getOriginX(), getOriginY(),
                    item.getItemTexture().getRegionWidth(), item.getItemTexture().getRegionHeight(), cof, cof, getRotation());
            x += 20;
        }
    }

    public void draw(int y, Batch batch){
        batch.draw(getTexture(), getX(), getY() + getZ() + y, getOriginX(), getOriginY(), getTexture().getRegionWidth(), getTexture().getRegionHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
