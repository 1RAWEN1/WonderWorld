package com.mygdx.game.objects.decor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.buttons.gameButtons.Joystick;
import com.mygdx.game.objects.characters.neutral.Mage;
import com.mygdx.game.objects.characters.*;
import com.mygdx.game.objects.characters.enemy.Bug;
import com.mygdx.game.objects.characters.enemy.SkeletonMage;
import com.mygdx.game.objects.characters.neutral.Wolf;
import com.mygdx.game.objects.characters.peaceful.Fish;
import com.mygdx.game.objects.characters.peaceful.Rabbit;
import com.mygdx.game.objects.decor.nature.Algae;
import com.mygdx.game.objects.decor.nature.Tree;
import com.mygdx.game.objects.decor.obs.CaveWall;
import com.mygdx.game.objects.inventory.FallenItem;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

import java.util.ArrayList;

public class Cell extends Actor {
    private float temperature;
    private TextureRegion texture;
    private int cellType;
    public static final ArrayList<Cell> sources = new ArrayList<>();
    private float biomeVal = 1;
    private TextureRegion back;
    public Cell(int cellType1){
        cellType = cellType1;

        setBounds(0, 0, 32, 32);
    }

    public float getTemperature() {
        return temperature;
    }

    private final int radius = 1000;
    public void generateBiomes(){
        biomeVal = 1;

        int cells = 1;
        for(int i = 0; i < 4; i++){
            Cell cell = GameScreen.getCellAt((float) (getX() + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + (Math.sin(Math.toRadians(i * 90)) * 32)));
            if(cell != null && cell.updated) {
                biomeVal = (biomeVal * (float)(cells - 1) / cells) + (cell.getBiomeVal() / cells);
                cells++;
            }
        }

        if(Math.random() <= 0.5)
            biomeVal += (float) (Math.random() * 0.3);
        else
            biomeVal -= (float) (Math.random() * 0.3);

        if(biomeVal > 2.5){
            biomeVal -= (float) (Math.random() * 0.3);
        }

        z = (biomeVal - 1) * 30;

        double v = Math.pow(getX() - GameScreen.squareSize * GameScreen.waterX, 2) + Math.pow(getY() - GameScreen.squareSize * GameScreen.waterY, 2);
        if(v < Math.pow(radius, 2) ||
        cellType == 0){
            cellType = 0;

            checkDepth();
        }
        else if(biomeVal > 2){
            cellType = 2;
        }
    }

    public static float waterZ;
    public void checkDepth(){
        double v = Math.pow(getX() - GameScreen.squareSize * GameScreen.waterX, 2) + Math.pow(getY() - GameScreen.squareSize * GameScreen.waterY, 2);
        depth = Math.max(-60, -(float) ((1 - (v / Math.pow(radius, 2))) * 100));
        waterLevel = Math.min(60, (float) ((1 - (v / Math.pow(radius, 2))) * 100));

        if(waterZ == 0){
            waterZ = z - 50;

            z -= waterLevel + 50;
        }
        else{
            z = waterZ - waterLevel;
        }
    }

    private boolean updated = false;

    public boolean isUpdated() {
        return updated;
    }

    public void sort(){
        Array<Actor> actors = getStage().getActors();
        //System.out.println(index + " index1");
        int index = getZIndex();
        while (index - 1 >= 0 && !(actors.get(index - 1) instanceof MyActor) &&
                !(actors.get(index - 1) instanceof Cell)) {
            actors.get(index - 1).toFront();
            index--;
        }
        //System.out.println(index + " index2");

        boolean hasChange = false;
        if(index + 1 < actors.size - GameScreen.endEntities) {
            float height = getY() + 27;
            Actor act2 = actors.get(index + 1);
            float actorHeight = act2.getY();
            if(act2 instanceof Cell) {
                actorHeight += 27;
            }
            int index1 = index;

            while (actorHeight > height){
                hasChange = true;
                index1 ++;

                if(index1 + 1 < actors.size - GameScreen.endEntities) {
                    act2 = actors.get(index1 + 1);
                    actorHeight = act2.getY();
                    if(act2 instanceof Cell) {
                        actorHeight += 27;
                    }
                    /*if(act2 instanceof MyActor){
                        if (((MyActor)act2).getZ() >= getNormalZ())
                            actorHeight = height - 1;
                    }
                    else */
                }
                else{
                    break;
                }
            }

            if(hasChange)
                setZIndex(act2.getZIndex() - 1);
        }

        if(!hasChange && index - 1 >= 0) {
            float height = getY() + 27;
            Actor act2 = actors.get(index - 1);
            float actorHeight = act2.getY();
            if(act2 instanceof Cell) {
                actorHeight += 27;
            }
            int index2 = index;

            boolean toBack = false;
            while (actorHeight < height){
                hasChange = true;
                index2 --;

                if(index2 - 1 >= 0) {
                    act2 = actors.get(index2 - 1);
                    actorHeight = act2.getY();
                    if(act2 instanceof Cell) {
                        actorHeight += 27;
                    }
                }
                else{
                    toBack = true;
                    break;
                }
            }

            if(hasChange)
                if(toBack)
                    toBack();
                else
                    setZIndex(act2.getZIndex() + 1);

            /*System.out.println(GameScreen.endEntities);
            for(int i = 0; i < GameScreen.endEntities; i++){
                System.out.println(getStage().getActors().get(getStage().getActors().size - 1 - i) + " " +
                        (getStage().getActors().get(getStage().getActors().size - 1 - i) == this));
            }
            System.out.println(getY() + " " + getZIndex() + " " + act2.getY() + " " + act2 + " " + getStage().getActors().size);*/
        }
    }

    private TextureRegion water;
    String picName;
    public void setImage() {
        if (temperature <= -10) {
            picName = "decor\\winter.png";
            if(back == null){
                back = GameScreen.loadRegion("ground");
            }
        } else if (temperature >= 30) {
            picName = "decor\\desert.png";
            if(back == null){
                back = GameScreen.loadRegion("desert");
            }
        } else {
            picName = "decor\\forest.png";
            if(back == null){
                back = GameScreen.loadRegion("ground");
            }
        }
        switch (cellType) {
            case 0:
                if(waterLevel > 0)
                    picName = "decor\\deep_water.png";
                else
                    picName = "decor\\forest.png";
                break;
            case 2:
                picName = "decor\\cave_back.png";

                back = GameScreen.loadRegion("cave");

                break;
            case 3:
                water = GameScreen.loadRegion("decor\\WaterAndFire.png", 0, 0, 32, 32);
                break;
            case 4:
                picName = "decor\\house.png";
                break;
            case 5:
                water = GameScreen.loadRegion("decor\\hole.png");
                break;
        }
        if (temperature <= -10 && cellType == 1){
            texture = GameScreen.loadRegion(picName, 224, 0, 32, 32);
        }
        else{
            texture = GameScreen.loadRegion(picName);
        }

        updated = true;
    }

    public float getBiomeVal() {
        return biomeVal;
    }

    private float light = 0;

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
        for (int i = 0; i < 4; i++) {
            Cell cell = GameScreen.getCellAt((float) (getX() + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + (Math.sin(Math.toRadians(i * 90)) * 32)));
            if (cell != null && cell.getLight() < getLight() - 0.04) {
                cell.setLight(getLight() - 0.04f, i * 90);
            }
        }
    }

    public void updateLight() {
        for (int i = 0; i < 4; i++) {
            Cell cell = GameScreen.getCellAt((float) (getX() + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + (Math.sin(Math.toRadians(i * 90)) * 32)));
            if (cell != null && cell.getLight() <= getLight() - 0.04) {
                cell.setLight(getLight() - 0.04f, i * 90);
            }
        }
    }

    public float getOppositeRot(float rot){
        return (rot + 180) % 360;
    }

    public void setLight(float light, int rot) {
        rot = (int) getOppositeRot(rot);
        this.light = light;
        for (int i = 0; i < 4; i++) {
            if(i * 90 != rot) {
                Cell cell = GameScreen.getCellAt((float) (getX() + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + (Math.sin(Math.toRadians(i * 90)) * 32)));
                if (cell != null && cell.getLight() <= getLight() - 0.04) {
                    cell.setLight(getLight() - 0.04f);
                }
            }
        }

        sources.add(this);
    }

    public void deleteLight(){
        for (int i = 0; i < 4; i++) {
            Cell cell = GameScreen.getCellAt((float) (getX() + 16 + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + 16 + (Math.sin(Math.toRadians(i * 90)) * 32)));
            if (cell != null && cell.getLight() == getLight() - 0.04) {
                cell.deleteLight(i * 90);
            }
        }
        sources.remove(this);

        for(Cell cell : sources) {
            if((int)(Math.abs(cell.getX() - getX()) / 32) + (int)(Math.abs(cell.getY() - getY()) / 32) < (light + cell.getLight()) / 0.04){
                cell.updateLight();
            }
        }
        light = 0;
    }

    public void deleteLight(int rot){
        rot = (int) getOppositeRot(rot);
        for (int i = 0; i < 4; i++) {
            if(i * 90 != rot) {
                Cell cell = GameScreen.getCellAt((float) (getX() + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + (Math.sin(Math.toRadians(i * 90)) * 32)));
                if (cell != null && cell.getLight() == getLight() - 0.04) {
                    cell.deleteLight(i * 90);
                }
            }
        }
        light = 0;
    }

    private boolean createObjects = true;

    public void setCreateObjects(boolean createObjects) {
        this.createObjects = createObjects;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setBiomeVal(float biomeVal) {
        this.biomeVal = biomeVal;
        z = (biomeVal - 1) * 30;
    }

    @Override
    public void setY(float y) {
        super.setY(y);

        GameScreen.cells[(int)(getX() / 32) + 122][(int)(getY() / 32) + (GameScreen.cells[0].length - 70) / 2] = this;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        GameScreen.cells[(int)(getX() / 32) + 122][(int)(getY() / 32) + (GameScreen.cells[0].length - 70) / 2] = this;
    }

    /*public void checkWater(){
        for(int i = 2; i < 4; i++){
            Cell cell = GameScreen.getCellAt((float) (getX() + 16 + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + 16 + (Math.sin(Math.toRadians(i * 90)) * 32)));
            if(cell != null && cell.cellType == 0) {
                cellType = 0;
            }
        }
    }*/

    private float waterLevel;

    public float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(float waterLevel) {
        this.waterLevel = waterLevel;
    }

    private void checkWater(){
        for(int i = 0; i < 4; i++){
            Cell cell = GameScreen.getCellAt((float) (getX() + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + (Math.sin(Math.toRadians(i * 90)) * 32)));
            if(cell != null && getNormalZ() + waterLevel > cell.getNormalZ() + cell.waterLevel) {
                float newValue = (getNormalZ() + waterLevel + cell.getNormalZ() + cell.waterLevel) / 2;
                waterLevel = Math.max(0, newValue - z);
                cell.waterLevel = Math.max(0, newValue - cell.z);
            }
        }
    }
    private float z;
    private boolean setLight = false;
    @Override
    public void act(float delta) {
        if(createObjects){
            temperature = ((getX() / 128 - (getY() / 64))) / 2 + (int)(Math.random() * 10) + 7;

            if(cellType == 4) {
                setLight(0.4f);

                setLight = true;
            }
            else{
                for (int i = 0; i < 4; i++) {
                    Cell cell = GameScreen.getCellAt((float) (getX() + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (getY() + (Math.sin(Math.toRadians(i * 90)) * 32)));
                    if (cell != null && cell.getLight() - 0.04 > getLight()) {
                        light = cell.getLight() - 0.04f;
                    }
                }
            }

            double dist = Math.pow(35 * 32 - getX(), 2) + Math.pow(32 * 35 - getY(), 2);
            if(dist > Math.pow(18 * 32, 2))
                generateBiomes();
            else
                sort();

            if(cellType == 1){
                cellType = Math.random() < 0.995 + 0.05 * Math.abs(30 - temperature) / 30 ? cellType : 3;
            }

            int i = Math.random() * 100 < 80 + (temperature >= 30 ? 15 : 0) ? 0 : -1;
            i = Math.random() * 100 < 99 ? i : -2;
            i = Math.random() * 100 < 99.8 ? i : -3;
            i = Math.random() * 100 < 99.9 ? i : -4;
            i = Math.random() * 100 < 99.9 ? i : -6;
            i = Math.random() * 100 < 99.95 ? i : -5;

            if(temperature <= -10 && cellType == 3){
                cellType = 1;
            }
            double c = Math.pow(getX() - GameScreen.squareSize * GameScreen.nestX, 2) + Math.pow(getY() - GameScreen.squareSize * GameScreen.nestY, 2);

            double v = Math.pow(getX() - GameScreen.squareSize * GameScreen.caveCordX, 2) + Math.pow(getY() - GameScreen.squareSize * GameScreen.caveCordY, 2);
            if(v <= Math.pow(GameScreen.caveDist + 16, 2)){
                z = 0;

                cellType = 2;
                CaveWall cave = new CaveWall(32, 32, 10);
                cave.setX(getX());
                cave.setY(getY());
                cave.setZ((float) (65 + ((Math.pow(GameScreen.caveDist + 16, 2) - v) * 0.0007)));
                getStage().addActor(cave);

                cave.sort();

                /*boolean addSkeleton = false;
                for (int j = 0; j < GameScreen.skeletonCords.length; j += 2){
                    if(GameScreen.skeletonCords[j] == getX() / 32 &&
                            GameScreen.skeletonCords[j + 1] == getY() / 32) {
                        addSkeleton = true;
                        break;
                    }
                }*/

                if(getX() / 32 == GameScreen.caveCordX && getY() / 32 == GameScreen.caveCordY){
                    SkeletonMage skeletonMage = new SkeletonMage();
                    skeletonMage.setX(getX() - 16);
                    skeletonMage.setY(getY());
                    getStage().addActor(skeletonMage);

                    skeletonMage.sort();
                }
                else if(v < Math.pow(GameScreen.caveDist - 32, 2) && Math.random() < 0.2){
                    CaveWall cave1 = new CaveWall(25, 7, 32);
                    cave1.setX(getX() + 5);
                    cave1.setY(getY() + 10);
                    getStage().addActor(cave1);

                    cave1.sort();
                }

                float rot = (float) (180 * Math.atan2(GameScreen.caveCordY * GameScreen.squareSize - getY(), getX() - GameScreen.squareSize * GameScreen.caveCordX) / Math.PI);
                if(v >= Math.pow(GameScreen.caveDist - 16, 2)) {
                    if(rot > 105 || rot < 75) {
                        CaveWall cave2 = new CaveWall(32, 32, 74);
                        cave2.setX(getX());
                        cave2.setY(getY());
                        getStage().addActor(cave2);

                        cave2.sort();
                    }
                    /*else if (!hasEnter) {
                        hasEnter = true;
                        CaveWall cave2 = new CaveWall(106, 5, 20);
                        cave2.setX(getX() + 22);
                        cave2.setY(getY());
                        getStage().addActor(cave2);
                    }*/
                }
            }
            else if(c <= Math.pow(176, 2)){
                if((int)(getX() / 32) == GameScreen.nestX && (int)(getY() / 32) == GameScreen.nestY){
                    cellType = 5;
                }
            }
            else if(cellType == 0){
                if(Math.random() < 0.01){
                    Fish fish = new Fish();
                    fish.setX(getX());
                    fish.setY(getY());
                    fish.setZ(getNormalZ());
                    getStage().addActor(fish);

                    fish.sort();
                }
                else if(depth < -40 && Math.random() < 0.02){
                    Algae algae = new Algae();
                    algae.setX(getX() + 5);
                    algae.setY(getY() + 10);
                    algae.setZ(getNormalZ());
                    getStage().addActor(algae);

                    algae.sort();
                }
            }
            else if(cellType == 1) {
                if(i == -6){
                    FallenItem fi = new FallenItem(new Item(100));
                    fi.setPosition(getX() + 6, getY() + 10);
                    fi.setZ(getNormalZ());
                    getStage().addActor(fi);

                    fi.sort();
                }
                else if(i == -5){
                    Mage mage = new Mage();
                    mage.setPosition(getX() + 6, getY() + 10);
                    mage.setZ(getNormalZ());
                    getStage().addActor(mage);

                    mage.sort();
                }
                else if (i == -3 && temperature < 30) {
                    Rabbit rabbit = new Rabbit(false);
                    rabbit.setX(getX());
                    rabbit.setY(getY());
                    rabbit.setZ(getNormalZ());
                    getStage().addActor(rabbit);

                    rabbit.sort();
                } else if (i == -4 && temperature < 30) {
                    Wolf wolf = new Wolf();
                    wolf.setX(getX() - 16);
                    wolf.setY(getY() + 8);
                    wolf.setZ(getNormalZ());
                    getStage().addActor(wolf);

                    wolf.sort();
                }
                else if(temperature >= 30 && Math.random() < 0.01){
                    Bug bug = new Bug(false);
                    bug.setX(getX());
                    bug.setY(getY());
                    bug.setZ(getNormalZ());
                    getStage().addActor(bug);

                    bug.sort();
                }
                else if (i == -1) {
                    Tree tree = new Tree(temperature, this);
                    tree.setX(getX() - (temperature < 30 ? 16 : 0));
                    tree.setY(getY() + (temperature < 30 ? 0 : 10));
                    tree.setZ(z);
                    getStage().addActor(tree);

                    tree.sort();
                }
            }
            else if(cellType == 2) {
                if (biomeVal >= 2.5 || Math.random() < 0.7){
                    CaveWall cave = new CaveWall(Math.max(1, (int) ((biomeVal - 2) * 50)));
                    cave.setX(getX());
                    cave.setY(getY());
                    cave.setZ(z);
                    getStage().addActor(cave);

                    cave.sort();
                }
                else{
                    CaveWall cave = new CaveWall(25, 7, 32);
                    cave.setX(getX() + 5);
                    cave.setY(getY() + 10);
                    cave.setZ(z);
                    getStage().addActor(cave);

                    cave.sort();
                }
            }

            setImage();

            createObjects = false;
        }
        else if(cellType == 5 && !GameScreen.hasQueen && Math.pow(GameScreen.player.getCentralX() - getX() - 16, 2) +
        Math.pow(GameScreen.player.getRectCenterY() - getY() - 16, 2) <= 10000 && Bug.colonyThinking < 50){
            GameScreen.hasQueen = true;
            Bug bug = new Bug(true);
            bug.setX(getX());
            bug.setY(getY());
            bug.setZ(getNormalZ());
            getStage().addActor(bug);

            bug.sort();

            for(int i = 0; i < 5; i++){
                Bug bug1 = new Bug(false);
                bug1.setX((float) (getX() + Math.cos(Math.toRadians(i * 72)) * 20));
                bug1.setY((float) (getY() + Math.sin(Math.toRadians(i * 72)) * 20));
                bug1.setZ(getNormalZ());
                getStage().addActor(bug1);

                bug1.sort();
            }
        }
        else if(cellType == 5 && Bug.colonyThinking >= 50 && Math.random() < 0.0005){
            Item item = new Item(103);
            FallenItem fallenItem = new FallenItem(item);
            fallenItem.setX(getX() + (float)(32 * Math.random()));
            fallenItem.setY(getY() + (float)(32 * Math.random()));
            fallenItem.setZ(z);
            getStage().addActor(fallenItem);

            fallenItem.sort();
        }

        if(!setLight && cellType == 4){
            setLight(0.4f);

            setLight = true;
        }

        if(waterLevel > 0){
            texture = GameScreen.loadRegion("decor\\deep_water.png");
            water = null;

            checkWater();
            setImage = true;
        }
        else if(setImage){
            setImage();
            setImage = false;
        }
    }

    private boolean setImage = false;

    public static Cell cell;

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX(Joystick.index);
        float touchY = GameScreen.getMouseY(Joystick.index);

        return touchX >= (getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2) && touchX <= getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2 + texture.getTexture().getWidth()
                && touchY >= (getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2) && touchY <= getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2 + texture.getTexture().getHeight();
    }

    public float getDarkCof(){
        return Math.max(0, GameScreen.darkCof - light);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(texture != null) {
            float darkCof = Math.max(0, 1 - Math.max(0, GameScreen.darkCof - light) - GameScreen.player.getDarkCof());
            setColor(darkCof, darkCof, darkCof, 1);
            batch.setColor(getColor());
            if(cell == this){
                batch.setColor(1, 0, 0, 1);
            }
            batch.draw(texture, getX(), getY() + waterLevel + z);

            Cell prevCell = GameScreen.getCellAt(getX(), getY() - 32);
            if(waterLevel == 0 && prevCell != null && prevCell.getNormalZ() + prevCell.getWaterLevel() < getNormalZ()){
                batch.draw(back, getX(), prevCell.getY() + 32 + prevCell.getNormalZ() + prevCell.getWaterLevel(), getOriginX(), getOriginY(),
                        getWidth(), getNormalZ() - prevCell.getWaterLevel() - prevCell.getNormalZ(), getScaleX(), getScaleY(), getRotation());
            }

            if(water != null && waterLevel == 0){
                batch.draw(water, getX(), getY() + z);
            }

            Cell nextCell = GameScreen.getCellAt(getX(), getY() + 32);
            if(waterLevel == 0 && nextCell != null && nextCell.getNormalZ() + nextCell.getWaterLevel() + 2 < getNormalZ()){
                batch.draw(GameScreen.loadRegion("gray"), getX(), getY() + z + 31, getOriginX(), getOriginY(),
                        getWidth(), 1, getScaleX(), getScaleY(), getRotation());
            }

            Cell right = GameScreen.getCellAt(getX() + 32, getY());
            if(waterLevel == 0 && right != null && right.getNormalZ() + right.getWaterLevel() + 2 < getNormalZ()){
                batch.draw(GameScreen.loadRegion("gray"), getX() + 31, getY() + z, getOriginX(), getOriginY(),
                        1, 32, getScaleX(), getScaleY(), getRotation());
            }

            Cell left = GameScreen.getCellAt(getX() - 32, getY());
            if(waterLevel == 0 && left != null && left.getNormalZ() + left.getWaterLevel() + 2 < getNormalZ()){
                batch.draw(GameScreen.loadRegion("gray"), getX(), getY() + z, getOriginX(), getOriginY(),
                        1, 32, getScaleX(), getScaleY(), getRotation());
            }
        }
    }

    public boolean isCreateObjects() {
        return createObjects;
    }

    public final ArrayList<MyActor> actors = new ArrayList<>();

    public void add(Stage stage){
        stage.addActor(this);
        sort();

        for(MyActor act1 : actors)
            if(!(act1 instanceof Player)) {
                stage.addActor(act1);

                act1.sort();
            }
    }

    public void remove1(){
        remove();

        for (MyActor actor : actors)
            if(!(actor instanceof Player))
                actor.remove1();
    }

    float depth;
    public float getZ(float x, float y){
        return cellType == 3 ? (float)((1 - ((Math.pow(x - getX() - 16, 2) + Math.pow(y - getY() - 16, 2)) / (Math.pow(16, 2) * 2))) * -10) + z : z;
    }

    public float getNormalZ(){
        return z;
    }

    public float getDepth() {
        return depth;
    }

    public int getCellType() {
        return cellType;
    }
}
