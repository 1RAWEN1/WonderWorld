package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.State;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.building.BuildingObject;
import com.mygdx.game.objects.building.BuildingSlot;
import com.mygdx.game.objects.building.NumberButton1;
import com.mygdx.game.objects.buttons.*;
import com.mygdx.game.objects.buttons.gameButtons.*;
import com.mygdx.game.objects.buttons.magicButtons.MagicButton;
import com.mygdx.game.objects.buttons.magicButtons.MagicEffectButton;
import com.mygdx.game.objects.buttons.magicButtons.MagicRefactorButton;
import com.mygdx.game.objects.buttons.magicButtons.NumberButton;
import com.mygdx.game.objects.characters.*;
import com.mygdx.game.objects.characters.withInventory.*;
import com.mygdx.game.objects.characters.effects.DealtEffect;
import com.mygdx.game.objects.characters.enemy.Bug;
import com.mygdx.game.objects.characters.enemy.Skeleton;
import com.mygdx.game.objects.characters.enemy.SkeletonMage;
import com.mygdx.game.objects.characters.neutral.Mage;
import com.mygdx.game.objects.characters.neutral.Villager;
import com.mygdx.game.objects.characters.neutral.Wolf;
import com.mygdx.game.objects.characters.peaceful.Fish;
import com.mygdx.game.objects.characters.peaceful.Rabbit;
import com.mygdx.game.objects.decor.*;
import com.mygdx.game.objects.decor.nature.Algae;
import com.mygdx.game.objects.decor.nature.Tree;
import com.mygdx.game.objects.decor.obs.Block;
import com.mygdx.game.objects.decor.obs.CaveWall;
import com.mygdx.game.objects.decor.obs.Wall;
import com.mygdx.game.objects.inventory.*;
import com.mygdx.game.objects.mechanisms.Mech;
import com.mygdx.game.objects.serialization.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GameScreen implements Screen {
    public static boolean hasKnight = false;
    public static boolean hasElf = false;
    public static int picHeight = 64;
    private static Stage stage;

    private static int[][] map;

    public static int[][] getMap(){
        return map;
    }

    public static Player player;
    private static final Label helpLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    private final Label descLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    public static DialogReader dialogReader;

    public static ArrayList<Integer> buttons = new ArrayList<>();
    private static final Label moneyLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
    public static final Label magicLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

    private final MyGdxGame game;

    public static Cell[][] cells;
    public static HashMap<String, Texture> textures = new HashMap<>();
    public GameScreen(final MyGdxGame game){
        regions = new HashMap<>();
        dialogReader = new DialogReader();
        cells = new Cell[314][304];

        this.game = game;
        hasKnight = false;
        hasElf = false;

        Cell.waterZ = 0;

        stage = new Stage(new FitViewport(1200, 750));
        Gdx.input.setInputProcessor(stage);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        pixmap.setColor(94f / 255, 82f / 255, 82f / 255, 1);
        pixmap.fill();

        textures.put("cave", new Texture(pixmap));
        regions.put("cave", new TextureRegion(textures.get("cave")));

        pixmap.setColor(Color.CLEAR);
        pixmap.fill();

        pixmap.setColor(217f / 255, 190f / 255, 65f/ 255, 1);
        pixmap.fill();

        textures.put("desert", new Texture(pixmap));
        regions.put("desert", new TextureRegion(textures.get("desert")));

        pixmap.setColor(Color.CLEAR);
        pixmap.fill();

        pixmap.setColor(156f / 255, 100f / 255, 65f/ 255, 1);
        pixmap.fill();

        textures.put("ground", new Texture(pixmap));
        regions.put("ground", new TextureRegion(textures.get("ground")));

        pixmap.setColor(Color.CLEAR);
        pixmap.fill();

        pixmap.setColor(0, 0, 0, 0.3f);
        pixmap.fill();

        textures.put("gray", new Texture(pixmap));
        regions.put("gray", new TextureRegion(textures.get("gray")));

        pixmap.dispose();

        try {
            createSavedMap(getSave());

            createButtons();
        } catch (IOException | ClassNotFoundException e) {
            generateMap();
        }

        if(inventorySlots.size() == 0)
            generateInventorySlots(player.getX(), player.getY());
        else
            addInventorySlots();

        stage.addActor(player);

        inventorySizeLabel.setFontScale(2f);
        stage.addActor(inventorySizeLabel);

        moneyLabel.setFontScale(2f);
        stage.addActor(moneyLabel);
        stage.addActor(descLabel);
        magicLabel.setFontScale(2f);
        stage.addActor(magicLabel);
        helpLabel.setFontScale(2f);
        stage.addActor(helpLabel);
        helpLabel.setVisible(false);

        stage.addActor(dialogReader);

        addButtons();

        sortCells();
        sortObstacles();
    }

    public static SettingsSave getSettingsSave() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("settings.ser");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        SettingsSave settingsSave = (SettingsSave) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();

        return settingsSave;
    }

    public static float volume = 1;
    public static void saveButtons() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("settings.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        SettingsSave settingsSave = new SettingsSave(buttons, volume);
        objectOutputStream.writeObject(settingsSave);

        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static void addButtons() {
        if(buttons.size() == 0) {
            try {
                SettingsSave settingsSave = getSettingsSave();

                buttons = settingsSave.getButtons();
            }
            catch (Exception e) {
                buttons.add(Input.Keys.W);
                buttons.add(Input.Keys.S);
                buttons.add(Input.Keys.D);
                buttons.add(Input.Keys.A);
                buttons.add(Input.Keys.SPACE);

                buttons.add(Input.Keys.TAB);
                buttons.add(Input.Keys.F);
                buttons.add(Input.Keys.P);
                buttons.add(Input.Keys.ESCAPE);
                buttons.add(Input.Keys.E);
                buttons.add(Input.Keys.R);
                buttons.add(Input.Buttons.LEFT);
                buttons.add(Input.Buttons.RIGHT);
            }
        }
    }

    public static void reloadButton(){
        buttons.set(0, Input.Keys.W);
        buttons.set(1, Input.Keys.S);
        buttons.set(2, Input.Keys.D);
        buttons.set(3, Input.Keys.A);
        buttons.set(4, Input.Keys.SPACE);

        buttons.set(5, Input.Keys.TAB);
        buttons.set(6, Input.Keys.F);
        buttons.set(7, Input.Keys.P);
        buttons.set(8, Input.Keys.ESCAPE);
        buttons.set(9, Input.Keys.E);
        buttons.set(10, Input.Keys.R);
        buttons.set(11, Input.Buttons.LEFT);
        buttons.set(12, Input.Buttons.RIGHT);
    }

    public static Texture loadTexture(String name){
        Texture texture = textures.get(name);
        if(texture == null){
            textures.put(name, new Texture(name));
            texture = textures.get(name);
        }

        return texture;
    }

    public static HashMap<String, TextureRegion> regions;
    public static TextureRegion loadRegion(String name, int x, int y, int xSize, int ySize){
        TextureRegion texture = regions.get(name);
        if(texture == null){
            regions.put(name, new TextureRegion(loadTexture(name), x, xSize, y, ySize));
            texture = regions.get(name);
            texture.setRegion(x, y, xSize, ySize);
        }

        return texture;
    }

    public static TextureRegion loadRegion(String name){
        TextureRegion texture = regions.get(name);
        if(texture == null){
            regions.put(name, new TextureRegion(loadTexture(name)));
            texture = regions.get(name);
        }

        return texture;
    }

    public static boolean isKeyDown(int index){
        if(buttons.get(index) == Input.Buttons.LEFT){
            return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        }
        else if(buttons.get(index) == Input.Buttons.RIGHT){
            return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        }
        else{
            return Gdx.input.isKeyPressed(GameScreen.buttons.get(index));
        }
    }

    public static int getMouseX(){
        return (int)(((float)(Gdx.input.getX(0) - stage.getViewport().getScreenX()) / (Gdx.graphics.getWidth() - stage.getViewport().getScreenX() * 2)) * stage.getViewport().getWorldWidth());
    }

    public static int getMouseX(int index){
        return (int)(((float)(Gdx.input.getX(index) - stage.getViewport().getScreenX()) / (Gdx.graphics.getWidth() - stage.getViewport().getScreenX() * 2)) * stage.getViewport().getWorldWidth());
    }

    public static int getMouseY(){
        return (int)((1 - ((float)(Gdx.input.getY(0) - stage.getViewport().getScreenY()) / (Gdx.graphics.getHeight() - stage.getViewport().getScreenY() * 2))) * stage.getViewport().getWorldHeight());
    }

    public static int getMouseY(int index){
        return (int)((1 - ((float)(Gdx.input.getY(index) - stage.getViewport().getScreenY()) / (Gdx.graphics.getHeight() - stage.getViewport().getScreenY() * 2))) * stage.getViewport().getWorldHeight());
    }

    public static int getMouseX(Stage stage){
        return (int)(((float)(Gdx.input.getX(0) - stage.getViewport().getScreenX()) / (Gdx.graphics.getWidth() - stage.getViewport().getScreenX() * 2)) * stage.getViewport().getWorldWidth());
    }

    public static int getMouseY(Stage stage){
        return (int)((1 - ((float)(Gdx.input.getY(0) - stage.getViewport().getScreenY()) / (Gdx.graphics.getHeight() - stage.getViewport().getScreenY() * 2))) * stage.getViewport().getWorldHeight());
    }

    private static final int textTics = 10000;
    private static long lastTimeSetText = 0;
    private static int butIndex;
    public static void setHelpText(String text, int buttonIndex){
        helpLabel.setColor(1, 1, 1, 1);
        butIndex = buttonIndex;
        helpLabel.setVisible(true);
        helpLabel.setText(text);

        lastTimeSetText = TimeUtils.millis();
    }

    public static boolean isHelpLabelVisible(){
        return helpLabel.isVisible();
    }

    public static int getButIndex() {
        return butIndex;
    }

    public static final ArrayList<Slot> inventorySlots = new ArrayList<>();
    public static final ArrayList<Slot> creatureSlots = new ArrayList<>();
    private void generateInventorySlots(float startX, float startY){
        for(int i = 0; i < 6; i++){
            Slot slot = new Slot(player);
            slot.setIndex(i - 6);
            if(i == 5){
                slot.setX(startX + 120);
                slot.setY(startY + 190);
            }
            else if(i == 0){
                slot.setX(startX - 20);
                slot.setY(startY + 190);
            }
            else{
                slot.setX(startX + 50);
                slot.setY(startY + 50 + i * 70);
            }
            slot.setVisible(false);

            inventorySlots.add(slot);

            stage.addActor(slot);
        }
    }

    private void addInventorySlots(){
        for(Slot slot : inventorySlots){
            stage.addActor(slot);
        }

        for(Slot slot : creatureSlots){
            stage.addActor(slot);
        }
    }

    public static void updateSlotPos(float startX, float startY, boolean left) {
        for (int i = 0; i < inventorySlots.size(); i++) {
            Slot slot = inventorySlots.get(i);
            if (i > 0 && i < 5) {
                slot.setX(startX + 50);
                slot.setY(startY + 50 + (i - 1) * 70);
            } else if (i == 5) {
                slot.setX(startX + 120);
                slot.setY(startY + 190);
            } else if (i == 0) {
                slot.setX(startX - 20);
                slot.setY(startY + 190);
            } else {
                slot.setX(startX + 190 + 70 * ((i - 6) % 7));
                slot.setY(startY + 260 - 70 * ((i - 6) / 7));
            }
        }

        inventorySizeLabel.setX(startX + 300);
        inventorySizeLabel.setY(startY + 350);

        moneyLabel.setX(startX + 400);
        moneyLabel.setY(startY + 350);

        startX -= 500;
        for (int i = 0; i < creatureSlots.size(); i++) {
            Slot slot = creatureSlots.get(i);
            slot.setX(startX + 190 + 70 * (i % (left ? 2 : 7)));
            slot.setY(startY + 260 - 70 * (i / (left ? 2 : 7)));
        }
    }

    public static void updateReaderPos(float startX, float startY){
        dialogReader.setX(startX + 300);
        dialogReader.setY(startY - 150);
    }

    public static void setVisible(boolean visible){
        for (Slot inventorySlot : inventorySlots) {
            inventorySlot.setVisible(visible);
        }

        inventorySizeLabel.setVisible(visible);
        moneyLabel.setVisible(visible);

        for (Slot creatureSlot : creatureSlots) {
            creatureSlot.setVisible(visible);
        }
    }

    private static final Label inventorySizeLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

    public static void setVisible(boolean visible, int inventorySize, float inventorySize2){
        for(int i = 0; i < inventorySlots.size(); i++){
            if(i < inventorySize + 7) {
                inventorySlots.get(i).setVisible(visible);
            }
            else{
                inventorySlots.get(i).setVisible(false);
            }
        }

        inventorySizeLabel.setVisible(visible);
        moneyLabel.setVisible(visible);

        for(int i = 0; i < creatureSlots.size(); i++){
            if(i < inventorySize2 + 1) {
                creatureSlots.get(i).setVisible(visible);
            }
            else{
                creatureSlots.get(i).setVisible(false);
            }
        }
    }

    public static void setVisible(boolean visible, int inventorySize, int inventorySize2){
        for(int i = 0; i < inventorySlots.size(); i++){
            if(i >= 6 && i < inventorySize + 7) {
                inventorySlots.get(i).setVisible(visible);
            }
            else{
                inventorySlots.get(i).setVisible(false);
            }
        }

        inventorySizeLabel.setVisible(visible);
        moneyLabel.setVisible(visible);

        for(int i = 0; i < creatureSlots.size(); i++){
            if(i < inventorySize2 + 1) {
                creatureSlots.get(i).setVisible(visible);
            }
            else{
                creatureSlots.get(i).setVisible(false);
            }
        }
    }

    public static void setItems(Creature cr, Workbench cr2){
        Inventory inv = cr.getInventory();
        Inventory inv2 = cr2.getInventory();
        for(int i = 0; i < inv.getAllItems().size() + 6; i++){
            if(inv.getItem(i - 6) != null) {
                if (inventorySlots.size() > i)
                    inventorySlots.get(i).setItem(inv.getItem(i - 6));
                else {
                    Slot slot = new Slot(cr);

                    slot.setIndex(i - 6);
                    slot.setItem(inv.getItem(i - 6));
                    slot.setX(player.getCentralX() + 120 + 70 * (i - 5));
                    slot.setY(player.getY() + 260);

                    inventorySlots.add(slot);

                    stage.addActor(slot);

                    sortCells();
                }
            }
            else{
                inventorySlots.get(i).setItem(null);
            }
        }

        if(inventorySlots.size() < inv.getAllItems().size() + 7) {
            Slot slot = new Slot(cr);

            slot.setIndex(inv.getAllItems().size());

            inventorySlots.add(slot);

            stage.addActor(slot);

            sortCells();
        }
        else{
            inventorySlots.get(inv.getAllItems().size() + 6).setItem(null);
            inventorySlots.get(inv.getAllItems().size() + 6).setInv(cr);
        }

        inventorySizeLabel.setText(inv.getItemsSize() + "/" + inv.getInventorySize());
        moneyLabel.setText("Money: " + player.getMoney());

        for(int i = 0; i < inv2.getAllItems().size(); i++){
            if(inv2.getItem(i) != null) {
                if (creatureSlots.size() > i)
                    creatureSlots.get(i).setItem(inv2.getItem(i));
                else {
                    Slot slot = new Slot(cr2);

                    slot.setIndex(i);
                    slot.setItem(inv2.getItem(i));
                    slot.setX(player.getCentralX() - 500 + 120 + 70 * i);
                    slot.setY(player.getY() + 260);

                    creatureSlots.add(slot);

                    stage.addActor(slot);

                    sortCells();
                }
            }
            else{
                creatureSlots.get(i).setItem(null);
            }

            creatureSlots.get(i).setInv(cr2);
        }

        if(creatureSlots.size() < inv2.getAllItems().size() + 1) {
            Slot slot = new Slot(cr2);

            slot.setIndex(inv2.getAllItems().size());

            creatureSlots.add(slot);

            stage.addActor(slot);

            sortCells();
        }
        else{
            creatureSlots.get(inv2.getAllItems().size()).setItem(null);
            creatureSlots.get(inv2.getAllItems().size()).setInv(cr2);
        }
    }

    private static boolean mouseClicked = true;
    public static void setMagicType(){
        magicLabel.setVisible(true);
        magicLabel.setPosition(player.getCentralX() - 100, player.getCentralY() + 275);
        magicLabel.setText("Magic " + (player.isMaterialMagicType() == 0 ? "" : (player.isMaterialMagicType() == 1 ? "semi " : "not ")) + "material");

        float x = getMouseX();
        float y = getMouseY();
        float magicLabelX = magicLabel.getX() - player.getCentralX() + 600;
        float magicLabelY = magicLabel.getY() - player.getCentralY() + 350;
        if(x >= magicLabelX && x <= magicLabelX + magicLabel.getPrefWidth() * 2 &&
            y >= magicLabelY && y <= magicLabelY + magicLabel.getPrefHeight() * 2 && !mouseClicked &&
        Gdx.input.isTouched()){
            player.setMaterialMagicType((player.isMaterialMagicType() + 1) % 3);
            mouseClicked = true;
        }
        else if(!Gdx.input.isTouched()){
            mouseClicked = false;
        }

        if(GameScreen.player.isSeeInventory())
            magicLabel.setVisible(false);
    }

    public static void setItems(Creature cr, WithInventory cr2){
        Inventory inv = cr.getInventory();
        Inventory inv2 = cr2.getInventory();

        for(int i = 6; i < inv.getAllItems().size() + 6; i++){
            if(inv.getItem(i - 6) != null) {
                if (inventorySlots.size() > i)
                    inventorySlots.get(i).setItem(inv.getItem(i - 6));
                else {
                    Slot slot = new Slot(cr);

                    slot.setIndex(i - 6);
                    slot.setItem(inv.getItem(i - 6));
                    slot.setX(player.getCentralX() + 120 + 70 * (i - 5));
                    slot.setY(player.getY() + 260);

                    inventorySlots.add(slot);

                    stage.addActor(slot);

                    sortCells();
                }
            }
            else{
                inventorySlots.get(i).setItem(null);
            }
        }

        if(inventorySlots.size() < inv.getAllItems().size() + 7) {
            Slot slot = new Slot(cr);

            slot.setIndex(inv.getAllItems().size());

            inventorySlots.add(slot);

            stage.addActor(slot);

            sortCells();
        }
        else{
            inventorySlots.get(inv.getAllItems().size() + 6).setItem(null);
            inventorySlots.get(inv.getAllItems().size() + 6).setInv(cr);
        }

        inventorySizeLabel.setText(inv.getItemsSize() + "/" + inv.getInventorySize());
        moneyLabel.setText("Money: " + player.getMoney());

        for(int i = 0; i < inv2.getAllItems().size(); i++){
            if(inv2.getItem(i) != null) {
                if (creatureSlots.size() > i)
                    creatureSlots.get(i).setItem(inv2.getItem(i));
                else {
                    Slot slot = new Slot(cr2);

                    slot.setIndex(i);
                    slot.setItem(inv2.getItem(i));
                    slot.setX(player.getCentralX() - 500 + 120 + 70 * i);
                    slot.setY(player.getY() + 260);

                    creatureSlots.add(slot);

                    stage.addActor(slot);

                    sortCells();
                }
            }
            else{
                creatureSlots.get(i).setItem(null);
            }

            creatureSlots.get(i).setInv(cr2);
        }

        if(creatureSlots.size() < inv2.getAllItems().size() + 1) {
            Slot slot = new Slot(cr2);

            slot.setIndex(inv2.getAllItems().size());

            creatureSlots.add(slot);

            stage.addActor(slot);

            sortCells();
        }
        else{
            creatureSlots.get(inv2.getAllItems().size()).setItem(null);
            creatureSlots.get(inv2.getAllItems().size()).setInv(cr2);
        }
    }

    public static final ArrayList<Mech> mechs = new ArrayList<>();

    private boolean hasBuildingObject = false;
    public void createSavedMap(MainSavedObj savedGame){
        darkCof = savedGame.getDarkCof();
        Bug.colonyThinking = savedGame.getColonyThinking();
        Obstacle building = null;

        for(SavedCell savedCell : savedGame.getCells()){
            Cell cell = new Cell(savedCell.getCellType());
            cell.setTemperature(savedCell.getTemperature());
            cell.setBiomeVal(savedCell.getBiomeVal());
            cell.setCreateObjects(false);
            cell.setWaterLevel(savedCell.getWaterLevel());

            cell.setImage();

            cell.setPosition(savedCell.getX(), savedCell.getY());
            if(cell.getCellType() == 0){
                cell.checkDepth();
            }

            stage.addActor(cell);
        }

        for(SavedCreature obj : savedGame.getCreatures()){
            ObsWithInventory obs = null;
            Creature creature = null;

            Item[] items = new Item[obj.getInv().length];
            for(int i = 0; i < obj.getInv().length; i++){
                if(obj.getInv()[i] != null) {
                    items[i] = new Item(obj.getInv()[i].getItemIndex());
                    items[i].setEndurance(obj.getInv()[i].getEndurance());
                }
            }

            ArrayList<Item> inv = new ArrayList<>();
            for(SavedItem item : obj.getMainItems()){
                Item item1 = new Item(item.getItemIndex());
                item1.setEndurance(item.getEndurance());
                inv.add(item1);
            }

            String[] str;
            switch (obj.getObjectType()){
                case 1:
                    str = obj.getPicName().split(", ");
                    creature = new Rabbit(str[0].equals("true"));
                    ((Rabbit) creature).setFollow(str[1].equals("true"));
                    break;
                case 2:
                    creature = new Skeleton();
                    break;
                case 3:
                    creature = new SkeletonMage();
                    break;
                case 4:
                    creature = new Wolf();
                    ((Wolf) creature).setTamed(obj.getPicName() == null || obj.getPicName().equals("true"));
                    break;
                case 5:
                    creature = new Bug(obj.getPicName().equals("true"));
                    if(obj.getPicName().equals("true")){
                        hasQueen = true;
                    }
                    break;
                case 6:
                    obs = new Chest();
                    break;
                case 7:
                    obs = new Oven(Integer.parseInt(obj.getPicName()));
                    break;
                case 8:
                    obs = new Workbench();
                    break;
                case 9:
                    str = obj.getPicName().split(", ");
                    creature = new Villager(items[0] != null && items[0].getIndex() == 1, items[1]!= null && items[1].getIndex() == 65, str[0]);
                    if(items[0] != null && items[0].getIndex() == 1){
                        hasKnight = true;
                    }
                    ((Villager) creature).setFollow(str[1].equals("true"));
                    ((Villager) creature).setPhraseNum(Integer.parseInt(str[2]));
                    if(((Villager) creature).isKnight() && ((Villager) creature).phraseNum >= 4){
                        ((Villager) creature).addMage = false;
                    }
                    break;
                case 10:
                    creature = new Player();
                    player = (Player) creature;
                    break;
                case 11:
                    hasBuildingObject = true;
                    creature = new BuildingObject();
                    buildingObject = (BuildingObject) creature;
                    ((BuildingObject) creature).setMaterials(savedGame.getMaterials());
                    break;
                case 12:
                    creature = new Fish();
                    break;
                case 13:
                    creature = new Mage();
                    break;
            }
            if(creature != null) {
                creature.setPosition(obj.getX(), obj.getY());
                creature.setZ(obj.getZ());

                creature.setHp(obj.getHp());

                creature.getInventory().setMainSlots(items);
                creature.getInventory().setItems(inv);

                stage.addActor(creature);

                if(creature instanceof BuildingObject) {
                    BuildingObject.buildings.get(savedGame.getBuildingType() - 1).setStruct();
                    ((BuildingObject) creature).setStatic(true);
                }
            }
            else if(obs != null){
                obs.setPosition(obj.getX(), obj.getY());
                obs.setZ(obj.getZ());

                obs.getInventory().setMainSlots(items);
                obs.getInventory().setItems(inv);

                stage.addActor(obs);

                if(obj.isBuilding())
                    building = obs;
            }
        }

        for(SavedObs obj : savedGame.getObstacles()){
            Obstacle obs = null;
            switch (obj.getObsType()) {
                case 0:
                    obs = new Obstacle(obj.getPicName(), new TextureRegion(GameScreen.loadTexture(obj.getPicName())),
                            new Rectangle3D(new Rectangle(0, 0, obj.getWidth(), obj.getHeight()), 0, obj.getzSize()), obj.getMaterials());
                case 1:
                    if(obj.getSize() > 0)
                        obs = new CaveWall(obj.getSize());
                    else
                        obs = new CaveWall(obj.getWidth(), obj.getHeight(), obj.getzSize());
                    break;
                case 2:
                    obs = new Tree(obj.getType());
                    break;
                case 3:
                    obs = new Wall(obj.getWidth(), obj.getHeight(), obj.getzSize());
                    break;
                case 4:
                    obs = new Interior();
                    break;
                case 9:
                    obs = new Algae();
                    break;
                case 10:
                    obs = new AlchemyTable();
                    break;
                case 11:
                    obs = new Block();
                    obs.getRect().setWidth(obj.getWidth());
                    obs.getRect().setHeight(obj.getHeight());
                    obs.getRect().setzSize(obj.getzSize());
                    ((Block) obs).calcMat();
                    break;
            }

            if(obs != null) {
                obs.setMaterials(obj.getMaterials());
                if(obj.getObsType() == 0)
                    obs.setRectDelta(obj.getRectDelta());
                obs.setPosition(obj.getX(), obj.getY());
                obs.setZ(obj.getZ());

                stage.addActor(obs);

                if(obj.isBuilding())
                    building = obs;
            }
        }

        for(SavedFallenItem fallenItem: savedGame.getFallenItems()){
            Item item = new Item(fallenItem.getSavedItem().getItemIndex());
            item.setEndurance(fallenItem.getSavedItem().getEndurance());
            FallenItem fallenItem1 = new FallenItem(item);
            fallenItem1.setPosition(fallenItem.getX(), fallenItem.getY());
            fallenItem1.setZ(fallenItem.getZ());

            stage.addActor(fallenItem1);
        }

        player.setMoney(savedGame.getMoney());
        player.setSatiety(savedGame.getPlayerSatiety());
        player.setWater(savedGame.getPlayerWater());
        player.setMagicType(savedGame.getMagicType());
        player.setMagDamage(savedGame.getMagDamage());
        player.setMaxMagicPower(savedGame.getPlMaxMagicPower());
        player.setMagicPower(savedGame.getPlMagicPower());

        NumberButton.magicPar = savedGame.getMagicPar();
        for(Integer i : savedGame.getEffects())
            player.getDealtEffects().add(new DealtEffect(0, i));
        player.setAutoCast(savedGame.isAutoRot());
        player.setWithFreezing(savedGame.isFreeze());

        GameScreen.player.start = 4;

        buildingObject.setBuilding(building);
        if(building instanceof Block){
            buildingObject.nButtons.get(0).setValue(building.getRect().getWidth());
            buildingObject.nButtons.get(1).setValue(building.getRect().getHeight());
            buildingObject.nButtons.get(2).setValue(building.getRect().getzSize());
        }
        building.getRect().setMaterial(false);
    }

    public static final int mapSize = 70;
    public static final int squareSize = 32;
    public static final int caveDist = 160;
    public static final int caveCordX = -20;
    public static final int caveCordY = 90;
    public static final int nestX = 100;
    public static final int nestY = -30;
    public static final int waterX = 120;
    public static final int waterY = 120;
    public static BuildingObject buildingObject;
    public static boolean hasQueen;
    private void generateMap(){
        player = new Player();
        map = new int[mapSize][mapSize];
        int villageX = 35;
        int villageY = 35;
        int houseNum = 5 + (int)(Math.random() * 5);

        for(int i = 0; i < map.length; i ++)
            for(int j = 0; j < map.length; j ++){
                map[i][j] = Math.random() * 100 < 80 ? 0 : -1;
                map[i][j] = Math.random() * 100 < 99.5 ? map[i][j] : -2;
                map[i][j] = Math.random() * 100 < 99.8 ? map[i][j] : -3;
                map[i][j] = Math.random() * 100 < 99.9 ? map[i][j] : -4;
            }

        for(int i = 0; i < houseNum; i++){
            int houseX = villageX + (int)(Math.cos(Math.toRadians(i * 360 / houseNum)) * (6 + (Math.random() * 10)));
            int houseY = villageY + (int)(Math.sin(Math.toRadians(i * 360 / houseNum)) * (6 + (Math.random() * 10)));
            for(int i1 = houseX; i1 > houseX - 3; i1--)
                for(int j1 = houseY; j1 > houseY - 2; j1--)
                    map[i1][j1] = 2;
            map[houseX][houseY] = 3;
            map[houseX - 1][houseY - 1] = i == 0 ? 4 : 1;
        }

        for(int i = 0; i < map.length; i ++)
            for(int j = 0; j < map.length; j ++) {
                if(map[i][j] == 1 || map[i][j] == 2 || map[i][j] == 3 || map[i][j] == 4) {
                    Cell background = new Cell(4);
                    background.setX(i * squareSize);
                    background.setY(j * squareSize);
                    stage.addActor(background);
                }
                else if(map[i][j] == -2){
                    Cell background = new Cell(3);
                    background.setX(i * squareSize);
                    background.setY(j * squareSize);
                    stage.addActor(background);
                }
                else{
                    Cell background = new Cell(1);
                    background.setX(i * squareSize);
                    background.setY(j * squareSize);
                    stage.addActor(background);
                }
            }

        boolean hasOven = false;
        for(int j = map.length - 1; j >= 0; j --)
            for(int i = 0; i < map[j].length; i ++){
                if(map[i][j] == 1){
                    Villager villager = new Villager(false);
                    villager.setX(i * squareSize - 16);
                    villager.setY(j * squareSize + 10);
                    stage.addActor(villager);
                }
                else if(map[i][j] == 4){
                    player.setX(i * squareSize - 16);
                    player.setY(j * squareSize + 10);
                }
                else if(map[i][j] == 3){
                    Interior bed = new Interior();
                    bed.setX(i * squareSize);
                    bed.setY(j * squareSize - 30);
                    stage.addActor(bed);

                    Wall wall = new Wall(5, 64, 60);
                    wall.setX(i * squareSize + 32);
                    wall.setY(j * squareSize - 32 + 1);
                    stage.addActor(wall);

                    Wall wall1 = new Wall(96, 5, 60);
                    wall1.setX(i * squareSize - 64);
                    wall1.setY(j * squareSize + 30);
                    stage.addActor(wall1);

                    Wall wall2 = new Wall(5, 64, 60);
                    wall2.setX(i * squareSize - 69);
                    wall2.setY(j * squareSize - 32 + 1);
                    stage.addActor(wall2);

                    Wall wall3 = new Wall(37, 5, 60);
                    wall3.setX(i * squareSize - 69);
                    wall3.setY(j * squareSize - 32);
                    stage.addActor(wall3);

                    Wall wall4 = new Wall(37, 5, 60);
                    wall4.setX(i * squareSize);
                    wall4.setY(j * squareSize - 32);
                    stage.addActor(wall4);

                    Wall wall5 = new Wall(32, 5, 60);
                    wall5.setX(i * squareSize - 32);
                    wall5.setY(j * squareSize - 32);
                    stage.addActor(wall5);

                    Wall roof = new Wall(96, 64, 30);
                    roof.setZ(60);
                    roof.setX(i * squareSize - 74);
                    roof.setY(j * squareSize - 32);
                    stage.addActor(roof);

                    Chest chest = new Chest();
                    chest.setX(i * squareSize - 32);
                    chest.setY(j * squareSize + 16);
                    stage.addActor(chest);

                    if(!hasOven){
                        Oven oven = new Oven(1);
                        oven.setX(i * squareSize - 64);
                        oven.setY(j * squareSize + 8);
                        stage.addActor(oven);

                        hasOven = true;
                    }
                    else if(Math.random() < 0.4){
                        Oven oven = new Oven(1);
                        oven.setX(i * squareSize - 64);
                        oven.setY(j * squareSize + 8);
                        stage.addActor(oven);
                    }
                    else{
                        Workbench workbench = new Workbench();
                        workbench.setX(i * squareSize - 64);
                        workbench.setY(j * squareSize + 8);
                        stage.addActor(workbench);
                    }
                }
            }

        createButtons();
    }

    public void createButtons(){
        if(MyGdxGame.phone) {
            Joystick joystick = new Joystick();
            stage.addActor(joystick);

            Backpack backpack = new Backpack();
            stage.addActor(backpack);

            AttackButton attackButton = new AttackButton();
            stage.addActor(attackButton);

            JumpButton jumpButton = new JumpButton();
            stage.addActor(jumpButton);

            HandButton handButton = new HandButton();
            stage.addActor(handButton);

            MagicButton magicButton = new MagicButton();
            stage.addActor(magicButton);

            PortalButton portalButton = new PortalButton();
            stage.addActor(portalButton);

            ShieldButton shieldButton = new ShieldButton();
            stage.addActor(shieldButton);
        }

        if(!hasBuildingObject) {
            buildingObject = new BuildingObject();
            stage.addActor(buildingObject);
        }

        for (BuildingSlot building : BuildingObject.buildings) {
            stage.addActor(building);
        }

        for (NumberButton1 b : buildingObject.nButtons) {
            stage.addActor(b);
        }

        BuildingButton buildingButton = new BuildingButton();
        stage.addActor(buildingButton);

        MagicRefactorButton magicRefactorButton = new MagicRefactorButton();
        stage.addActor(magicRefactorButton);

        for(int i = 0; i < 4; i++) {
            MagicEffectButton magicEffectButton = new MagicEffectButton(i, -350 + ((i % 2) * 120), 150 - (i / 2) * 120);

            if(i == 0 && player.isWithFreezing())
                magicEffectButton.setSelected();
            else
                for(DealtEffect i1 : GameScreen.player.getDealtEffects())
                    if(i == i1.getType())
                        magicEffectButton.setSelected();


            stage.addActor(magicEffectButton);
        }

        for(int i = -1; i >= -6; i--) {
            MagicEffectButton magicEffectButton = new MagicEffectButton(i, 300 - (((i + 1) % 2) * 120), 150 + ((i + 1) / 2) * 120);

            if(i == -6 && player.isAutoCast())
                magicEffectButton.setSelected();
            else
                for(DealtEffect i1 : GameScreen.player.getDealtEffects())
                    if(i == i1.getType())
                        magicEffectButton.setSelected();

            stage.addActor(magicEffectButton);
        }

        for(int i = 0; i < 7; i++) {
            String str = "Number of stacks: ";
            switch(i){
                case 1:
                    str = "Damage: ";
                    break;
                case 2:
                    str = "Tics: ";
                    break;
                case 3:
                    str = "Diameter: ";
                    break;
                case 4:
                    str = "Speed: ";
                    break;
                case 5:
                    str = "Number of spells: ";
                    break;
                case 6:
                    str = "Angle: ";
                    break;
            }
            NumberButton nb = new NumberButton(i, str, 150, 240 - i * 60);
            stage.addActor(nb);
        }

        SaveButton saveButton = new SaveButton();
        stage.addActor(saveButton);
    }

    public static boolean touchButton(){
        for(int i = 0; i < endEntities; i++){
            Actor act = stage.getActors().get(stage.getActors().size - 1 - i);
            if(act instanceof ButtonInterface && ((ButtonInterface) act).touchThis()){
                return true;
            }
        }

        return false;
    }
    public static int pressedKey;

    @Override
    public void show() {

    }

    public static float darkCof = 0;

    public static float getDarkCof() {
        return darkCof;
    }

    public static void setDarkCof(float darkCof1) {
        darkCof = darkCof1;
    }

    public boolean night = false;
    private long lastTimeSave;
    private final int saveTime = 300000;
    public void updateTime(){
        if(getDarkCof() >= 0.5){
            night = true;
        }
        else if(getDarkCof() <= 0){
            night = false;
        }
        if(!night){
            setDarkCof(darkCof + 0.00001f);
        }
        else{
            setDarkCof(darkCof - 0.00001f);
        }

        if(TimeUtils.timeSinceMillis(lastTimeSave) >= saveTime){
            sortCells();
            sortObstacles();

            save();

            lastTimeSave = TimeUtils.millis();
        }
    }

    private State state = State.RUN;
    private boolean pushP = false;
    @Override
    public void render(float v) {
        switch (state) {
            case RUN:
                Gdx.gl.glClearColor(0f, 0f, 0f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                Joystick.index = 0;
                stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

                render2_5();

                stage.draw();

                drawDesc();

                if(helpLabel.isVisible())
                    drawHelpText();

                if(Gdx.input.isKeyPressed(buttons.get(7)) && !pushP){
                    state = State.PAUSE;

                    pushP = true;
                }
                else if(!Gdx.input.isKeyPressed(buttons.get(7)) && pushP)
                    pushP = false;
                break;
            case PAUSE:
                Gdx.gl.glClearColor(0f, 0f, 0f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                stage.draw();

                if(Gdx.input.isKeyPressed(buttons.get(7)) && !pushP){
                    state = State.RUN;

                    pushP = true;
                }
                else if(!Gdx.input.isKeyPressed(buttons.get(7)) && pushP)
                    pushP = false;
                break;
        }
    }

    public void drawHelpText(){
        long time = TimeUtils.millis() - lastTimeSetText;

        helpLabel.setPosition(player.getCentralX() - helpLabel.getPrefWidth() / 2, player.getY() + player.getZ() + 300);

        if(time > textTics){
            helpLabel.setVisible(false);

            player.start++;
        }
        else if(time > textTics * 4f / 5){
            helpLabel.setColor(1, 1, 1, 1 - ((time - textTics * 4f / 5) / (textTics / 5)));
        }
    }

    private void drawDesc(){
        if(Slot.firstSlot != null){
            descLabel.toFront();
            descLabel.setVisible(true);
            descLabel.setText(Slot.firstSlot.getItem().getDesc());

            /*Pixmap labelColor = new Pixmap((int)descLabel.getWidth(), (int)descLabel.getHeight(), Pixmap.Format.RGB888);
            labelColor.setColor(new Color(1f, 1f, 1f, 0.5f));
            labelColor.fill();
            descLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();*/

            if(Slot.firstSlot.touchThis())
                descLabel.setPosition(GameScreen.player.getCentralX() - stage.getViewport().getWorldWidth() / 2 + getMouseX(), GameScreen.player.getCentralY() - stage.getViewport().getWorldHeight() / 2 + getMouseY() - descLabel.getHeight());
        }
        else{
            descLabel.setVisible(false);
        }
    }
    public static int endEntities = 0;
    private static boolean needRemoveCells = true;
    public static boolean needObsSort = false;
    public static boolean needSave = false;
    public void render2_5(){
        if(needObsSort){
            sortCells();

            sortObstacles();

            needObsSort = false;
        }

        /*for(int i = 0; i < endEntities; i++){
            System.out.println(stage.getActors().get(stage.getActors().size - i - 1));
        }*/

        if(needRemoveCells){
            removeCells();

            needRemoveCells = false;
        }

        if(needSave){
            sortCells();
            sortObstacles();

            save();

            needSave = false;

            game.setScreen(new Lobby(game));
        }

        updateTime();

        Projectile item;
        int len = activeProjectiles.size;
        for (int i = len; --i >= 0;) {
            item = activeProjectiles.get(i);
            if (!item.active) {
                activeProjectiles.removeIndex(i);
                projPool.free(item);
            }
        }

        stage.getViewport().getCamera().position.set(player.getCentralX(), player.getCentralY() + player.getZ(), 0);
    }

    public void save() {
        if(player.getHp() > 0) {
            try {
                MainSavedObj mainObj = new MainSavedObj();

                mainObj.setMagicPar(NumberButton.magicPar);
                ArrayList<Integer> effects = new ArrayList<>();
                for(DealtEffect dealtEffect : GameScreen.player.getDealtEffects())
                    effects.add(dealtEffect.getType());

                mainObj.setEffects(effects);
                mainObj.setFreeze(GameScreen.player.isWithFreezing());
                mainObj.setAutoRot(GameScreen.player.isAutoCast());

                mainObj.setMoney(player.getMoney());
                mainObj.setDarkCof(darkCof);
                mainObj.setColonyThinking(Bug.colonyThinking);
                mainObj.setPlayerSatiety(player.getSatiety());
                mainObj.setPlayerWater(player.getWater());
                mainObj.setMagicType(player.getMagicType());
                mainObj.setMagDamage(player.getMagDamage());
                mainObj.setMaterials(buildingObject.getMaterials());
                mainObj.setBuildingType(buildingObject.getBuildingType());
                mainObj.setPlMagicPower(player.getMagicPower());
                mainObj.setPlMaxMagicPower(player.getMaxMagicPower());

                ArrayList<SavedCell> cells = new ArrayList<>();
                ArrayList<SavedCreature> creatures = new ArrayList<>();
                ArrayList<SavedObs> obs = new ArrayList<>();
                ArrayList<SavedFallenItem> fallenItems = new ArrayList<>();

                if(buildingObject.isStatic() &&
                        buildingObject.getBuildingType() != 0)
                    creatures.add(getSave(buildingObject));

                for(int i1 = 0; i1 < GameScreen.cells.length; i1++) {
                    for(int j = 0; j < GameScreen.cells[i1].length; j++){
                        Cell cell = GameScreen.cells[i1][j];
                        if(cell != null) {
                            SavedCell cell1 = new SavedCell(cell.getX(), cell.getY(), cell.getBiomeVal(), cell.getCellType(), cell.getTemperature(), cell.getWaterLevel());
                            cells.add(cell1);
                            for (MyActor actor : cell.actors) {
                                if(actor != buildingObject)
                                    if (actor instanceof Creature) {
                                        creatures.add(getSave((Creature) actor));
                                    } else if (actor instanceof Obstacle) {
                                        Obstacle obstacle = (Obstacle) actor;
                                        ObsWithInventory obs2 = null;
                                        if (actor instanceof ObsWithInventory)
                                            obs2 = (ObsWithInventory) actor;

                                        int obsType = 0;
                                        if (actor instanceof CaveWall) {
                                            obsType = 1;
                                        } else if (actor instanceof Tree) {
                                            obsType = 2;
                                        } else if (actor instanceof Wall) {
                                            obsType = 3;
                                        } else if (actor instanceof Interior) {
                                            obsType = 4;
                                        } else if (actor instanceof Chest) {
                                            obsType = 6;
                                        } else if (actor instanceof Oven) {
                                            obsType = 7;
                                        } else if (actor instanceof AlchemyTable) {
                                            obsType = 10;
                                        } else if (actor instanceof Workbench) {
                                            obsType = 8;
                                        } else if(actor instanceof Algae){
                                            obsType = 9;
                                        } else if(actor instanceof Block){
                                            obsType = 11;
                                        }

                                        if (obs2 == null) {
                                            SavedObs obs1 = new SavedObs(obstacle.getMaterials(), obsType, obstacle.getX(), obstacle.getY(), obstacle.getZ()
                                                    , (int) obstacle.getRect().getWidth(), (int) obstacle.getRect().getHeight(), (int) obstacle.getRect().getzSize(),
                                                    obstacle == buildingObject.getBuilding());

                                            if (obsType == 2) {
                                                obs1.setType(((Tree) actor).getType());
                                            } else if (obsType == 1 && ((CaveWall) actor).getHeight1() > 0) {
                                                obs1.setSize(((CaveWall) actor).getHeight1());
                                            } else if (obsType == 0) {
                                                obs1.setPicName(obstacle.getPicName());
                                                obs1.setRectDelta(obstacle.getRectDelta());
                                            }

                                            obs.add(obs1);
                                        } else {
                                            SavedItem[] items = new SavedItem[obs2.getInventory().getItems().length];
                                            for (int i = 0; i < obs2.getInventory().getItems().length; i++) {
                                                if (obs2.getInventory().getItems()[i] != null) {
                                                    SavedItem savedItem = new SavedItem(obs2.getInventory().getItems()[i].getIndex(),
                                                            obs2.getInventory().getItems()[i].getEndurance());
                                                    items[i] = savedItem;
                                                }
                                            }

                                            ArrayList<SavedItem> inv = new ArrayList<>();
                                            for (Item item : obs2.getInventory().getAllItems()) {
                                                SavedItem savedItem = new SavedItem(item.getIndex(),
                                                        item.getEndurance());
                                                inv.add(savedItem);
                                            }

                                            SavedCreature savedCreature = new SavedCreature(obsType, 0, items,
                                                    inv, obs2.getX(), obs2.getY(), obs2.getZ(),
                                                    obs2 == buildingObject.getBuilding());
                                            if (obsType == 7) {
                                                savedCreature.setPicName("" + ((Oven) obs2).getType());
                                            }

                                            creatures.add(savedCreature);
                                        }
                                    } else if (actor instanceof FallenItem) {
                                        SavedItem savedItem = new SavedItem(((FallenItem) actor).getItem().getIndex(), ((FallenItem) actor).getItem().getEndurance());
                                        SavedFallenItem fallenItem = new SavedFallenItem(savedItem, actor.getX(), actor.getY(), actor.getZ());

                                        fallenItems.add(fallenItem);
                                    }
                            }
                        }
                    }
                }

                mainObj.setCells(cells);
                mainObj.setCreatures(creatures);
                mainObj.setObstacles(obs);
                mainObj.setFallenItems(fallenItems);

                FileOutputStream outputStream;
                if(MyGdxGame.phone && game.context != null){
                    outputStream = game.context.getOStream();
                }
                else{
                    outputStream = new FileOutputStream(String.valueOf(Gdx.files.local("save.ser")));
                }
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

                objectOutputStream.writeObject(mainObj);

                objectOutputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SavedCreature getSave(Creature creature){
        SavedItem[] items = new SavedItem[creature.getInventory().getItems().length];
        for (int i = 0; i < creature.getInventory().getItems().length; i++) {
            if (creature.getInventory().getItems()[i] != null) {
                SavedItem savedItem = new SavedItem(creature.getInventory().getItems()[i].getIndex(),
                        creature.getInventory().getItems()[i].getEndurance());
                items[i] = savedItem;
            }
        }

        ArrayList<SavedItem> inv = new ArrayList<>();
        for (Item item : creature.getInventory().getAllItems()) {
            SavedItem savedItem = new SavedItem(item.getIndex(),
                    item.getEndurance());
            inv.add(savedItem);
        }

        int creatureType = 1;
        if (creature instanceof Skeleton) {
            creatureType = 2;
        } else if (creature instanceof SkeletonMage) {
            creatureType = 3;
        } else if (creature instanceof Wolf) {
            creatureType = 4;
        } else if (creature instanceof Bug) {
            creatureType = 5;
        } else if (creature instanceof Villager) {
            creatureType = 9;
        } else if (creature instanceof Player) {
            creatureType = 10;
        } else if (creature instanceof BuildingObject) {
            creatureType = 11;
        } else if(creature instanceof Fish){
            creatureType = 12;
        } else if(creature instanceof Mage){
            creatureType = 13;
        }

        SavedCreature savedCreature = new SavedCreature(creatureType, creature.getHp(), items,
                inv, creature.getX(), creature.getY(), creature.getZ(), false);
        if (creatureType == 1) {
            savedCreature.setPicName(((Rabbit) creature).isShadow() + ", " +
                    ((Rabbit) creature).isFollow());
        } else if (creatureType == 4) {
            savedCreature.setPicName(String.valueOf(((Wolf) creature).isTamed()));
        } else if (creatureType == 5) {
            savedCreature.setPicName(String.valueOf(((Bug) creature).isQueen()));
        } else if (creatureType == 9) {
            String picName = ((Villager) creature).getPicName();
            if (((Villager) creature).isElf() && ((Villager) creature).isFollow()) {
                picName += ", true";
            }
            else{
                picName += ", false";
            }

            picName += ", " + ((Villager) creature).phraseNum;
            savedCreature.setPicName(picName);
        }

        return savedCreature;
    }

    public MainSavedObj getSave() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream;
        if(MyGdxGame.phone && game.context != null){
            fileInputStream = game.context.getIStream();
        }
        else{
            fileInputStream = new FileInputStream("save.ser");
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        MainSavedObj savedGame = (MainSavedObj) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();

        return savedGame;
    }

    public static void sortObstacles(){
        Array<Actor> actors = stage.getActors();
        /*boolean hasChange = true;
        int i1 = 0;
        while(hasChange) {
            i1++;
            hasChange = false;
            if(i1 % 2 == 1)
                for (int i = actors.size - endEntities - 2; i >= 1; i--) {
                    boolean hasChange1 = sortEntity(i);
                    if(hasChange1 && !hasChange)
                        hasChange = true;
                }
            else
                for (int i = 1; i < actors.size - endEntities - 1; i++) {
                    boolean hasChange1 = sortEntity(i);
                    if(hasChange1 && !hasChange)
                        hasChange = true;
                }
        }*/

        for (int i = actors.size - endEntities - 1; i >= 1; i--) {
            Actor act = actors.get(i);
            if(act instanceof MyActor)
                ((MyActor) act).sort();
            else if(act instanceof Cell)
                ((Cell) act).sort();

            if(actors.get(i) != act){
                i ++;
            }
        }
    }

    public static boolean sortEntity(int i){
        boolean hasChange = false;
        Array<Actor> actors = stage.getActors();

        if(actors.get(i) instanceof MyActor) {
            MyActor act1 = (MyActor) (actors.get(i));
            if (actors.get(i + 1) instanceof MyActor) {
                MyActor act2 = (MyActor) (actors.get(i + 1));
                if (act1.getY() + act1.getRect().getHeight() - act1.getZ() < act2.getY() + act2.getRect().getHeight() - act2.getZ()) {
                    int z = act1.getZIndex();
                    act1.setZIndex(act2.getZIndex());
                    act2.setZIndex(z);
                    hasChange = true;
                }
            }
            else {
                Actor act2 = actors.get(i + 1);
                if (act1.getY() + act1.getRect().getHeight() - act1.getZ() < act2.getY() + 32){
                    int z = act1.getZIndex();
                    act1.setZIndex(act2.getZIndex());
                    act2.setZIndex(z);
                    hasChange = true;
                }
            }

            if (actors.get(i - 1) instanceof MyActor) {
                MyActor act2 = (MyActor) (actors.get(i - 1));
                if (act1.getY() + act1.getRect().getHeight() - act1.getZ() > act2.getY() + act2.getRect().getHeight() - act2.getZ()) {
                    int z = act1.getZIndex();
                    act1.setZIndex(act2.getZIndex());
                    act2.setZIndex(z);
                    hasChange = true;
                }
            }
            else{
                Actor act2 = actors.get(i - 1);
                if(act1.getY() + act1.getRect().getHeight() - act1.getZ() > act2.getY() + 32) {
                    int z = act1.getZIndex();
                    act1.setZIndex(act2.getZIndex());
                    act2.setZIndex(z);
                    hasChange = true;
                }
            }
        }
        else if(actors.get(i) instanceof Cell){
            Actor act1 = actors.get(i);
            if (actors.get(i + 1) instanceof MyActor) {
                MyActor act2 = (MyActor) (actors.get(i + 1));
                if (act1.getY() + 32 < act2.getY() + act2.getRect().getHeight() - act2.getZ()) {
                    int z = act1.getZIndex();
                    act1.setZIndex(act2.getZIndex());
                    act2.setZIndex(z);
                    hasChange = true;
                }
            }
            else{
                Actor act2 = actors.get(i + 1);
                if(act1.getY() < act2.getY()) {
                    int z = act1.getZIndex();
                    act1.setZIndex(act2.getZIndex());
                    act2.setZIndex(z);
                    hasChange = true;
                }
            }

            if (actors.get(i - 1) instanceof MyActor) {
                MyActor act2 = (MyActor) (actors.get(i - 1));
                if (act1.getY() + 32 > act2.getY() + act2.getRect().getHeight() - act2.getZ()) {
                    int z = act1.getZIndex();
                    act1.setZIndex(act2.getZIndex());
                    act2.setZIndex(z);
                    hasChange = true;
                }
            }
            else {
                Actor act2 = actors.get(i - 1);
                if(act1.getY() > act2.getY()) {
                    int z = act1.getZIndex();
                    act1.setZIndex(act2.getZIndex());
                    act2.setZIndex(z);
                    hasChange = true;
                }
            }
        }

        return hasChange;
    }

    public static void sortCreatures(){
        Array<Actor> actors = stage.getActors();
        for (int i = 0; i < actors.size - endEntities; i++) {
            if (actors.get(i) instanceof MovableObject && onScreen(actors.get(i))) {
                MyActor act1 = (MyActor) (actors.get(i));

                boolean hasReplace = false;
                for(int j = i - 1; j >= 0; j--) {
                    if (actors.get(j) instanceof MyActor) {
                        MyActor act2 = (MyActor) (actors.get(j));
                        if (act1.getY() + act1.getRect().getHeight() / 2 - act1.getZ() <= act2.getY() + act2.getRect().getHeight() / 2 - act2.getZ()) {
                            break;
                        }
                        else {
                            hasReplace = true;
                            int z = act1.getZIndex();
                            act1.setZIndex(act2.getZIndex());
                            act2.setZIndex(z);
                        }
                    }
                }

                if(!hasReplace){
                    for(int j = i + 1; j < actors.size - endEntities; j++) {
                        if (actors.get(j) instanceof MyActor) {
                            MyActor act2 = (MyActor) (actors.get(j));
                            if (act1.getY() + act1.getRect().getHeight() / 2 - act1.getZ() >= act2.getY() + act2.getRect().getHeight() / 2 - act2.getZ()) {
                                break;
                            }
                            else {
                                int z = act1.getZIndex();
                                act1.setZIndex(act2.getZIndex());
                                act2.setZIndex(z);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void removeCells(){
        if(oppositeCornerCell == null) {
            for (int i = 0; i < stage.getActors().size - endEntities; i++) {
                if (stage.getActors().get(i) instanceof Cell && !onScreen((Cell) stage.getActors().get(i))) {
                    ((Cell) stage.getActors().get(i)).remove1();
                    //System.out.println("removed " + stage.getActors().get(i).getX() + " " + stage.getActors().get(i).getY());
                    i--;
                }
            }
        }
        else{
            while(oppositeCornerCell != null && oppositeCornerCell.getStage() != null){
                oppositeCornerCell.remove1();

                oppositeCornerCell = getCellAt(oppositeCornerCell, rot2);
            }
        }
    }


    public static Cell oppositeCornerCell = null;
    public static int rot2;
    public static void generateCells(int rot, int dist){
        /*if(dist > 1){
            dist ++;
        }
        if(dist < 10) {
            boolean cellAdded = false;
            for (int k = 0; k < dist; k++) {
                int widthHalf;
                int heightHalf;
                if (rot == 0 || rot == 2) {
                    widthHalf = (int) (stage.getViewport().getWorldWidth() / 64) + 1;
                    heightHalf = (int) (stage.getViewport().getWorldHeight() / 64) + dist + 1;
                } else {
                    widthHalf = (int) (stage.getViewport().getWorldWidth() / 64) + dist + 1;
                    heightHalf = (int) (stage.getViewport().getWorldHeight() / 64) + 2;
                }
                int i1;
                int j1;
                if (rot == 0 || rot == 2) {
                    i1 = startCord + (rot == 0 ? k + 1 : -k - 1);
                    j1 = (int) ((player.getRectCenterY() + player.getZ()) / 32);
                } else {
                    i1 = (int) (player.getCentralX() / 32);
                    j1 = startCord + (rot == 1 ? k + 1 : -k - 1);
                }

                switch (rot) {
                    case 0:
                        for (int j = j1 - heightHalf; j < j1 + heightHalf; j++) {
                            if (!hasCell((i1 + widthHalf) * 32 + 16, j * 32 + 16)) {
                                Cell background = new Cell(1);
                                background.setX((i1 + widthHalf) * squareSize);
                                background.setY(j * squareSize);
                                stage.addActor(background);
                                cellAdded = true;
                            }
                        }
                        break;
                    case 2:
                        for (int j = j1 - heightHalf; j < j1 + heightHalf; j++) {
                            if (!hasCell((i1 - widthHalf) * 32 + 16, j * 32 + 16)) {
                                Cell background = new Cell(1);
                                background.setX((i1 - widthHalf) * squareSize);
                                background.setY(j * squareSize);
                                stage.addActor(background);
                                cellAdded = true;
                            }
                        }
                        break;
                    case 3:
                        for (int i = i1 - widthHalf; i < i1 + widthHalf; i++) {
                            if (!hasCell(i * 32 + 16, (j1 - heightHalf) * 32 + 16)) {
                                Cell background = new Cell(1);
                                background.setX(i * squareSize);
                                background.setY((j1 - heightHalf) * squareSize);
                                stage.addActor(background);
                                cellAdded = true;
                            }
                        }
                        break;
                    case 1:
                        for (int i = i1 - widthHalf; i < i1 + widthHalf; i++) {
                            if (!hasCell(i * 32 + 16, (j1 + heightHalf) * 32 + 16)) {
                                Cell background = new Cell(1);
                                background.setX(i * squareSize);
                                background.setY((j1 + heightHalf) * squareSize);
                                stage.addActor(background);
                                cellAdded = true;
                            }
                        }
                        break;
                }
            }

            if(cellAdded)
                sortCells();
        }*/
        try {
            if (0 < dist) {
                Cell oppositeCornerCell;
                rot2 = (rot + 1) % 4;
                //System.out.println("startt");
                for (int i1 = 0; i1 < dist; i1++) {
                    int rot1 = (rot + 3) % 4;
                    int rot3 = (rot + 2) % 4;
                    Cell cornerCell = cells[(int) (player.getCentralX() / 32) + 122]
                            [(int) ((player.getRectCenterY() + player.getZ()) / 32) + (cells[0].length - 70) / 2];
                    oppositeCornerCell = cornerCell;
                    Cell cell1 = getCellAt(cornerCell, rot);
                    while (cell1 != null && cell1.getStage() != null) {
                        cornerCell = cell1;
                        cell1 = getCellAt(cornerCell, rot);
                    }

                    cell1 = getCellAt(cornerCell, rot2);
                    while (cell1 != null && cell1.getStage() != null) {
                        cornerCell = cell1;
                        cell1 = getCellAt(cornerCell, rot2);
                    }

                    cell1 = getCellAt(oppositeCornerCell, rot3);
                    while (cell1 != null && cell1.getStage() != null) {
                        oppositeCornerCell = cell1;
                        cell1 = getCellAt(oppositeCornerCell, rot3);
                    }

                    cell1 = getCellAt(oppositeCornerCell, rot1);
                    while (cell1 != null && cell1.getStage() != null) {
                        oppositeCornerCell = cell1;
                        cell1 = getCellAt(oppositeCornerCell, rot1);
                    }
                /*for (int i = 0; i < stage.getActors().size; i++) {
                    if(stage.getActors().get(i) instanceof Cell) {
                        Cell cell = (Cell) stage.getActors().get(i);
                        if ((getCellAt(cell, rot) == null || getCellAt(cell, rot).getStage() == null) &&
                                (getCellAt(cell, rot2) == null || getCellAt(cell, rot2).getStage() == null)) {
                            cornerCell = cell;
                            break;
                        }
                    }
                }*/

                /*System.out.println(cornerCell);
                if(cornerCell != null && cornerCell.cells[rot2] != null)
                    System.out.println("start " + (cornerCell.cells[rot] != null ? cornerCell.cells[rot].getStage() : "1") + " " + (cornerCell.cells[rot2] != null ? cornerCell.cells[rot2].getStage() : "2") + " " +  cornerCell.cells[rot2].getX() + " " + cornerCell.cells[rot2].getY());
                else if(cornerCell != null)
                    System.out.println("start " + (cornerCell.cells[rot] != null ? cornerCell.cells[rot].getStage() : "1") + " " + (cornerCell.cells[rot2] != null ? cornerCell.cells[rot2].getStage() : "2"));*/
                    while (cornerCell != null && cornerCell.getStage() != null) {
                        if (getCellAt(cornerCell, rot) != null && Objects.requireNonNull(getCellAt(cornerCell, rot)).getStage() == null) {
                            Cell cell = getCellAt(cornerCell, rot);
                            if (cell != null) {
                                cell.add(stage);
                            }
                            //System.out.println(1 + " " + cornerCell.getStage());
                        } else if (getCellAt(cornerCell, rot) == null) {
                            Cell background = new Cell(1);
                            background.setX(cornerCell.getX() + (int) Math.cos(Math.toRadians(rot * 90)) * 32);
                            background.setY(cornerCell.getY() + (int) Math.sin(Math.toRadians(rot * 90)) * 32);
                            stage.addActor(background);

                            background.sort();
                        }

                        cornerCell = getCellAt(cornerCell, rot1);
                    }
                }

                needRemoveCells = true;
            }
        } catch (Exception e) {}
    }

    public static void sortCells(){
        endEntities = 0;
        Array<Actor> actors = stage.getActors();
        for(int i = 0; i < actors.size - endEntities; i++){
            if(!(actors.get(i) instanceof MyActor) && !(actors.get(i) instanceof Cell)){
                actors.get(i).toFront();

                i--;

                endEntities ++;
            }
        }
    }

    private static boolean hasCell(float x, float y){
        for(int i = 0; i < 0; i++){
            Actor cell = stage.getActors().get(i);
            if (cell.getX() <= x && cell.getX() + squareSize >= x && cell.getY() <= y && cell.getY() + squareSize >= y)
                return true;
        }

        return false;
    }
    public static Cell getCellAt(float x, float y){
        try {
            return cells[(int) (x / 32) + 122][(int) (y / 32) + (cells[0].length - 70) / 2];
        }
        catch (Exception e){
            return null;
        }
    }

    public static Cell getCellAt(Cell cell, int rot){
       return cells[(int) (cell.getX() / 32) + 122 + (int) Math.cos(Math.toRadians(rot * 90))]
                    [(int) (cell.getY() / 32) + (cells[0].length - 70) / 2 + (int) Math.sin(Math.toRadians(rot * 90))];
    }

    public static boolean onScreen(MyActor act){
        return act.getX() + act.getTexture().getRegionWidth() > player.getCentralX() - stage.getViewport().getWorldWidth() / 2 &&
                act.getX() < player.getCentralX() + stage.getViewport().getWorldWidth() / 2 &&
                act.getY() + act.getZ() + act.getTexture().getRegionHeight() > player.getCentralY() + player.getZ() - stage.getViewport().getWorldHeight() / 2 &&
                act.getY() + act.getZ() < player.getCentralY() + player.getZ() + stage.getViewport().getWorldHeight() / 2;
    }

    public static boolean onScreen(Actor act){
        return act.getRight() > player.getCentralX() - stage.getViewport().getWorldWidth() / 2 &&
                act.getX() < player.getCentralX() + stage.getViewport().getWorldWidth() / 2 &&
                act.getTop() > player.getCentralY() + player.getZ() - stage.getViewport().getWorldHeight() / 2 &&
                act.getY() < player.getCentralY() + player.getZ() + stage.getViewport().getWorldHeight() / 2;
    }

    public static boolean onScreen(Cell act){
        return act.getRight() > player.getCentralX() - 64 - stage.getViewport().getWorldWidth() / 2 &&
                act.getX() < player.getCentralX() + 64 + stage.getViewport().getWorldWidth() / 2 &&
                act.getTop() > player.getCentralY() + player.getZ() - 200 - stage.getViewport().getWorldHeight() / 2 &&
                act.getY() < player.getCentralY() + player.getZ() + 64 + stage.getViewport().getWorldHeight() / 2;
    }

    private static final Pool<Projectile> projPool = new Pool<Projectile>() {
        @Override
        protected Projectile newObject() {
            return new Projectile();
        }
    };
    private static final Array<Projectile> activeProjectiles = new Array<>();

    public static Projectile addProjectile(TextureRegion textureRegion, float speed, float z, float damage
            , Creature owner, int damageType, int itemIndex, int stacks) {
        Projectile projectile = projPool.obtain();
        projectile.init(textureRegion, speed, z, damage, owner, damageType, itemIndex, stacks);
        activeProjectiles.add(projectile);

        return projectile;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {
        state = State.PAUSE;
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        for (String key : textures.keySet()) {
            Texture texture = textures.get(key);
            texture.dispose();
        }
        stage.dispose();
    }
}
