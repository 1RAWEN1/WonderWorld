package com.mygdx.game.objects.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.objects.*;
import com.mygdx.game.objects.characters.effects.DealtEffect;
import com.mygdx.game.objects.characters.enemy.Bug;
import com.mygdx.game.objects.characters.enemy.SkeletonMage;
import com.mygdx.game.objects.characters.neutral.Mage;
import com.mygdx.game.objects.characters.neutral.Villager;
import com.mygdx.game.objects.characters.peaceful.Fish;
import com.mygdx.game.objects.characters.peaceful.Rabbit;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.objects.decor.nature.Tree;
import com.mygdx.game.objects.inventory.FallenItem;
import com.mygdx.game.objects.inventory.Inventory;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.objects.inventory.WithInventory;
import com.mygdx.game.scenes.GameScreen;

import java.util.ArrayList;
import java.util.Iterator;

public class Creature extends MyActor implements MovableObject, WithInventory {
    private final ArrayList<DealtEffect> dealtEffects = new ArrayList<>();
    private final ArrayList<DealtEffect> effects = new ArrayList<>();
    private final Inventory inventory = new Inventory();

    private boolean lookInv;
    private Texture hpBar;
    private float slimeCof;

    public float getSlimeCof() {
        return slimeCof;
    }

    public void setSlimeCof(float slimeCof) {
        this.slimeCof = slimeCof;
    }

    public Creature(TextureRegion textureRegion, Rectangle3D rect) {
        super(textureRegion, rect);

        setDelay(0.5f);
    }

    public Creature(Rectangle3D rect) {
        super(rect);

        setDelay(0.5f);
    }

    public boolean isLookInv() {
        return lookInv;
    }

    public void setLookInv(boolean lookInv) {
        this.lookInv = lookInv;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private float speed = 1;

    private float zSpeed;

    private int rot;

    private float physArmor;
    private float magArmor;
    private float hp;
    private float maxHp;

    private int cooldown;
    private long lastTimeDealDamage;

    private float magDamage;
    private float physDamage;
    private float critChance;

    private float agility;
    private float maxAgility;

    private float delay;

    private float defaultMass;
    private float mass;
    public int move(float xSpeed, float ySpeed, float zSpeed){
        return move(xSpeed, ySpeed, zSpeed, true, false);
    }

    public boolean inStan(){
        return TimeUtils.millis() - getLastTimeDealStan() <= getStanTime();
    }

    private boolean canOpenDoor = false;

    public void setCanOpenDoor(boolean canOpenDoor) {
        this.canOpenDoor = canOpenDoor;
    }

    public int move(float xSpeed, float ySpeed, float zSpeed, boolean countAnima, boolean ignoreStan){
        int blockedCord = 0;
        if(xSpeed !=0 || ySpeed != 0 || zSpeed != 0){
            if(!inStan() && !cooldown() || xSpeed == 0 && ySpeed == 0 || ignoreStan) {
                Obstacle obs;
                float startX = getX();
                float startY = getY();

                if (xSpeed != 0) {
                    setX(getX() + xSpeed);

                    if (getRect().getX() < -100 * GameScreen.squareSize) {
                        setX(getX() - getRect().getX() - 100 * GameScreen.squareSize);
                        blockedCord = 1;
                    } else if (getRect().getX() + getRect().getWidth() > 32 * (GameScreen.mapSize + 100)) {
                        setX(getX() - (getRect().getX() + getRect().getWidth() - 32 * (GameScreen.mapSize + 100)));
                        blockedCord = 1;
                    } else {
                        if (countAnima) {
                            boolean b = Math.abs(ySpeed) <= Math.abs(xSpeed);
                            if (xSpeed > 0 && b) {
                                rot = 3;
                            } else if (b) {
                                rot = 1;
                            }
                        }
                    }

                    Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                    if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY()) && Math.abs(getZ() - cell.getZ(getCentralX(), getRectCenterY())) >= 25) {
                        setX(startX);

                        blockedCord = 1;
                    }
                }

                if (ySpeed != 0) {
                    setY(getY() + ySpeed);

                    if (getRect().getY() < -100 * GameScreen.squareSize) {
                        setY(getY() - getRect().getY() - 100 * GameScreen.squareSize);
                        blockedCord = 2;
                    } else if (getRect().getY() + getRect().getHeight() > 32 * (GameScreen.mapSize + 100)) {
                        setY(getY() - (getRect().getY() + getRect().getHeight() - 32 * (GameScreen.mapSize + 100)));
                        blockedCord = 2;
                    } else {
                        if (countAnima) {
                            boolean b = Math.abs(ySpeed) > Math.abs(xSpeed);
                            if (ySpeed > 0 && b) {
                                rot = 0;
                            } else if (b) {
                                rot = 2;
                            }
                        }
                    }

                    Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                    if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY()) && Math.abs(getZ() - cell.getZ(getCentralX(), getRectCenterY())) >= 25) {
                        setY(startY);

                        blockedCord = 2;
                    }
                }

                if (zSpeed != 0) {
                    setZ(getZ() + zSpeed);

                    Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                    if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY())) {
                        setZ(cell.getZ(getCentralX(), getRectCenterY()));

                        if(blockedCord == 0)
                            blockedCord = 3;
                    }
                }

                obs = (Obstacle) getOneIntersectingObject(canOpenDoor, Obstacle.class);
                if(obs != null && !obs.isStatic()) {
                    float[] vector = getCollision(obs);
                    obs.colMove(-vector[0] / 2 * (getMass() / obs.getMass()), -vector[1] / 2 * (getMass() / obs.getMass()), -vector[2] / 2 * (getMass() / obs.getMass()), this, true);
                    if (Math.abs(vector[0]) > Math.abs(vector[1]))
                        blockedCord = 1;
                    else
                        blockedCord = 2;

                    if(obs.getSlimeCof() > 0 && vector[0] < vector[2] && vector[1] < vector[2]){
                        blockedCord = 0;
                        this.zSpeed *= -obs.getSlimeCof();
                    }
                }
                else if(obs != null){
                    float[] vector = getCollision(obs);
                    setPosition(getX() + vector[0], getY() + vector[1]);
                    setZ(getZ() + vector[2]);
                    if (Math.abs(vector[0]) > Math.abs(vector[1]))
                        blockedCord = 1;
                    else
                        blockedCord = 2;

                    if(obs.getSlimeCof() > 0 && vector[0] < vector[2] && vector[1] < vector[2]){
                        blockedCord = 0;
                        this.zSpeed *= -obs.getSlimeCof();
                    }
                }


                //for(MyActor act : getIntersectingObjects(Creature.class)) {
                    Creature creature = (Creature) getOneIntersectingObject(Creature.class);
                    if(creature != null) {
                        float[] vector = getCollision(creature);
                        creature.colMove(-vector[0] / 2 * (getMass() / creature.getMass()), -vector[1] / 2 * (getMass() / creature.getMass()), -vector[2] / 2 * (getMass() / creature.getMass()), this, true);
                        if(creature.isStaticObj()) {
                            if (Math.abs(vector[0]) > Math.abs(vector[1]))
                                blockedCord = 1;
                            else
                                blockedCord = 2;
                        }

                        if(Math.abs(vector[0]) < Math.abs(vector[2]) && Math.abs(vector[1]) < Math.abs(vector[2])) {
                            if((creature.deltaX != 0 || creature.deltaY != 0) && vector[2] > 0){
                                move(-creature.deltaX, -creature.deltaY, 0);

                                creature.setMassDelta(getMass());
                            }
                            if (creature.getSlimeCof() > 0) {
                                blockedCord = 0;
                                this.zSpeed *= -creature.getSlimeCof();
                            }
                        }
                    }
            }
        }
        return blockedCord;
    }

    public void colMove(float xSpeed, float ySpeed, float zSpeed, MyActor cr, boolean impulse){
        if(xSpeed !=0 || ySpeed != 0 || zSpeed != 0) {
            float startX = getX();
            float startY = getY();
            float startZ = getZ();

            if (xSpeed != 0) {
                setX(getX() + xSpeed);

                if (getRect().getX() < -100 * GameScreen.squareSize) {
                    setX(getX() - getRect().getX() - 100 * GameScreen.squareSize);
                } else if (getRect().getX() + getRect().getWidth() > 32 * (GameScreen.mapSize + 100)) {
                    setX(getX() - (getRect().getX() + getRect().getWidth() - 32 * (GameScreen.mapSize + 100)));
                }

                Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY()) && Math.abs(getZ() - cell.getZ(getCentralX(), getRectCenterY())) >= 25) {
                    setX(startX);
                }
            }

            if (ySpeed != 0) {
                setY(getY() + ySpeed);

                if (getRect().getY() < -100 * GameScreen.squareSize) {
                    setY(getY() - getRect().getY() - 100 * GameScreen.squareSize);
                } else if (getRect().getY() + getRect().getHeight() > 32 * (GameScreen.mapSize + 100)) {
                    setY(getY() - (getRect().getY() + getRect().getHeight() - 32 * (GameScreen.mapSize + 100)));
                }

                Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY()) && Math.abs(getZ() - cell.getZ(getCentralX(), getRectCenterY())) >= 25) {
                    setY(startY);
                }
            }

            if (zSpeed != 0) {
                setZ(getZ() + zSpeed);
                Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY())) {
                    setZ(startZ);
                }
            }

            Obstacle obs = (Obstacle) getOneIntersectingObject(canOpenDoor, Obstacle.class);
            if (obs != null && obs.isStatic()) {
                float[] vector = getCollision(obs);
                setPosition(vector[0] + getX(), vector[1] + getY());
                setZ(vector[2] + getZ());

                if(obs.getSlimeCof() > 0 && vector[0] < vector[2] && vector[1] < vector[2]){
                    this.zSpeed *= -obs.getSlimeCof();
                }
            }

            if (impulse) {
                float[] vector = getCollision(cr);
                if(cr instanceof Creature)
                    ((Creature) cr).colMove(-vector[0], -vector[1], -vector[2], null, false);
            }
        }
    }

    public void physics(){
        //for(MyActor act : getIntersectingObjects(Creature.class)) {
            Creature creature = (Creature) getOneIntersectingObject(Creature.class);
            if(creature != null) {
                float[] vector = getCollision(creature);
                creature.colMove(-vector[0] / 2 * (getMass() / creature.getMass()), -vector[1] / 2 * (getMass() / creature.getMass()), -vector[2] / 2 * (getMass() / creature.getMass()), this, true);
            }

        updateZ();

        /*MyActor obs = getOneIntersectingObject(canOpenDoor, Obstacle.class);
        if (obs != null) {
            float[] vector = getCollision(obs);
            setX(vector[0] + getX());
            setY(vector[1] + getY());
            setZ(vector[2] + getZ());
        }*/
        //}
    }
    private float animation = 0;

    private int animaShots = 8;

    private int action = 8;

    private float animaSpeed = 7;
    public float lastX = -3000;
    public float lastY;

    private float lastZ = -3000;

    private int startX = -1;
    private int startY = -1;

    public void setStartCords(int startX, int startY){
        this.startX = startX;
        this.startY = startY;
    }

    private boolean canLookInv = true;

    public void setCanLookInv(boolean canLookInv) {
        this.canLookInv = canLookInv;
    }

    public boolean canLookInv() {
        return canLookInv;
    }

    private boolean isPeople = true;

    public void setPeople(boolean people) {
        isPeople = people;
    }

    public void setAnimation(int action, int animaShots, int animaSpeed){
        this.action = action;
        this.animaShots = animaShots;
        this.animaSpeed = animaSpeed;
    }

    private boolean take = true;

    public void setTake(boolean take){
        this.take = take;
    }

    public boolean isTake() {
        return take;
    }

    private boolean updateAnimation = true;

    public void setUpdateAnimation(boolean updateAnimation) {
        this.updateAnimation = updateAnimation;
    }

    private int attackAnimation = 12;

    public void setAttackAnimation(int attackAnimation) {
        this.attackAnimation = attackAnimation;
    }

    private float defaultPhysDamage = 1;

    public void setDefaultPhysDamage(float defaultPhysDamage) {
        this.defaultPhysDamage = defaultPhysDamage;
    }

    private final int fixingTime = 5000;
    private long lastFixingTime;

    private float massDelta;

    public float getMassDelta() {
        return massDelta;
    }

    public void setMassDelta(float massDelta) {
        this.massDelta = massDelta;
    }

    public float deltaX;
    public float deltaY;
    public void animate(){
        if(getAttackSpeedBoost() != 1)
            setAttackSpeedBoost(1);
        if(getMagicPower() < getMaxMagicPower()){
            setMagicPower(getMagicPower() + 0.2f);
        }
        else{
            setMagicPower(getMaxMagicPower());
        }

        if(getSpeedBoost() != 1)
            setSpeedBoost(1);
        setRealMass(defaultMass + massDelta);
        massDelta = 0;
        if(getCritCof() != 1)
            setCritCof(1);

        fix();

        heal();

        if(getEnemy() != null && getEnemy().getHp() <= 0){
            setEnemy(null);
        }

        if(this instanceof Player) {
            if (getMaxWater() != 0)
                updateWaterBar(-1);
            if (getMaxSatiety() != 0)
                updateSatietyBar(-1);

            if(getSatiety() <= 0 || getWater() <= 0){
                dealDamage(0.01f, 0, this);
            }
        }

        updateZ();

        FallenItem fallenItem = (FallenItem) getOneIntersectingObject(FallenItem.class);
        if(fallenItem != null && !isStaticObj() && take && (this instanceof Player || getInventory().isNeedRes(fallenItem.getItem()))
                && getInventory().getItemsSize() + fallenItem.getItem().getItemSize() <= getInventory().getInventorySize()){
            getInventory().add(fallenItem.getItem(), this);
            fallenItem.take();
        }

        deltaX = lastX - getCentralX();
        deltaY = lastY - getRectCenterY();

        if(lastX != -3000 && (lastX != getCentralX() || lastY != getRectCenterY())) {
            sort();

            if (this instanceof Player) {
                if (lastX > getCentralX()) {
                    GameScreen.generateCells(2, Math.abs((int) (lastX / 32) - (int) (getCentralX() / 32)));
                } else {
                    GameScreen.generateCells(0, Math.abs((int) (lastX / 32) - (int) (getCentralX() / 32)));
                }
                if (lastY > getRectCenterY()) {
                    GameScreen.generateCells(3, Math.abs((int) ((lastY + getZ()) / 32) - (int) ((getRectCenterY() + getZ()) / 32)));
                } else {
                    GameScreen.generateCells(1, Math.abs((int) ((lastY + getZ()) / 32) - (int) ((getRectCenterY() + getZ()) / 32)));
                }
            }

            if(myCell != null) {
                Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                if(cell != null && myCell != cell) {
                    myCell.actors.remove(this);

                    myCell = cell;
                    myCell.actors.add(this);
                }
                else if(cell == null){
                    setCentralX(lastX);
                    setRectCentralY(lastY);
                }
            }
            else{
                myCell = GameScreen.getCellAt(getCentralX(), getRectCenterY());

                if(myCell != null)
                    myCell.actors.add(this);
            }
        }

        if(!isStaticObj()){
            if(isPeople) {
                for (int i = 0; i < 6; i++) {
                    Item item = getInventory().getItems()[i];
                    if (item != null && item.getEndurance() > 0) {
                        item.giveBoosts(this);
                    } else if (item != null && item.getEndurance() <= 0) {
                        getInventory().getItems()[i] = null;
                    }
                }

                setSpeedBoost(getSpeedBoost() * defaultMass / getMass());
                setAgility(maxAgility * defaultMass / getMass());
            }

            if (getHp() <= 0) {
                setLastTimeDealStan(TimeUtils.millis());
                setStanTime(1000, false);

                if(isPeople) {
                    action = 20;
                    rot = 0;
                    animaShots = 6;
                    animaSpeed = 7;


                    setRectDelta(15);
                    getRect().setHeight(30);
                    getRect().setzSize(10);

                    if (animation < animaShots * animaSpeed - 2) {
                        animation++;
                    } else {
                        animation = animaShots * animaSpeed - 1;
                    }
                }
                else{
                    animation = 0;
                }

                if (transparency > 0) {
                    transparency -= 0.02;
                }
            }
            else if(inStan()){
                animation = 0;
            }
            else if (cooldown()) {
                if(isPeople) {
                    action = attackAnimation;
                    switch (attackAnimation){
                        case 16:
                            animaShots = 13;
                            break;
                        case 12:
                            animaShots = 5;
                            break;
                        case 0:
                            animaShots = 6;
                            break;
                    }
                    animaSpeed = (int) (7 * ((float) getCooldown() / 500) / (1.3 * (animaShots / 5)));
                }

                if (animation < animaShots * animaSpeed - 1) {
                    animation++;
                } else if(updateAnimation){
                    animation = 0;
                }
                else {
                    animation = animaShots * animaSpeed - 1;
                }
            } else {
                if(isPeople) {
                    action = 8;
                    animaShots = 8;
                    animaSpeed = 7 / getSpeedBoost();
                }
                if ((lastX != getCentralX() || lastY != getRectCenterY()) && animation < animaShots * animaSpeed - 1) {
                    animation++;
                } else if(updateAnimation){
                    animation = 0;
                }
                else {
                    animation = animaShots * animaSpeed - 1;
                }
            }

            canDealDamage();

            for(DealtEffect dealtEffect : effects) {
                dealtEffect.effect(this, TimeUtils.millis() - dealtEffect.getDealtTime());
            }
            Iterator<DealtEffect> it = effects.iterator();
            while(it.hasNext()) {
                DealtEffect dealtEffect = it.next();
                if(TimeUtils.millis() >= dealtEffect.getEffectTime() + dealtEffect.getDealtTime())
                    it.remove();
            }

            lastX = getCentralX();
            lastY = getRectCenterY();
        }
    }

    public void setAnimation(float animation) {
        this.animation = animation;
    }

    public ArrayList<DealtEffect> getDealtEffects() {
        return dealtEffects;
    }

    public void fix(){
        if(getInventory().hasItem(101) != -1 && TimeUtils.timeSinceMillis(lastFixingTime) >= fixingTime){
            for (int i = 0; i < 6; i++) {
                Item item = getInventory().getItems()[i];
                if(item != null && item.getEndurance() < item.getMaxEndurance()){
                    item.setEndurance(item.getEndurance() + 1);
                }
            }
            lastFixingTime = TimeUtils.millis();
        }
    }

    public void updateZ(){
        int hasStone = getInventory().hasItem(108);
        if(this instanceof Rabbit && ((Rabbit) this).isShadow()){
            hasStone = 1;
        }

        Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
        float underWaterZ = 0;
        //float waterLevel = 0;
        if(cell != null) {
            if(getZ() < cell.getZ(getCentralX(), getRectCenterY())){
                setZ(cell.getZ(getCentralX(), getRectCenterY()));
            }

            if(getStage() != null && getZ() > cell.getZ(getCentralX(), getRectCenterY())) {
                Array<Actor> actors = getStage().getActors();
                for (int i = actors.size - GameScreen.endEntities - 1; i > getZIndex(); i--) {
                    if (actors.get(i) instanceof MyActor &&
                            getRect().overlapsWZ(((MyActor) actors.get(i)).getRect()) &&
                            getZ() >= ((MyActor) actors.get(i)).getZ() + ((MyActor) actors.get(i)).getRect().getzSize()) {
                        ((MyActor) actors.get(i)).drawAfter.add(this);
                        setVisible(false);

                        break;
                    }
                }
            }
        }
        else{
            zSpeed = 0;
        }

        if((underWaterZ < -getRect().getzSize() * 0.5 || hasStone != -1) && zSpeed < -3){
            zSpeed += 0.4;
        }
        else{
            zSpeed-=0.4;
        }
        int n = move(0, 0, zSpeed);
        /*if(cell != null){
            if(getZ() < cell.getZ(getCentralX(), getRectCenterY()) + cell.getWaterLevel()) {
                underWaterZ = getZ() - cell.getZ(getCentralX(), getRectCenterY()) - cell.getWaterLevel();
                waterLevel = cell.getZ(getCentralX(), getRectCenterY()) + cell.getWaterLevel();
            }
            else if(getZ() < cell.getNormalZ()){
                underWaterZ = getZ() - cell.getNormalZ();
                waterLevel = cell.getNormalZ();
            }
        }
        if(underWaterZ < 0 && underWaterZ > -getRect().getzSize() * 0.5 && hasStone == -1 &&
                cell.getWaterLevel() >= getRect().getzSize() * 0.5){
            setZ(waterLevel - getRect().getzSize() * 0.5f);
        }*/

        if(this instanceof Player && lastZ != -3000 && lastZ != getZ()) {
            sort();

            if (lastZ < getZ()) {
                GameScreen.generateCells(1, Math.abs((int) ((lastZ + getRectCenterY()) / 32) - (int) ((getZ() + getRectCenterY()) / 32)));
            } else if (lastZ > getZ()) {
                GameScreen.generateCells(3, Math.abs((int) ((lastZ + getRectCenterY()) / 32) - (int) ((getZ() + getRectCenterY()) / 32)));
            }
        }

        lastZ = getZ();

        if(zSpeed < 0 && onGround() || n == 3){
            if(Math.abs(zSpeed) > 5){
                dealDamage((float) Math.pow(Math.abs(zSpeed) - 4, 1.5), (long) (Math.pow(Math.abs(zSpeed) - 4, 1.5) * 100), this);
            }

            zSpeed = 0;
        }

        if(!(this instanceof Fish))
            if(!inStan() && (underWaterZ < -getRect().getzSize() * 0.5 || hasStone != -1 && getEnemy() != null && getEnemy().getZ() > getZ()) && !(this instanceof Player)){
                setZSpeed(Math.min(getzSpeed() + 0.4f, 2));
            }
    }

    private float heal = 0.001f;
    public void heal(){
        if(getHp() < getMaxHp() && getSatiety() > 0 && getWater() > 0)
            setHp(getHp() + heal);
        else if(getHp() > getMaxHp())
            setHp(getMaxHp());
    }

    public void setHeal(float heal) {
        this.heal = heal;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setZSpeed(float zSpeed){
        this.zSpeed = zSpeed;
    }

    public float getzSpeed(){
        return zSpeed;
    }

    public boolean onGround(){
        float prevSpeed = zSpeed;
        lastZ = getZ();
        move(0, 0, -0.4f);
        boolean onGround = lastZ == getZ();
        setZ(lastZ);
        zSpeed = prevSpeed;
        return onGround;
    }

    private float speedBoost = 0;

    public float getSpeedBoost() {
        return speedBoost;
    }

    public void setSpeedBoost(float speedBoost) {
        this.speedBoost = speedBoost;
    }

    public float getSpeed() {
        return speed * speedBoost;
    }

    private int[][] map = new int[GameScreen.mapSize][GameScreen.mapSize];
    private int cellX;
    private int cellY;
    public boolean moveToCords(float x, float y, boolean a){
        if(Math.abs(getCentralX() - x) >= speed || Math.abs(getY() - y) >= speed) {
            if ((int) x / 32 != cellX || (int) y / 32 != cellY) {
                cellX = (int) x / 32;
                cellY = (int) y / 32;

                map = new int[GameScreen.mapSize][GameScreen.mapSize];

                map[cellX][cellY] = 1;

                int iterations = 0;
                while (map[(int) (getCentralX() / 32)][(int) (getY() / 32)] == 0) {
                    iterations++;
                    for (int i = 0; i < map.length; i++)
                        for (int j = 0; j < map[i].length; j++) {
                            if (GameScreen.getMap()[i][j] >= 0) {
                                if (i - 1 >= 0 && map[i - 1][j] > 0 && (map[i][j] == 0 || map[i - 1][j] + 1 < map[i][j])) {
                                    map[i][j] = map[i - 1][j] + 1;
                                }
                                if (i + 1 < map.length && map[i + 1][j] > 0 && (map[i][j] == 0 || map[i + 1][j] + 1 < map[i][j])) {
                                    map[i][j] = map[i + 1][j] + 1;
                                }

                                if (j - 1 >= 0 && map[i][j - 1] > 0 && (map[i][j] == 0 || map[i][j - 1] + 1 < map[i][j])) {
                                    map[i][j] = map[i][j - 1] + 1;
                                }
                                if (j + 1 < map[i].length && map[i][j + 1] > 0 && (map[i][j] == 0 || map[i][j + 1] + 1 < map[i][j])) {
                                    map[i][j] = map[i][j + 1] + 1;
                                }
                            }
                        }
                    if (iterations > 10000) {
                        break;
                    }
                }
            }

            float needX = 0;
            float needY = 0;

            if ((int) getCentralX() / 32 - 1 >= 0 && map[(int) getCentralX() / 32 - 1][(int) getY() / 32] == map[(int) getCentralX() / 32][(int) getY() / 32] - 1) {
                needX = ((int) getCentralX() / 32 - 1) * 32 + 16;
                needY = ((int) getY() / 32) * 32 + 16;
            }
            else if ((int) getCentralX() / 32 + 1 < map.length && map[(int) getCentralX() / 32 + 1][(int) getY() / 32] == map[(int) getCentralX() / 32][(int) getY() / 32] - 1) {
                needX = ((int) getCentralX() / 32 + 1) * 32 + 16;
                needY = ((int) getY() / 32) * 32 + 16;
            }
            else if ((int) getY() / 32 - 1 >= 0 && map[(int) getCentralX() / 32][(int) getY() / 32 - 1] == map[(int) getCentralX() / 32][(int) getY() / 32] - 1) {
                needY = ((int) getY() / 32 - 1) * 32 + 16;
                needX = ((int) getCentralX() / 32) * 32 + 16;
            }
            else if ((int) getY() / 32 + 1 < map[(int) getX() / 32].length && map[(int) getCentralX() / 32][(int) getY() / 32 + 1] == map[(int) getCentralX() / 32][(int) getY() / 32] - 1) {
                needY = ((int) getY() / 32 + 1) * 32 + 16;
                needX = ((int) getCentralX() / 32) * 32 + 16;
            }

            if(Math.abs(getCentralX() - x) <= 32 && Math.abs(getY() - y) <= 32){
                needX = x;
                needY = y;
            }

            move(Math.abs(needX - getCentralX()) < speed ? needX - getCentralX() : Float.compare(needX, getCentralX()) * getSpeed(), Math.abs(needY - getY()) < speed ? needY - getY() : Float.compare(needY, getY()) * getSpeed(), 0, true, false);

            return true;
        }
        else{
            return false;
        }
    }

    private float movingRot;
    public int sum;

    private boolean leftHand;

    private int moves = 0;
    private final int needMoves = 1;
    private float movingDist;

    public void setMovingDist(float delta) {
        if(getInventory().getItems()[4] != null && getInventory().getItems()[4].getIndex() == 64) {
            movingDist += delta;

            if (movingDist >= 200) {
                movingDist = 0;

                getInventory().getItems()[4].setEndurance(getInventory().getItems()[4].getEndurance() - 1);
            }
        }
        else{
            movingDist = 0;
        }
    }
    public void moveToCords(float x, float y){
        if(sum != 0) {
            int n = move((float) (getSpeed() * Math.cos(Math.toRadians(movingRot))), (float) (getSpeed() * Math.sin(Math.toRadians(movingRot))), 0, true, false);
            if(n == 0){
                setMovingDist(getSpeed());
            }
            else if(n != 3){
                turnWhenObsIsAhead(n, x, y);
            }

            int n1 = 0;

            float checkCollisionRot = turn(movingRot, !leftHand);
            float startX = getX();
            float startY = getY();
            setPosition((float) (getX() + Math.cos(Math.toRadians(checkCollisionRot)) * getRect().getWidth())
                    , (float) (getY() + Math.sin(Math.toRadians(checkCollisionRot)) * getRect().getHeight()));

            MyActor act = getOneIntersectingObject(canOpenDoor, Obstacle.class);
            Creature creature = (Creature) getOneIntersectingObject(Creature.class);
            Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
            if(act != null || creature != null && creature.isStaticObj() ||
            getRect().getX() < 0 || getRect().getX() + getRect().getWidth() > GameScreen.mapSize * 32 || getRect().getY() < 0 || getRect().getY() + getRect().getHeight() > GameScreen.mapSize * 32 ||
            cell != null && cell.getZ(getCentralX(), getRectCenterY()) > getZ() + getRect().getzSize() * 0.5){
                n1 = 1;
            }

            setPosition(startX, startY);
            //n1 = move((float) (Math.cos(Math.toRadians(checkCollisionRot)) * getSpeed()), (float) (Math.sin(Math.toRadians(checkCollisionRot)) * getSpeed()), 0, false, false);

            if(n1 == 0) {
                moves++;
                if(moves >= needMoves) {
                    moves = 10;

                    movingRot = checkCollisionRot;

                    if (!leftHand)
                        sum--;
                    else
                        sum++;
                }
            }
            else{
                moves = 0;
            }
        }
        else if(moves != 0){
            int n = move((float) (getSpeed() * Math.cos(Math.toRadians(movingRot))), (float) (getSpeed() * Math.sin(Math.toRadians(movingRot))), 0, true, false);
            moves --;

            if(n == 0){
                setMovingDist(getSpeed());
            }
        }
        else if (Math.abs(getCentralX() - x) >= speed || Math.abs(getRectCenterY() - y) >= speed){
            float movRot = getRotTo(x, y);
            float xSpeed = (float) (Math.cos(Math.toRadians(movRot)) * getSpeed());
            float ySpeed = (float) (Math.sin(Math.toRadians(movRot)) * getSpeed());
            int n = move(xSpeed, ySpeed, 0, true, false);

            if(n == 0){
                setMovingDist(getSpeed());
            }
            else if(n != 3){
                turnWhenObsIsAhead(n, x, y);
            }
        }
    }

    public boolean moveToCords(float x, float y, int a){
        if(a == 1)
            System.out.println(sum + " " + movingRot + " " + moves + " " + leftHand);
        if(sum != 0) {
            int n = move((float) (getSpeed() * Math.cos(Math.toRadians(movingRot))), (float) (getSpeed() * Math.sin(Math.toRadians(movingRot))), 0, true, false);
            if(a == 1)
                System.out.println(n + " " + movingRot);
            if(n != 0 && n != 3){
                turnWhenObsIsAhead(n, x, y);
                if(a == 1)
                    System.out.println(movingRot + " mr");
            }

            int n1 = 0;

            float checkCollisionRot = turn(movingRot, !leftHand);
            float startX = getX();
            float startY = getY();
            setPosition((float) (getX() + Math.cos(Math.toRadians(checkCollisionRot)) * 20)
                    , (float) (getY() + Math.sin(Math.toRadians(checkCollisionRot)) * 10));

            if(isTouching(Obstacle.class) || isTouching(Creature.class)){
                n1 = 1;
            }

            setPosition(startX, startY);
            //n1 = move((float) (Math.cos(Math.toRadians(checkCollisionRot)) * getSpeed()), (float) (Math.sin(Math.toRadians(checkCollisionRot)) * getSpeed()), 0, false, false);
            if(a == 1)
                System.out.println(movingRot + " mr1 " + n1);
            if(n1 == 0) {
                moves++;
                if(moves >= needMoves) {
                    movingRot = checkCollisionRot;

                    if (!leftHand)
                        sum--;
                    else
                        sum++;
                }
            }
            else{
                moves = 0;
            }
            if(a == 1)
                System.out.println(movingRot + " mr2 " + checkCollisionRot + " " + moves);
            return true;
        }
        else if(moves != 0){
            move((float) (getSpeed() * Math.cos(Math.toRadians(movingRot))), (float) (getSpeed() * Math.sin(Math.toRadians(movingRot))), 0, true, false);
            moves --;

            return true;
        }
        else if (Math.abs(getCentralX() - x) >= speed || Math.abs(getRectCenterY() - y) >= speed){
            float xSpeed = Math.abs(getCentralX() - x) < speed ? 0 : Float.compare(x, getCentralX()) * getSpeed();
            float ySpeed = Math.abs(getRectCenterY() - y) < speed ? 0 : Float.compare(y, getRectCenterY()) * getSpeed();
            int n = move(xSpeed, ySpeed, 0, true, false);

            if(a == 1)
                System.out.println(n + " 0sum");
            if(n != 0 && n != 3){
                turnWhenObsIsAhead(n, x, y);
            }
            return true;
        }
        else{
            return false;
        }
    }

    private void turnWhenObsIsAhead(int n, float x, float y){
        if(sum == 0) {
            if (n == 1) {
                if (y > getY()) {
                    movingRot = 90;
                    leftHand = x > getCentralX();
                } else {
                    movingRot = 270;
                    leftHand = x < getCentralX();
                }
            } else if (n == 2) {
                if (x < getCentralX()) {
                    movingRot = 180;
                    leftHand = y > getY();
                } else {
                    movingRot = 0;
                    leftHand = y < getY();
                }
            }
        }
        else{
            movingRot = turn(movingRot, leftHand);
        }

        if(leftHand)
            sum--;
        else
            sum++;
    }

    private float turn(float movingRot, boolean leftHand){
        if(leftHand)
            movingRot += 90;
        else
            movingRot -= 90;

        return (movingRot + 360) % 360;
    }

    private float attackRange = 30;

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public MyActor getObject(float rot, Class<?> c){
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this
                    && getStage().getActors().get(i) instanceof MyActor) {
                MyActor actor = (MyActor) getStage().getActors().get(i);
                if (getDist(actor) <= actor.getRect().getRadius(getCentralX(), getRect().getCenterY()) + getRect().getRadius(actor.getCentralX(), actor.getRect().getCenterY()) + attackDist &&
                        Math.abs(getZ() - actor.getZ()) <= actor.getRect().getzSize() + getRect().getzSize() && getRotDelta(getRotTo(actor), rot) <= attackRange &&
                        (!c.isInstance(Creature.class) || ((Creature) actor).getTeam() != getTeam())) {
                    return actor;
                }
            }
        }
        return null;
    }

    public ArrayList<MyActor> getObjects(float rot, Class<?> c){
        ArrayList<MyActor> actors = new ArrayList<>();
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this
                    && getStage().getActors().get(i) instanceof MyActor) {
                MyActor actor = (MyActor) getStage().getActors().get(i);
                if (getDist(actor) <= actor.getRect().getRadius(getCentralX(), getRect().getCenterY()) + getRect().getRadius(actor.getCentralX(), actor.getRect().getCenterY()) + attackDist &&
                        Math.abs(getZ() - actor.getZ()) <= actor.getRect().getzSize() + getRect().getzSize() && getRotDelta(getRotTo(actor), rot) <= attackRange &&
                        (!c.isInstance(Creature.class) || ((Creature) actor).getTeam() != getTeam())) {
                    actors.add(actor);
                }
            }
        }
        return actors;
    }

    public MyActor getNearestEnemy(Class<?> c){
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this
                    && getStage().getActors().get(i) instanceof MyActor) {
                MyActor actor = (MyActor) getStage().getActors().get(i);
                if (getDist(actor) <= actor.getRect().getRadius(getCentralX(), getRect().getCenterY()) + getRect().getRadius(actor.getCentralX(), actor.getRect().getCenterY()) + attackDist &&
                        Math.abs(getZ() - actor.getZ()) <= actor.getRect().getzSize() + getRect().getzSize() &&
                        (!c.isInstance(Creature.class) || ((Creature) actor).getTeam() != getTeam())) {
                    return actor;
                }
            }
        }
        return null;
    }

    private int team;

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean checkObject(MyActor actor){
        return actor != null && getDist(actor) <= actor.getRect().getRadius(getCentralX(), getRect().getCenterY()) + getRect().getRadius(actor.getCentralX(), actor.getRect().getCenterY()) + attackDist &&
                Math.abs(getZ() - actor.getZ()) <= actor.getRect().getzSize() + getRect().getzSize() && (!(actor instanceof Creature) || getMagDamage() < 0 || ((Creature) actor).getTeam() != getTeam() ||  ((Creature) actor).getTeam() == 0);
    }

    public boolean checkObjectFull(MyActor actor){
        return actor != null && getDist(actor) <= actor.getRect().getRadius(getCentralX(), getRect().getCenterY()) + getRect().getRadius(actor.getCentralX(), actor.getRect().getCenterY()) + attackDist &&
                Math.abs(getZ() - actor.getZ()) <= actor.getRect().getzSize() + getRect().getzSize() && getRotDelta(getRotTo(actor), targetRot) <= attackRange && (!(actor instanceof Creature) || ((Creature) actor).getTeam() != getTeam());
    }

    private int satiety;
    private int maxSatiety;
    private int water;
    private int maxWater;

    public int getMaxSatiety() {
        return maxSatiety;
    }

    public void setMaxSatiety(int maxSatiety) {
        this.maxSatiety = maxSatiety;
        satiety = maxSatiety;
    }

    public int getSatiety() {
        return satiety;
    }

    public void setSatiety(int satiety) {
        this.satiety = satiety;
    }

    public int getMaxWater() {
        return maxWater;
    }

    public void setMaxWater(int maxWater) {
        this.maxWater = maxWater;
        water = maxWater;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    private float attackSpeedBoost = 1;

    public float getAttackSpeedBoost() {
        return attackSpeedBoost;
    }

    public void setAttackSpeedBoost(float attackSpeedBoost) {
        this.attackSpeedBoost = attackSpeedBoost;
    }

    private boolean dealDamage;
    private Projectile projectile;

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    private float needDist;

    public void setNeedDist(float needDist) {
        this.needDist = needDist;
    }

    private float magicPower = 300;
    private float maxMagicPower = 300;
    private float magicCost = 0;

    public void setMagicCost(float magicCost) {
        this.magicCost = magicCost;
    }

    public float getMagicCost() {
        return magicCost;
    }

    public float getMagicPower() {
        return magicPower;
    }

    public void setMagicPower(float magicPower) {
        this.magicPower = magicPower;
    }

    public float getMaxMagicPower() {
        return maxMagicPower;
    }

    public void setMaxMagicPower(float maxMagicPower) {
        this.maxMagicPower = maxMagicPower;
    }

    public void setMP(float mp){
        maxMagicPower = mp;
        magicPower = mp;
    }

    private void useMP(float mp, Projectile proj){
        int n = (projectile != null ? 1 : 3);

        mp *= (this instanceof Player && ((Player) this).isMaterialMagicType() == 0 && proj.getStacks() != 0 ? proj.getStacks() : 1);
        double j = 1;
        if(proj != null){
            j = Math.max((proj.getSpeed() / 10), 0.1) * proj.getMaxDist() / 16 * Math.pow(proj.getScaleX() / 0.4, 3);
        }
        mp += 30 * (this instanceof Player && ((Player) this).isMaterialMagicType() == 0 ? proj.getStacks() : 1) * Math.abs(getMagDamage()) / 4 * n * j;

        if(this instanceof Player && proj != null)
            mp *= projNum;
        if(this instanceof Player && ((Player) this).isMaterialMagicType() == 0){
            mp = 0;
        }

        setMagicPower(getMagicPower() - mp);
        if(getMagicPower() < 0){
            dealDamage(-(getMagicPower() * 20 / getMaxMagicPower()), (long)((getMagicPower() * 20 / getMaxMagicPower()) * 100), this);
        }
    }

    private int projNum = 1;
    private float projAngle = 0;

    public void setProjNum(int projNum) {
        this.projNum = projNum;
    }

    public void setProjAngle(float projAngle) {
        this.projAngle = projAngle;
    }

    public int getProjNum() {
        return projNum;
    }

    public boolean isDealDamage() {
        return dealDamage;
    }

    public void canDealDamage(){
        if(inStan()){
            lastTimeDealDamage = TimeUtils.millis() - (int)(cooldown / attackSpeedBoost);
        }
        else if(TimeUtils.timeSinceMillis(lastTimeDealDamage) >= cooldown / attackSpeedBoost * delay &&
                TimeUtils.timeSinceMillis(lastTimeDealDamage) < cooldown / attackSpeedBoost && !dealDamage){
            Creature creature = (Creature) getObject(targetRot, Creature.class);
            ArrayList<MyActor> creatures = null;
            if(getInventory().getItems()[0] != null && getInventory().getItems()[0].isSplash())
                creatures = getObjects(targetRot, Creature.class);

            Projectile proj = (Projectile) getObject(targetRot, Projectile.class);
            if((target != null && checkObjectFull(target) || projectile != null)){
                if(projectile == null) {
                    if (damageType == 1) {
                        if(creatures != null) {
                            for (MyActor actor : creatures)
                                dealDamageWithDelay(getPhysDamage(), (Creature) actor, damageType, false);
                        }
                        else{
                            dealDamageWithDelay(getPhysDamage(), target, damageType, false);
                        }
                    }
                    else {
                        if(creatures != null) {
                            for (MyActor actor : creatures)
                                dealDamageWithDelay(getMagDamage(), (Creature) actor, damageType, false);
                        }
                        else{
                            dealDamageWithDelay(getMagDamage(), target, damageType, false);
                        }
                    }

                    if (getInventory().getItems()[0] != null)
                        getInventory().getItems()[0].setEndurance(getInventory().getItems()[0].getEndurance() - 1);
                }
                else{
                    if(projNum == 1 || damageType == 1) {
                        if (projectile.getDamageType() == 1) {
                            if (target != null)
                                projectile.setNeedZSpeed(getDist(target) - Math.min(0, (getRect().getzSize() - target.getRect().getzSize()) * 0.7));
                            else if (needDist > 0) {
                                projectile.setNeedZSpeed(needDist);

                                needDist = 0;
                            } else
                                projectile.setzSpeed(1);
                        } else {
                            if (target != null) {
                                projectile.setzSpeed((target.getZ() - projectile.getZ()) / (needDist / projectile.getSpeed()));
                            } else if (needDist > 0) {
                                projectile.setzSpeed(-(projectile.getZ() - GameScreen.player.getZ()) / (needDist / projectile.getSpeed()));
                            }
                        }

                        projectile.setxSpeed((float) (Math.cos(Math.toRadians(targetRot)) * projectile.getSpeed()));
                        projectile.setySpeed((float) (Math.sin(Math.toRadians(targetRot)) * projectile.getSpeed()));

                        projectile.setCentralX(getCentralX());
                        projectile.setCentralY(getRectCenterY());

                        getStage().addActor(projectile);

                        projectile.sort();
                        projectile.active = true;

                        for(DealtEffect dealtEffect : dealtEffects){
                            dealtEffect.setDealtTime(TimeUtils.millis());

                            if(dealtEffect.getType() < 0)
                                if(dealtEffect.getType() != -1)
                                    effects.add(new DealtEffect(dealtEffect.getDealtTime(), dealtEffect.getType()));
                                else
                                    effects.add(dealtEffect);
                        }

                        if (!(this instanceof Bug)) {
                            int arrowIndex = getInventory().hasItem(106);
                            if (arrowIndex != -1)
                                getInventory().getAllItems().remove(arrowIndex);
                        }
                    }
                    else{
                        targetRot = targetRot - (projNum * projAngle / 2);
                        for(int i = 0; i < projNum; i++) {
                            Projectile projectile = new Projectile();
                            projectile.init(this.projectile.getTexture(), this.projectile.getSpeed(),
                                    this.projectile.getZ(), this.projectile.getDamage(), this.projectile.getOwner(),
                                    2, 0, this.projectile.getStacks());
                            projectile.setColor(new Color(220f / 256, 220f / 256, 220f / 256, 1));
                            projectile.setScale(this.projectile.getScaleX());
                            projectile.setMaxDist(this.projectile.getMaxDist());
                            projectile.setAutoRotate(this.projectile.isAutoRotate());

                            if (projectile.getDamageType() == 1) {
                                if (target != null)
                                    projectile.setNeedZSpeed(getDist(target) - Math.min(0, (getRect().getzSize() - target.getRect().getzSize()) * 0.7));
                                else if (needDist > 0) {
                                    projectile.setNeedZSpeed(needDist);

                                    needDist = 0;
                                } else
                                    projectile.setzSpeed(1);
                            } else {
                                if (target != null) {
                                    projectile.setzSpeed((target.getZ() - projectile.getZ()) / (needDist / projectile.getSpeed()));
                                } else if (needDist > 0) {
                                    projectile.setzSpeed(-(projectile.getZ() - GameScreen.player.getZ()) / (needDist / projectile.getSpeed()));
                                }
                            }

                            projectile.setxSpeed((float) (Math.cos(Math.toRadians(targetRot)) * projectile.getSpeed()));
                            projectile.setySpeed((float) (Math.sin(Math.toRadians(targetRot)) * projectile.getSpeed()));

                            projectile.setCentralX(getCentralX());
                            projectile.setCentralY(getRectCenterY());

                            getStage().addActor(projectile);

                            projectile.sort();
                            projectile.active = true;

                            targetRot += projAngle;
                        }
                    }
                }
            }
            else if(proj != null && damageType == 1 && (creature == null || getDist(creature) > getDist(proj))){
                proj.remove();
                if (getInventory().getItems()[0] != null)
                    getInventory().getItems()[0].setEndurance(getInventory().getItems()[0].getEndurance() - 1);
            }
            else if(creature != null){
                if(damageType == 1) {
                    if(creatures != null) {
                        for (MyActor actor : creatures)
                            dealDamageWithDelay(getPhysDamage(), (Creature) actor, damageType, false);
                    }
                    else{
                        dealDamageWithDelay(getPhysDamage(), creature, damageType, false);
                    }
                }
                else {
                    if(creatures != null) {
                        for (MyActor actor : creatures)
                            dealDamageWithDelay(getMagDamage(), (Creature) actor, damageType, false);
                    }
                    else{
                        dealDamageWithDelay(getMagDamage(), creature, damageType, false);
                    }
                }

                if (getInventory().getItems()[0] != null)
                    getInventory().getItems()[0].setEndurance(getInventory().getItems()[0].getEndurance() - 1);
            }
            else if(damageType == 1){
                Obstacle res = (Obstacle) getObject(targetRot, Obstacle.class);
                if(res != null && !dealDamage && getInventory().getTypeOfResICanGet() != 0) {
                    boolean hasRes = false;
                    int i;
                    switch (getInventory().getTypeOfResICanGet()) {
                        case -1:
                            for(i = 0; i < res.getMaterials().length; i += 2){
                                if(res.getMaterials()[i + 1] == 91 && res.getMaterials()[i] > 0){
                                    hasRes = true;
                                    break;
                                }
                            }
                            if(hasRes) {
                                Item item = new Item(91);
                                getInventory().add(item, this);

                                res.getMaterials()[i]--;
                            }
                            break;
                        case -2:
                            int resType = 0;
                            for(i = 0; i < res.getMaterials().length; i += 2){
                                if((res.getMaterials()[i + 1] == 100 ||
                                        res.getMaterials()[i + 1] == 94 ||
                                        res.getMaterials()[i + 1] == 93 ||
                                        res.getMaterials()[i + 1] == 92) && res.getMaterials()[i] > 0){
                                    resType = res.getMaterials()[i + 1];
                                    hasRes = true;
                                    break;
                                }
                            }
                            if(hasRes) {
                                if(resType == 94){
                                    resType = 92;
                                }
                                Item item1 = new Item(resType);
                                getInventory().add(item1, this);

                                res.getMaterials()[i]--;
                            }
                            break;
                    }
                    getInventory().getItems()[0].setEndurance(getInventory().getItems()[0].getEndurance() - 1);
                }
                else if(res != null && !dealDamage && getInventory().getIndexInMainHand() == 0 && Math.random() <= 0.15 &&
                res instanceof Tree && ((Tree)res).getTreeType() == 1){
                    Item item = new Item(99);
                    getInventory().add(item, this);
                }
                else if(res != null && !dealDamage && getInventory().getIndexInMainHand() == 0 && Math.random() <= 0.15 &&
                        res instanceof Tree && ((Tree)res).getTreeType() == 1){
                    Item item = new Item(91);
                    getInventory().add(item, this);
                }
            }

            dealDamage = true;

            if(damageType == 2){
                useMP(magicCost, projectile);
            }
            projectile = null;
        }
    }

    private int magicType = 0;

    public void setMagicType(int magicType) {
        this.magicType = magicType;
    }

    public int getMagicType() {
        return magicType;
    }

    private final int magicCooldown = 10000;
    private long lastMagicTime;
    public void castMagic(float rot){
        if(TimeUtils.timeSinceMillis(lastMagicTime) > magicCooldown) {
            if(magicType >= 1){
                move((float) (Math.cos(Math.toRadians(rot)) * 160), (float) (Math.sin(Math.toRadians(rot)) * 160), 0);
            }
            lastMagicTime = TimeUtils.millis();
        }
    }

    public boolean cooldown() {
        return TimeUtils.timeSinceMillis(lastTimeDealDamage) < cooldown / attackSpeedBoost;
    }

    public long getLastTimeDealDamage() {
        return lastTimeDealDamage;
    }

    public void setLastTimeDealDamage(long lastTimeDealDamage) {
        this.lastTimeDealDamage = lastTimeDealDamage;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getRot() {
        return rot;
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public int getAnimaShots() {
        return animaShots;
    }

    public void setAnimaShots(int animaShots) {
        this.animaShots = animaShots;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public float getAnimaSpeed() {
        return animaSpeed;
    }

    public void setAnimaSpeed(float animaSpeed) {
        this.animaSpeed = animaSpeed;
    }

    public float getMagArmor() {
        Item item = getInventory().getItems()[1 + (int) (Math.random() * 4)];
        if (item != null)
            return magArmor + item.getMagArmor();
        else
            return magArmor;
    }

    public void setMagArmor(float magArmor) {
        this.magArmor = magArmor;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getMagDamage() {
        return magDamage;
    }

    public void setMagDamage(float magDamage) {
        this.magDamage = magDamage;
    }

    public float getPhysArmor() {
        Item item = getInventory().getItems()[Math.random() <= 0.3 ? 4 : 1 + (int) (Math.random() * 2)];
        if (item != null)
            return physArmor + item.getArmor();
        else
            return physArmor;
    }

    public int getArmorIndex(){
        return 1 + (int) (Math.random() * 3);
    }

    public void setPhysArmor(float physArmor) {
        this.physArmor = physArmor;
    }

    public float getPhysDamage() {
        return physDamage;
    }

    public void setPhysDamage(float physDamage) {
        this.physDamage = physDamage;
    }

    private long lastTimeDealStan;
    private long stanTime;
    private int dealtStanTime = 500;
    private boolean freeze;

    private float targetRot;

    private float attackDist = 10;

    private int damageType = 0;
    private Creature target;

    public float creatureRot;
    public boolean block;
    public float currentBlockArmor;

    public void dealDamage(boolean phys, Creature target){
        if(!cooldown()) {
            sum = 0;
            moves = 0;

            if(this instanceof Player && !phys)
                setMaxMagicPower(getMaxMagicPower() + 1);

            if(projectile != null && projectile.getDamageType() == 1){
                damageType = 1;
                attackAnimation = 16;
            }
            else {
                if (phys) {
                    damageType = 1;
                    attackAnimation = 12;
                } else {
                    damageType = 2;
                    attackAnimation = 0;
                }
            }

            this.target = target;
            targetRot = getRotTo(target);
            dealDamage = false;

            if (getInventory().getIndexInMainHand() == 0 && isPeople && (projectile == null || projectile.getDamageType() == 2)) {
                if(phys) {
                    setCooldown(500);

                    setPhysDamage(defaultPhysDamage);

                    setCritChance(0.2f);

                    setDealtStanTime(500);

                    setDropDist(5);

                    setAttackDist(10);

                    setAttackRange(30);
                }
                else{
                    setCooldown(700);

                    setCritChance(0.2f);

                    if(getAttackDist() != 50) {
                        setAttackDist(160);
                        if (this instanceof SkeletonMage) {
                            setAttackDist(150);
                        }
                    }

                    setDropDist(5);

                    setAttackRange(60);
                }
            }

            setRot(Math.abs(targetRot) <= 45 ? 3 : targetRot > 45 && targetRot <= 135 ? 0 : Math.abs(targetRot) > 135 ? 1 : 2);

            updateTimer();
        }
    }

    public void dealDamage(boolean phys, float rot){
        if(!cooldown()) {
            sum = 0;
            moves = 0;

            if(this instanceof Player && !phys)
                setMaxMagicPower(getMaxMagicPower() + 1);

            if(projectile != null && projectile.getDamageType() == 1){
                damageType = 1;
                attackAnimation = 16;
            }
            else {
                if (phys) {
                    damageType = 1;
                    attackAnimation = 12;
                } else {
                    damageType = 2;
                    attackAnimation = 0;
                }
            }

            target = null;
            targetRot = rot;
            dealDamage = false;

            if (getInventory().getIndexInMainHand() == 0 && isPeople && (projectile == null || projectile.getDamageType() == 2)) {
                if(phys) {
                    setCooldown(500);

                    setPhysDamage(defaultPhysDamage);

                    setCritChance(0.2f);

                    setDealtStanTime(500);

                    setDropDist(5);

                    setAttackDist(10);

                    setAttackRange(30);
                }
                else{
                    setCooldown(700);

                    setCritChance(0.2f);

                    if(getAttackDist() != 50) {
                        setAttackDist(160);
                        if (this instanceof SkeletonMage) {
                            setAttackDist(150);
                        }
                    }

                    setDropDist(5);

                    setAttackRange(60);
                }
            }

            setRot(Math.abs(targetRot) <= 45 ? 3 : targetRot > 45 && targetRot <= 135 ? 0 : Math.abs(targetRot) > 135 ? 1 : 2);

            updateTimer();
        }
    }

    private Creature enemy;

    public Creature getEnemy() {
        return enemy;
    }

    public void setEnemy(Creature cr){
        enemy = cr;
    }

    private float dropDist = 5;

    public float getDropDist() {
        return dropDist;
    }

    public void setDropDist(float dropDist) {
        this.dropDist = dropDist;
    }

    private boolean withFreezing = false;

    public void setWithFreezing(boolean withFreezing) {
        this.withFreezing = withFreezing;
    }

    public boolean isWithFreezing() {
        return withFreezing;
    }

    public void dealDamageWithDelay(float damage, Creature cr, int type, boolean withoutEvade){
        if(!inStan() && cr != null && (!cr.evade(this) || withoutEvade) && damage > 0) {
            if(cr.block && getRotDelta(getOppositeRot(getRotTo(cr)), cr.creatureRot) < cr.getInventory().getItems()[5].getRange()){
                cr.getInventory().getItems()[5].setEndurance(cr.getInventory().getItems()[5].getEndurance() - (int) damage);

                if(cr.currentBlockArmor > damage){
                    cr.currentBlockArmor -= damage;
                    damage = 0;
                }
                else{
                    damage -= cr.currentBlockArmor;
                    cr.currentBlockArmor = 0;
                }
            }

            if(damage > 0) {
                for(DealtEffect dealtEffect : dealtEffects){
                    dealtEffect.setDealtTime(TimeUtils.millis());

                    if(dealtEffect.getType() >= 0)
                        cr.effects.add(new DealtEffect(dealtEffect.getDealtTime()
                                , dealtEffect.getType()));
                }

                float prevHp = cr.getHp();
                if (type == 1) {
                    int armorIndex = getArmorIndex();
                    float armor = cr.getInventory().getItems()[armorIndex] != null ? cr.getInventory().getItems()[armorIndex].getArmor() : 0;
                    cr.hp -= Math.max(0, damage - armor) * getCritCof();

                    if (armor != 0)
                        cr.getInventory().getItems()[armorIndex].setEndurance(cr.getInventory().getItems()[armorIndex].getEndurance() - (int) damage);

                    if (this instanceof Rabbit && ((Rabbit) this).isShadow()) {
                        float rot = getRotTo(cr);
                        rot += 30;

                        setPosition(cr.getCentralX() + (float) (Math.cos(Math.toRadians(rot)) * ((cr.getRect().getWidth() + getRect().getWidth()) / 2)),
                                cr.getRectCenterY() + (float) (Math.sin(Math.toRadians(rot)) * ((cr.getRect().getHeight() + getRect().getHeight()) / 2)));
                        setZ(cr.getZ());
                    }
                } else {
                    int armorIndex = getArmorIndex();
                    float armor = cr.getInventory().getItems()[armorIndex] != null ? cr.getInventory().getItems()[armorIndex].getMagArmor() : 0;
                    cr.hp -= Math.max(0, damage - armor) * getCritCof();

                    if (armor != 0)
                        cr.getInventory().getItems()[armorIndex].setEndurance(cr.getInventory().getItems()[armorIndex].getEndurance() - (int) damage);
                }


                if (this instanceof Mage && cr instanceof Player && cr.getHp() <= 0) {
                    setEnemy(null);

                    setHp(getMaxHp());
                    cr.setHp(prevHp);
                }
                else if (this instanceof Villager && ((Villager) this).isKnight() && cr instanceof Player && cr.getHp() <= 0) {
                    setEnemy(null);

                    setHp(getMaxHp());
                    cr.setHp(prevHp);

                    ((Villager) this).setPhraseNum(3);

                    GameScreen.dialogReader.setTalker((Villager) this);
                } else if (this instanceof Rabbit && ((Rabbit) this).isShadow() && cr instanceof Player && cr.getHp() <= 0) {
                    setEnemy(null);

                    cr.setHp(prevHp);

                    ((Rabbit) this).setPhraseNum(1);
                    GameScreen.dialogReader.setTalker((Rabbit) this);

                    ((Rabbit) this).giveGift(getHp());
                }

                if (this instanceof Rabbit && ((Rabbit) this).isShadow() && cr.getHp() <= 0) {
                    setEnemy(null);

                    cr.setHp(prevHp);
                }

                float rot = getRotTo(cr);
                if (cr.getStage() != null)
                    cr.move((float) (dropDist * Math.cos(Math.toRadians(rot))), (float) (dropDist * Math.sin(Math.toRadians(rot))), 0, false, true);

                cr.setLastTimeDealStan(TimeUtils.millis());
                boolean dealFreeze = withFreezing && Math.random() <= 0.3;
                cr.setStanTime(getDealtStanTime() + (long)(dealFreeze ? 1000 : 0), dealFreeze);

                cr.enemy = this;

                if (this instanceof Player) {
                    GameScreen.player.setEnemy(cr);
                    if (cr instanceof Bug) {
                        Bug.colonyThinking -= 20;
                    }
                }
            }
        }
        else if(!inStan() && cr != null && damage < 0){
            cr.hp -= damage;
        }
    }

    public void dealDamageWithDelay(float damage, Creature cr, int type, boolean withoutEvade, MyActor damageDealer){
        if(!inStan() && cr != null && (!cr.evade(this) || withoutEvade) && damage > 0) {
            if(cr.block && getRotDelta(getOppositeRot(getRotTo(cr)), cr.creatureRot) < cr.getInventory().getItems()[5].getRange()){
                cr.getInventory().getItems()[5].setEndurance(cr.getInventory().getItems()[5].getEndurance() - (int) damage);

                if(cr.currentBlockArmor > damage){
                    cr.currentBlockArmor -= damage;
                    damage = 0;
                }
                else{
                    damage -= cr.currentBlockArmor;
                    cr.currentBlockArmor = 0;
                }
            }

            if(damage > 0) {
                for(DealtEffect dealtEffect : dealtEffects){
                    dealtEffect.setDealtTime(TimeUtils.millis());

                    if(dealtEffect.getType() >= 0)
                        cr.effects.add(new DealtEffect(dealtEffect.getDealtTime()
                        , dealtEffect.getType()));
                }

                float prevHp = cr.getHp();
                if (type == 1) {
                    int armorIndex = getArmorIndex();
                    float armor = cr.getInventory().getItems()[armorIndex] != null ? cr.getInventory().getItems()[armorIndex].getArmor() : 0;
                    cr.hp -= Math.max(0, damage - armor) * getCritCof();

                    if (armor != 0)
                        cr.getInventory().getItems()[armorIndex].setEndurance(cr.getInventory().getItems()[armorIndex].getEndurance() - (int) damage);

                    if (this instanceof Rabbit && ((Rabbit) this).isShadow()) {
                        float rot = getRotTo(cr);
                        rot += 30;

                        setPosition(cr.getCentralX() + (float) (Math.cos(Math.toRadians(rot)) * ((cr.getRect().getWidth() + getRect().getWidth()) / 2)),
                                cr.getRectCenterY() + (float) (Math.sin(Math.toRadians(rot)) * ((cr.getRect().getHeight() + getRect().getHeight()) / 2)));
                        setZ(cr.getZ());
                    }
                } else {
                    int armorIndex = getArmorIndex();
                    float armor = cr.getInventory().getItems()[armorIndex] != null ? cr.getInventory().getItems()[armorIndex].getMagArmor() : 0;
                    cr.hp -= Math.max(0, damage - armor) * getCritCof();

                    if (armor != 0)
                        cr.getInventory().getItems()[armorIndex].setEndurance(cr.getInventory().getItems()[armorIndex].getEndurance() - (int) damage);
                }


                if (this instanceof Villager && ((Villager) this).isKnight() && cr instanceof Player && cr.getHp() <= 0) {
                    setEnemy(null);

                    setHp(getMaxHp());
                    cr.setHp(prevHp);

                    ((Villager) this).setPhraseNum(3);

                    GameScreen.dialogReader.setTalker((Villager) this);
                } else if (this instanceof Rabbit && ((Rabbit) this).isShadow() && cr instanceof Player && cr.getHp() <= 0) {
                    setEnemy(null);

                    cr.setHp(prevHp);

                    ((Rabbit) this).setPhraseNum(1);
                    GameScreen.dialogReader.setTalker((Rabbit) this);

                    ((Rabbit) this).giveGift(getHp());
                }

                if (this instanceof Rabbit && ((Rabbit) this).isShadow() && cr.getHp() <= 0) {
                    setEnemy(null);

                    cr.setHp(prevHp);
                }

                float rot = getRotTo(damageDealer);
                if (damageDealer.getStage() != null)
                    cr.move((float) (dropDist * Math.cos(Math.toRadians(rot))), (float) (dropDist * Math.sin(Math.toRadians(rot))), 0, false, true);

                cr.setLastTimeDealStan(TimeUtils.millis());
                boolean dealFreeze = withFreezing && Math.random() <= 0.3;
                cr.setStanTime(getDealtStanTime() + (long)(dealFreeze ? 1000 : 0), dealFreeze);

                cr.enemy = this;

                if (this instanceof Player) {
                    GameScreen.player.setEnemy(cr);
                    if (cr instanceof Bug) {
                        Bug.colonyThinking -= 20;
                    }
                }
            }
        }
        else if(!inStan() && cr != null && damage < 0){
            cr.hp -= damage;
        }
    }

    public void dealDamage(float damage, long stanTime, Creature cr){
        cr.hp -= damage;
        if(stanTime != 0) {
            cr.setLastTimeDealStan(TimeUtils.millis());
            cr.setStanTime(stanTime, false);
        }
    }

    public long getLastTimeDealStan() {
        return lastTimeDealStan;
    }

    public void setLastTimeDealStan(long lastTimeDealStan) {
        this.lastTimeDealStan = lastTimeDealStan;
    }

    public long getStanTime() {
        return stanTime;
    }

    public void setStanTime(long stanTime, boolean freeze) {
        this.freeze = freeze;
        this.stanTime = stanTime;
    }

    public int getDealtStanTime() {
        return dealtStanTime;
    }

    public void setDealtStanTime(int dealtStanTime) {
        this.dealtStanTime = dealtStanTime;
    }

    private float critCof = 1;

    public void setCritCof(float critCof) {
        this.critCof = critCof;
    }

    public float getCritCof1(){
        return critCof;
    }

    public float getCritChance() {
        return critChance * getCritCof1();
    }

    public void setCritChance(float critChance) {
        this.critChance = critChance;
    }

    private float getCritCof(){
        return Math.random() <= getCritChance() ? 1.5f : 1;
    }

    public void setAttackDist(float attackDist) {
        this.attackDist = attackDist;
    }

    public float getAttackDist() {
        return attackDist;
    }

    private boolean evade(Creature cr){
        return !inStan() && Math.random() <= Math.max(0, getAgility() - cr.getAgility()) / getAgility();
    }

    public float getAgility() {
        return agility;
    }

    public void setAgility(float agility) {
        this.agility = agility;
    }

    public void setMaxAgility(float maxAgility) {
        this.maxAgility = maxAgility;
        agility = maxAgility;
    }

    private int prevHp = 0;
    private void updateHpBar(){
        if((int)(hp * 40 / maxHp) != prevHp) {
            prevHp = (int) (hp * 40 / maxHp);
            hpBar = generateBar(hpBar, hp, maxHp, Color.GREEN);
        }
    }

    private Texture waterBar;
    private Texture satietyBar;
    private int prevWater = 0;
    public void updateWaterBar(int water1){
        water += water1;
        water = Math.min(water, maxWater);

        if((int)((float) water * 40 / maxWater) != prevWater) {
            prevWater = (int) ((float) water * 40 / maxWater);
            waterBar = generateBar(waterBar, water, maxWater, Color.CYAN);
        }
    }

    private int prevSat = 0;
    private void updateSatietyBar(int satiety1){
        satiety += satiety1;
        satiety = Math.min(satiety, maxSatiety);

        if((int)((float)satiety * 40 / maxSatiety) != prevSat) {
            prevSat = (int)((float) satiety * 40 / maxSatiety);
            satietyBar = generateBar(satietyBar, satiety, maxSatiety, Color.ORANGE);
        }
    }

    private final Pixmap pm = new Pixmap(40, 4, Pixmap.Format.RGBA4444);
    private Texture generateBar(Texture texture, float value1, float value2, Color color){
        pm.setColor(Color.CLEAR);
        pm.fill();

        pm.setColor(color);
        pm.fillRectangle(0, 0, (int) (40 * value1 / value2), 4);
        pm.setColor(Color.BLACK);
        pm.drawRectangle(0, 0, 40, 4);

        if(texture != null) texture.dispose();
        texture = new Texture(pm);

        return texture;
    }

    private boolean staticObj = false;
    private float transparency = 1;
    public final int maxAir = 1000;
    public int air = maxAir;
    private Texture airBar;
    private Texture magicPowerBar;
    public void updateAnima(){
        getTexture().setRegion(startX + ((int) ((int) animation / animaSpeed)) * getPicWidth(), startY + getPicHeight() * (action + (!isPeople ? 0 : rot)), getPicWidth(), getPicHeight());
    }
    @Override
    public void draw (Batch batch, float parentAlpha) {
        if (transparency > 0 && getTexture() != null) {
            if (isLookInv() && !isStaticObj()) {
                rot = 2;
            }

            float underWaterZ = 0;
            float waterLevel = 0;
            Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
            Color color = getColor().cpy();
            if (cell != null) {
                if (color.r == 1 && color.g == 1 && color.b == 1)
                    color = cell.getColor();

                if (getZ() < cell.getZ(getCentralX(), getRectCenterY()) + cell.getWaterLevel()) {
                    underWaterZ = getZ() - cell.getZ(getCentralX(), getRectCenterY()) - cell.getWaterLevel();
                    waterLevel = cell.getZ(getCentralX(), getRectCenterY()) + cell.getWaterLevel();
                } else if (getZ() < cell.getNormalZ()) {
                    underWaterZ = getZ() - cell.getNormalZ();
                    waterLevel = cell.getNormalZ();
                }
            }

            if(freeze && inStan()) {
                underWaterZ = -getPicHeight();
            }

            if (!isStaticObj()) {
                getTexture().setRegion(startX + ((int) ((int) animation / animaSpeed)) * getPicWidth(), startY + getPicHeight() * (action + (!isPeople ? 0 : rot)), getPicWidth(), getPicHeight());

                if (underWaterZ < 0 && cell != null) {
                    Iterator<DealtEffect> it = effects.iterator();
                    while(it.hasNext()) {
                        DealtEffect dealtEffect = it.next();
                        if(dealtEffect.getType() == 1)
                            it.remove();
                    }

                    Color water = new Color(0, 0.5f, 0.5f, 1);
                    water.lerp(cell.getColor(), 1 - cell.getColor().r);
                    batch.setColor(water);
                    batch.draw(getTexture(), getX(), getY() + getZ(), getOriginX(), getOriginY(),
                            getPicWidth(), getPicHeight(), getScaleX(), getScaleY(), getRotation());

                    getTexture().setRegion(startX + ((int) ((int) animation / animaSpeed)) * getPicWidth(), startY + getPicHeight() * (action + (!isPeople ? 0 : rot)), getPicWidth(), Math.max(0, getPicHeight() + (int) Math.min(underWaterZ, 0)));
                }
            }

            batch.setColor(color.r, color.g, color.b, transparency);
            batch.draw(getTexture(), getX(), getY() + (underWaterZ < 0 ? waterLevel : getZ()), getOriginX(), getOriginY(),
                    getPicWidth(), !isStaticObj() ? Math.max(0, getPicHeight() + (int) underWaterZ) : getHeight(), getScaleX(), getScaleY(), getRotation());

            if (!isStaticObj())
                for (int i = 0; i < 6; i++) {
                    Item item;
                    if (i < 4) {
                        item = getInventory().getItems()[i + 1];
                    } else if (i == 4) {
                        item = getInventory().getItems()[5];
                    } else {
                        item = getInventory().getItems()[0];
                    }
                    if (item != null) {
                        item.getTexture().setRegion(((int) ((int) animation / animaSpeed)) * item.getTexture().getRegionWidth(), GameScreen.picHeight * (action + rot), item.getTexture().getRegionWidth(), getPicHeight());

                        if (underWaterZ < 0) {


                            Color water = new Color(0, 0.5f, 0.5f, 1);
                            water.lerp(cell.getColor(), 1 - cell.getColor().r);
                            water.lerp(item.getColor(), 0.5f);
                            batch.setColor(water);
                            batch.draw(item.getTexture(), getX() + 1, getY() + getZ());

                            item.getTexture().setRegion(((int) ((int) animation / animaSpeed)) * item.getTexture().getRegionWidth(), GameScreen.picHeight * (action + rot), item.getTexture().getRegionWidth(), Math.max(0, GameScreen.picHeight + (int) Math.min(underWaterZ, 0)));
                        }

                        batch.setColor(item.getColor());
                        batch.draw(item.getTexture(), getX() + 1, getY() + (underWaterZ < 0 ? waterLevel : getZ()));
                    }
                }

            batch.setColor(color.r, color.g, color.b, transparency);
            if (!isStaticObj()) {
                if (getMaxHp() > 0) {
                    updateHpBar();

                    batch.draw(hpBar, getCentralX() - 20, getY() + getZ() + getPicHeight());
                }
                if (getMaxWater() > 0) {
                    if (waterBar == null)
                        updateWaterBar(0);

                    batch.draw(waterBar, getCentralX() - 20, getY() + getZ() - 5 + getPicHeight());
                }
                if (getMaxSatiety() > 0) {
                    if (satietyBar == null)
                        updateSatietyBar(0);

                    batch.draw(satietyBar, getCentralX() - 20, getY() + getZ() - 10 + getPicHeight());
                }

                if (air < maxAir) {
                    airBar = generateBar(airBar, air, maxAir, new Color(0.9f, 0.9f, 0.9f, 1));
                    batch.draw(airBar, getCentralX() - 20, getY() + getZ() - 20 + getPicHeight());
                }

                if (getMagicPower() < getMaxMagicPower()) {
                    magicPowerBar = generateBar(magicPowerBar, getMagicPower(), getMaxMagicPower(), new Color(159f / 255, 143f / 255, 214f / 255, 1));
                    batch.draw(magicPowerBar, getCentralX() - 20, getY() + getZ() - 15 + getPicHeight());
                }
            }
        } else if (getStage() != null) {
            for (int i = 0; i < getInventory().getAllItems().size() + 6; i++) {
                if (getInventory().getItem(i - 6) != null) {
                    Item item = getInventory().getItem(i - 6);
                    FallenItem fallenItem = new FallenItem(item);
                    fallenItem.setX(getRect().getX() + (float) (getRect().getWidth() * Math.random()));
                    fallenItem.setY(getRect().getY() + (float) (getRect().getHeight() * Math.random()));
                    getStage().addActor(fallenItem);

                    fallenItem.sort();
                }
            }
            if(magicPowerBar != null)
                magicPowerBar.dispose();
            if(airBar != null)
                airBar.dispose();
            if(satietyBar != null)
                satietyBar.dispose();
            if(waterBar != null)
                waterBar.dispose();
            if(hpBar != null)
                hpBar.dispose();

            remove();
        }

        for (MyActor act : drawAfter) {
            act.setVisible(true);
            act.draw(batch, parentAlpha);
        }
        drawAfter.clear();
    }

    public boolean isStaticObj() {
        return staticObj;
    }

    public void setStaticObj(boolean staticObj) {
        this.staticObj = staticObj;
    }

    public void dealMagDamage(float damage, Creature cr){
        cr.hp -= Math.max(0, damage - cr.getMagArmor()) * getCritCof();
        cr.updateHpBar();
    }

    public void updateTimer(){
        lastTimeDealDamage = TimeUtils.millis();
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        defaultMass = mass;
    }

    public void setRealMass(float mass){
        this.mass = mass;
    }

    public float getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(float maxHp, boolean regenerateHp) {
        this.maxHp = maxHp;
        if(regenerateHp){
            hp = maxHp;
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
