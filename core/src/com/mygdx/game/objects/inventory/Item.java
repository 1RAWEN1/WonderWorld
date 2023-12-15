package com.mygdx.game.objects.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.characters.peaceful.Rabbit;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.scenes.GameScreen;

public class Item {
    private boolean splash = false;

    public boolean isSplash() {
        return splash;
    }

    public void setSplash(boolean splash) {
        this.splash = splash;
    }

    private String desc = "Cool item.";
    private Color color;
    private final int stackSize = 1;
    private int index;
    private int canGet;
    private int itemSize = 0;
    private int price;

    private float scale = 1;
    private int fuel;
    private int fuelPower;

    private int needFuelPower;
    private int creationTime;
    private int createdItem;

    private TextureRegion itemTexture;
    private TextureRegion texture;

    private int endurance = 0;
    private int maxEndurance = 0;

    private int armorSlot = 0;

    public Item(int index) {
        this.index = index;

        setItemSett();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getArmorSlot() {
        return armorSlot;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public int getMaxEndurance() {
        return maxEndurance;
    }

    public void setMaxEndurance(int maxEndurance) {
        this.maxEndurance = maxEndurance;
        if(maxEndurance == 50){
            desc = "STONE " + desc;
        }
    }

    public int getFuel() {
        return fuel;
    }

    public int getFuelPower() {
        return fuelPower;
    }

    public int getNeedFuelPower() {
        return needFuelPower;
    }

    public int getCreationTime() {
        return creationTime;
    }

    public int getCreatedItem() {
        return createdItem;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIndex(int index){
        this.index = index;

        needFuelPower = 0;
        canGet = 0;
        armor = 0;
        armorSlot = 0;
        creationTime = 0;
        createdItem = 0;
        fuel = 0;
        fuelPower = 0;

        setItemSett();
    }

    private float armor;
    private float magArmor;
    private int range;

    public int getRange() {
        return range;
    }

    private void setItemSett() {
        color = new Color(1, 1, 1, 1);
        switch (index) {
            case 1:
                setMaxEndurance(300);
                setEndurance(300);
                itemSize = 10;
                range = 30;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\knife.png"), 0, 64 * 8, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("inventory\\weapons.png"), 32 * 4 + 5, 15, 25, 10);

                splash = true;
                price = 30;
                desc = "KNIFE\ndamage(phys): 4\ncrit chance: 30%\ncooldown: 0.5sec\nattack dist: 15\nattack range: " + range + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 2:
                setMaxEndurance(300);
                setEndurance(300);
                canGet = -1;
                itemSize = 40;
                range = 45;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\axe.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("inventory\\weapons.png"), 32, 0, 32, 32);

                scale = 1.4f;

                price = 50;
                desc = "AXE\ndamage(phys): 7\ncrit chance: 15%\ncooldown: 1.2sec\nattack dist: 25\nattack range: " + range + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 3:
                setMaxEndurance(300);
                setEndurance(300);
                canGet = -2;
                itemSize = 40;
                range = 45;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\pickaxe.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("inventory\\weapons.png"), 32 * 2, 0, 32, 32);

                scale = 1.4f;

                price = 70;
                desc = "PICKAXE\ndamage(phys): 6\ncrit chance: 10%\ncooldown: 1.7sec\nattack dist: 25\nattack range: " + range + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 4:
                setMaxEndurance(300);
                setEndurance(300);
                itemSize = 20;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\bow.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 288, 60, 50, 50);

                scale = 0.6f;

                price = 40;
                desc = "BOW\ncrit chance: 20%\ncooldown: 1sec\nprojectile: arrow\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 31:
                setMaxEndurance(300);
                setEndurance(300);
                itemSize = 20;
                range = 45;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\shield.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\shield.png"), 25, 345, 32, 32);

                armor = 6;

                price = 80;
                desc = "SHIELD\narmor: " + armor + "\ndefence range: " + range + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 61:
                setMaxEndurance(900);
                setEndurance(900);
                armorSlot = 1;
                itemSize = 20;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\metal_helm_male.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\metal_helm_male.png"), 16, 140, 32, 32);

                armor = 3;

                scale = 1.4f;

                price = 90;
                desc = "METAL HELMET\narmor(phys): " + armor + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 62:
                setMaxEndurance(900);
                setEndurance(900);
                armorSlot = 2;
                itemSize = 30;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\plate_chest_male.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\plate_chest_male.png"), 16, 150, 32, 32);

                armor = 3;
                scale = 1.4f;

                price = 90;
                desc = "METAL CHEST PLATE\narmor(phys): " + armor + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            /*case 63:
                setMaxEndurance(300);
                setEndurance(300);
                armorSlot = 3;
                itemSize = 30;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\metal_gloves_male.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\metal_gloves_male.png"), 16, 150, 32, 32);

                armor = 1;
                scale = 1.4f;

                price = 30;
                break;*/
            case 63:
                setMaxEndurance(900);
                setEndurance(900);
                armorSlot = 3;
                itemSize = 30;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\plate_pants_male.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\plate_pants_male.png"), 16, 160, 32, 32);

                armor = 3;
                scale = 1.4f;

                price = 90;
                desc = "METAL PANTS\narmor(phys): " + armor + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 64:
                setMaxEndurance(300);
                setEndurance(300);
                armorSlot = 4;
                itemSize = 20;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\iron_boots.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\iron_boots.png"), 16, 160, 32, 32);

                scale = 1.4f;

                price = 50;
                desc = "IRON BOOTS\nspeed boost: +30%\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 65:
                setMaxEndurance(500);
                setEndurance(500);
                armorSlot = 1;
                itemSize = 10;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\robe.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\robe.png"), 16, 650, 32, 32);

                magArmor = 1;

                scale = 1.4f;

                price = 50;
                desc = "ROBE\narmor(phys): " + armor + "\narmor(mag): " + magArmor + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 66:
                setMaxEndurance(1000);
                setEndurance(1000);
                armorSlot = 2;
                itemSize = 30;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\plate_chest_male.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\plate_chest_male.png"), 16, 150, 32, 32);

                armor = 2;
                magArmor = 2;
                scale = 1.4f;

                color = new Color(0.5f, 1, 1, 1);

                price = 150;
                desc = "ENCHANTED METAL CHEST PLATE\narmor(phys): " + armor + "\narmor(mag): " + magArmor + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 67:
                setMaxEndurance(1000);
                setEndurance(1000);
                armorSlot = 3;
                itemSize = 30;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\plate_pants_male.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\plate_pants_male.png"), 16, 160, 32, 32);

                armor = 2;
                magArmor = 2;
                scale = 1.4f;

                color = new Color(0.5f, 1, 1, 1);

                price = 150;
                desc = "ENCHANTED METAL PANTS\narmor(phys): " + armor + "\narmor(mag): " + magArmor + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 68:
                setMaxEndurance(350);
                setEndurance(350);
                armorSlot = 4;
                itemSize = 20;
                texture = new TextureRegion(GameScreen.loadTexture("itemAnima\\iron_boots.png"), 0, 0, 64, 64);
                itemTexture = new TextureRegion(GameScreen.loadTexture("itemAnima\\iron_boots.png"), 16, 160, 32, 32);

                scale = 1.4f;

                color = new Color(0.5f, 1, 1, 1);

                price = 100;
                desc = "ENCHANTED METAL BOOTS\nspeed boost: +50% \nprice: " + price + "\nsize: " + itemSize;
                break;
            case 91:
                itemTexture = new TextureRegion(GameScreen.loadTexture("inventory\\wood.png"));
                itemSize = 15;
                fuel = 1;
                fuelPower = 100;

                scale = 15f / 50;

                price = 10;
                desc = "WOOD\nfuel power: " + fuelPower + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 92:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 350, 0, 50, 50);
                itemSize = 10;
                needFuelPower = 200;
                creationTime = 10000;
                createdItem = 94;

                scale = 10f / 50;

                price = 10;
                desc = "IRON\nneed fuel power: " + needFuelPower + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 93:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 405, 0, 50, 50);
                itemSize = 10;
                fuel = 2;
                fuelPower = 200;

                scale = 10f / 50;

                price = 10;
                desc = "COAL\nfuel power: " + fuelPower + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 94:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 405, 65, 50, 50);
                itemSize = 10;

                scale = 10f / 50;
                desc = "IRON INGOT\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 95:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 405, 285, 50, 50);
                itemSize = 10;
                needFuelPower = 100;
                creationTime = 6000;
                createdItem = 96;

                scale = 10f / 50;

                price = 20;
                desc = "RAW MEAT\nneed fuel power: " + needFuelPower + "\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 96:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 455, 285, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 30;
                desc = "BAKED MEAT\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 97:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 700, 460, 35, 60);
                itemSize = 10;

                scale = 10f / 50;

                price = 50;
                desc = "EMPTY FLASK\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 98:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 587, 517, 35, 60);
                itemSize = 10;

                scale = 10f / 50;

                price = 60;
                desc = "FLACK WITH WATER\n\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 99:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 579, 3, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 10;
                desc = "APPLE\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 100:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 520, 175, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 5;
                desc = "STONE\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 101:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 810, 465, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 500;
                desc = "MOON CRYSTAL\nrepair 1 endurance in 5sec\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 102:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 464, 2, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 100;
                desc = "CHITIN\nprice: " + price + "\nsize: " + itemSize;
                setColor(new Color(1, 1, 1, 0.7f));
                break;
            case 103:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 750, 520, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 500;
                desc = "GOLDEN CHITIN\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 104:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 809, 294, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 600;
                desc = "POTION OF REGENERATION\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 105:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 635, 350, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 1000;
                desc = "SHADOW PARTICLE\nsummons Shadow\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 106:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\arrow.png"));
                itemSize = 4;

                price = 10;
                desc = "ARROW\ndamage(phys): 8\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 107:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 636, 404, 50, 50);
                itemSize = 2;

                scale = 5f / 50;

                price = 10;
                desc = "CHITIN SHARD\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 108:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 405, 175, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 700;
                desc = "AIR STONE\nallows to hover\nprice: " + price + "\nsize: " + itemSize;
                break;
            case 109:
                itemTexture =  new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 635, 465, 50, 50);
                itemSize = 10;

                scale = 10f / 50;

                price = 200;
                desc = "MAGNETITH\nprice: " + price + "\nsize: " + itemSize;
                break;
        }
    }

    public float getArmor() {
        return armor;
    }

    public float getMagArmor() {
        return magArmor;
    }

    public void use(Creature cr){
        switch (index) {
            case 95: case 99:
                if(cr.getSatiety() < cr.getMaxSatiety()) {
                    cr.setSatiety(cr.getSatiety() + 5000);
                    remove(cr);
                }
                break;
            case 96:
                if(cr.getSatiety() < cr.getMaxSatiety()) {
                    cr.setSatiety(cr.getSatiety() + 10000);
                    remove(cr);
                }
                break;
            case 97:
                Cell cell = GameScreen.getCellAt(cr.getCentralX(), cr.getRectCenterY());
                if(cell != null && cell.getCellType() == 3)
                    setIndex(98);
                break;
            case 98:
                if(cr.getWater() < cr.getMaxWater()) {
                    cr.setWater(cr.getWater() + 5000);
                    setIndex(97);
                }
                break;
            case 104:
                if(cr.getHp() < cr.getMaxHp()){
                    cr.setHp(cr.getHp() + 5);
                    setIndex(97);
                }
                break;
            case 105:
                Rabbit shadow = new Rabbit(true);
                shadow.setPosition(GameScreen.player.getCentralX(), GameScreen.player.getRectCenterY());
                shadow.setZ(GameScreen.player.getZ());
                GameScreen.player.getStage().addActor(shadow);
                GameScreen.dialogReader.setTalker(shadow);
                shadow.setLookInv(true);
                remove(cr);
                break;
        }
    }

    public void remove(Creature cr){

        cr.getInventory().getAllItems().remove(this);
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public TextureRegion getItemTexture() {
        return itemTexture;
    }

    public void giveBoosts(Creature cr){
        switch (index){
            case 1:
                cr.setPhysDamage(4);
                cr.setCritChance(0.3f);
                cr.setDealtStanTime(200);
                cr.setCooldown(500);
                cr.setAttackDist(15);
                cr.setDropDist(2);
                cr.setAttackRange(range);
                cr.setDelay(0.5f);
                break;
            case 2:
                cr.setPhysDamage(7);
                cr.setCritChance(0.15f);
                cr.setDealtStanTime(500);
                cr.setCooldown(1200);
                cr.setAttackDist(25);
                cr.setDropDist(5);
                cr.setAttackRange(range);
                cr.setDelay(0.5f);
                break;
            case 3:
                cr.setPhysDamage(6);
                cr.setCritChance(0.1f);
                cr.setDealtStanTime(500);
                cr.setCooldown(1700);
                cr.setAttackDist(25);
                cr.setDropDist(5);
                cr.setAttackRange(range);
                cr.setDelay(0.5f);
                break;
            case 4:
                cr.setCritChance(0.2f);
                cr.setDealtStanTime(200);
                cr.setCooldown(1000);
                cr.setAttackDist(400);
                cr.setDropDist(5);
                cr.setDelay(0.5f);
                break;
            case 31:
                cr.setRealMass(cr.getMass() + 15);
                break;
            case 61: case 62: case 63: case 66: case 67:
                cr.setRealMass(cr.getMass() + 20);
                break;
            case 64: case 68:
                cr.setSpeedBoost(1.3f + (index == 68 ? 0.2f : 0));
                break;
        }
    }

    public float getScale() {
        return scale;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    public int getCanGet() {
        return canGet;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public boolean isCanBeInMainHand() {
        return index <= 30;
    }

    public boolean isCanBeInSecHand() {
        return index > 30 && index <= 60;
    }

    public boolean isArmor() {
        return index > 60 && index <= 90;
    }

    public int getStackSize() {
        return stackSize;
    }

    public int getIndex() {
        return index;
    }
}
