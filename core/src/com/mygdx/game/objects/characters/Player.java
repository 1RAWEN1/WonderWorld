package com.mygdx.game.objects.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.building.BuildingObject;
import com.mygdx.game.objects.buttons.magicButtons.NumberButton;
import com.mygdx.game.objects.characters.withInventory.ObsWithInventory;
import com.mygdx.game.objects.characters.withInventory.Workbench;
import com.mygdx.game.objects.characters.effects.DealtEffect;
import com.mygdx.game.objects.characters.enemy.Bug;
import com.mygdx.game.objects.characters.neutral.Wolf;
import com.mygdx.game.objects.characters.peaceful.Rabbit;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.objects.inventory.Inventory;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.objects.inventory.Slot;
import com.mygdx.game.scenes.GameScreen;

public class Player extends Creature {
    public Player(){
        super(new TextureRegion(GameScreen.loadTexture("characters\\player.png"), 0, 64 * 8, 64, 64),
                new Rectangle3D(new Rectangle(0, 0, 20, 10), 0, 50));
        getRect().setCircle(true);

        setCanOpenDoor(true);

        setSpeed(3);

        setMass(80);

        setMaxHp(20, true);

        setCooldown(500);

        setRectDelta(3);

        setCritChance(0.2f);

        setMagicType(2);

        setMaxAgility(20);

        setMaxSatiety(30000);
        setMaxWater(30000);

        //setMagDamage(4);

        Item item = new Item(3);
        getInventory().getAllItems().add(item);
        /*Item item1 = new Item(100);
        getInventory().getAllItems().add(item1);
        Item item2 = new Item(91);
        getInventory().getAllItems().add(item2);
        Item item3 = new Item(91);
        getInventory().getAllItems().add(item3);*/

        /*Item item1 = new Item(1);
        getInventory().getItems()[0] = item1;

        Item item5 = new Item(65);
        getInventory().getItems()[1] = item5;
        Item item2 = new Item(66);
        getInventory().getItems()[2] = item2;
        Item item3 = new Item(67);
        getInventory().getItems()[3] = item3;*/
        //Item item4 = new Item(68);
        //().getItems()[4] = item4;
/*
        Item item6 = new Item(105);
        getInventory().add(item6, this);

        Item item7 = new Item(4);
        getInventory().add(item7, this);
        Item item8 = new Item(106);
        getInventory().add(item8, this);
        Item item9 = new Item(108);
        getInventory().add(item9, this);
        Item item10 = new Item(100);
        getInventory().add(item10, this);
        Item item11 = new Item(100);
        getInventory().add(item11, this);*/

        setTeam(1);
    }

    private boolean qPush = false;
    private boolean tabPush = false;
    private boolean seeInventory = false;
    private boolean seeBuilding = false;

    public void setSeeInventory(boolean seeInventory) {
        this.seeInventory = seeInventory;
    }

    public boolean isSeeInventory() {
        return seeInventory;
    }

    public boolean isSeeBuilding() {
        return seeBuilding;
    }

    public void setSeeBuilding(boolean seeBuilding) {
        this.seeBuilding = seeBuilding;
    }

    private int money;
    private float movingRot = -1;
    private float rot;

    public void setMovingRot(float movingRot) {
        this.movingRot = movingRot;
    }

    private boolean attack = false;

    public void setAttack(boolean attack) {
        this.attack = attack;
    }

    private boolean magic = false;

    public void setMagic(boolean magic) {
        this.magic = magic;
    }

    public float getDarkCof(){
        return GameScreen.getCellAt(getCentralX(), getRectCenterY()).getDarkCof();
    }

    public int start = 0;
    private int invMode = 0;
    private boolean def = false;
    private int materialMagicType = 0;

    public int isMaterialMagicType() {
        return materialMagicType;
    }

    public void setMaterialMagicType(int materialMagicType) {
        this.materialMagicType = materialMagicType;
    }

    public void setDef(boolean def) {
        this.def = def;
    }

    private boolean seeMagic = false;

    private boolean autoCast = false;

    public boolean isAutoCast() {
        return autoCast;
    }

    public void setAutoCast(boolean autoCast) {
        this.autoCast = autoCast;
    }

    public boolean isSeeMagic() {
        return seeMagic;
    }

    public void setSeeMagic(boolean seeMagic) {
        this.seeMagic = seeMagic;
    }

    @Override
    public void act(float delta) {
        if(start == 0 && !GameScreen.isHelpLabelVisible()){
            GameScreen.setHelpText("To move use" + (!MyGdxGame.phone ? " W, A, S, D." : ""), 1);
        }
        else if(start == 1 && !GameScreen.isHelpLabelVisible()){
            GameScreen.setHelpText("To jump use" + (!MyGdxGame.phone ? " Space." : ""), 4);
        }
        else if(start == 2 && !GameScreen.isHelpLabelVisible()){
            GameScreen.setHelpText("To attack use" + (!MyGdxGame.phone ? " Left mouse button." : ""), -1);
        }
        else if(start == 3 && !GameScreen.isHelpLabelVisible()){
            GameScreen.setHelpText("To interact with items or creatures use" + (!MyGdxGame.phone ? " F." : ""), 6);
        }

        if(GameScreen.isKeyDown(5) && !tabPush){
            seeInventory = !seeInventory;
            tabPush = true;

            invMode = 0;
        }
        else if((!GameScreen.isKeyDown(5) || GameScreen.isKeyDown(8)) && tabPush){
            tabPush = false;
        }

        Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
        float underWaterZ = 0;
        if(cell != null) {
            if(getZ() < cell.getZ(getCentralX(), getRectCenterY()) + cell.getWaterLevel()) {
                underWaterZ = getZ() - cell.getZ(getCentralX(), getRectCenterY()) - cell.getWaterLevel();
            }
            else if(getZ() < cell.getNormalZ()){
                underWaterZ = getZ() - cell.getNormalZ();
            }
        }

        if(seeInventory) {
            seeMagic = false;
            seeBuilding = false;
            if(invMode == 0) {
                ObsWithInventory obs = (ObsWithInventory) getNearestObject(ObsWithInventory.class);
                if (obs != null && getDist(obs) < 50) {
                    obs.setLookInv(true);
                    GameScreen.setVisible(true, getInventory().getAllItems().size(), obs.getInventory().getAllItems().size());
                    GameScreen.updateSlotPos(getX() - 200, getY() + getZ() - 150, false);
                    GameScreen.setItems(this, obs);
                } else {
                    Workbench.craftTable = w;

                    w.checkCrafts();
                    GameScreen.setVisible(true, getInventory().getAllItems().size(), (float)w.getInventory().getAllItems().size());
                    GameScreen.updateSlotPos(getX() - 200, getY() + getZ() - 150, true);
                    GameScreen.setItems(this, w);
                }

                if (Slot.firstSlot != null && Slot.firstSlot.getInv() == getInventory() && Slot.firstSlot.getIndex() >= 0 && (GameScreen.isKeyDown(6) || GameScreen.pressedKey == 6)) {
                    getInventory().dropItem(Slot.firstSlot.getIndex(), this);
                    Slot.firstSlot = null;
                }
            }
            else if(invMode == 1){
                Creature creature = (Creature) getNearestObject(Creature.class);

                if(creature != null && getDist(creature) < 50 && creature.canLookInv()) {
                    creature.setLookInv(true);
                    GameScreen.setVisible(true, getInventory().getAllItems().size(), creature.getInventory().getAllItems().size());
                    GameScreen.updateSlotPos(getX() - 200, getY() + getZ() - 150, false);
                    GameScreen.setItems(this, creature);

                    seeInventory = true;
                }
            }
        }
        else if(seeBuilding){
            GameScreen.setVisible(false);
            setSeeMagic(false);
        }
        else if(seeMagic){
            GameScreen.setMagicType();
        }
        else if(GameScreen.dialogReader.getTalker() == null){
            if (GameScreen.isKeyDown(3)) {
                movingRot = 180;
            } else if (GameScreen.isKeyDown(2)) {
                movingRot = 0;
            }
            if (GameScreen.isKeyDown(0)) {
                if(movingRot == -1){
                    movingRot = 90;
                }
                else{
                    movingRot = (movingRot + 90) / 2;
                }
            } else if (GameScreen.isKeyDown(1)) {
                if(movingRot == -1){
                    movingRot = -90;
                }
                else{
                    movingRot = -(movingRot + 90) / 2;
                }
            }

            if(movingRot != -1 && !block) {
                move(movingRot);
                rot = movingRot;
                movingRot = -1;
            }

            if(!inStan()) {
                int hasStone = getInventory().hasItem(108);
                if ((underWaterZ <= -getRect().getzSize() * 0.5 || hasStone != -1)
                        && (GameScreen.isKeyDown(4) || GameScreen.pressedKey == 4)) {
                    setZSpeed(2);
                } else if (onGround() && underWaterZ > -getRect().getzSize() * 0.5
                        && (GameScreen.isKeyDown(4) || GameScreen.pressedKey == 4)) {
                    setZSpeed(4);
                }
            }

            setTake(GameScreen.isKeyDown(6) || GameScreen.pressedKey == 6);
            if(isTake()){
                Creature creature = (Creature) getNearestObject(Creature.class);

                if(creature != null && getDist(creature) < 50 && creature.canLookInv()) {
                    seeInventory = true;
                    invMode = 1;
                }
                else if(creature instanceof Talker && (!(creature instanceof Rabbit) || ((Rabbit) creature).isShadow())){
                    GameScreen.setVisible(false);

                    GameScreen.dialogReader.setTalker((Talker)creature);

                    seeInventory = false;
                }
                else if(creature instanceof Bug){
                    GameScreen.setVisible(false);
                    seeInventory = false;
                    int meatIndex = getInventory().hasItem(99);
                    if(meatIndex != -1){
                        getInventory().getAllItems().remove(meatIndex);

                        Bug.colonyThinking += 2;
                    }
                }
                else if(creature instanceof Wolf && !((Wolf)creature).isTamed()){
                    GameScreen.setVisible(false);
                    seeInventory = false;
                    int meatIndex = getInventory().hasItem(95);
                    if(meatIndex != -1 && !((Wolf)creature).isTamed()){
                        getInventory().getAllItems().remove(meatIndex);

                        ((Wolf)creature).setTamed(true);
                    }
                }
                else if(creature instanceof BuildingObject){
                    GameScreen.setVisible(false);
                    seeInventory = false;

                    int[] materials = ((BuildingObject) creature).getMaterials();
                    for(int i = 0; i < materials.length; i += 2){
                        int itemIndex = getInventory().hasItem(materials[i + 1]);
                        if(itemIndex != -1 && materials[i] > 0){
                            getInventory().getAllItems().remove(itemIndex);

                            materials[i]--;

                            ((BuildingObject) creature).update(i);
                        }
                    }
                }
            }

            if(!GameScreen.touchButton() && GameScreen.buildingObject.isStatic()) {
                if (!cooldown() && (GameScreen.isKeyDown(11) || GameScreen.isKeyDown(10)) && !MyGdxGame.phone) {
                    this.rot = (float) (180 * Math.atan2(GameScreen.getMouseY() - 15 - getStage().getViewport().getWorldHeight() / 2, GameScreen.getMouseX() - getStage().getViewport().getWorldWidth() / 2) / Math.PI);

                    if (GameScreen.isKeyDown(11))
                        attack = true;
                    else
                        magic = true;
                }
            }

            block = false;

            if((GameScreen.isKeyDown(12) && !MyGdxGame.phone || def) && getInventory().getItems()[5] != null){
                attack = false;
                block = true;
                def = false;

                this.rot = (float) (180 * Math.atan2(GameScreen.getMouseY() - 15 - getStage().getViewport().getWorldHeight() / 2, GameScreen.getMouseX() - getStage().getViewport().getWorldWidth() / 2) / Math.PI);

                creatureRot = rot;

                currentBlockArmor = getInventory().getItems()[5].getArmor();
            }
            else if(attack){
                boolean shoot = true;
                Creature creature = (Creature) getNearestEnemy(Creature.class);
                if(getInventory().getIndexInMainHand() == 4){
                    int arrowIndex = getInventory().hasItem(106);
                    if(arrowIndex != -1) {
                        Projectile projectile = GameScreen.addProjectile(new TextureRegion(GameScreen.loadTexture("inventory\\arrow.png")), 10, getZ() + (float) (getRect().getzSize() * 0.7), 8, this, 1, 106, 1);
                        setProjectile(projectile);

                        if(!MyGdxGame.phone){
                            float xd = GameScreen.getMouseX() - getStage().getViewport().getWorldWidth() / 2;
                            float yd = GameScreen.getMouseY() - 15 - getStage().getViewport().getWorldHeight() / 2;
                            setNeedDist((float) Math.sqrt(Math.pow(xd, 2) + Math.pow(yd, 2)));
                        }
                    }
                    else{
                        shoot = false;
                    }
                }

                if(shoot) {
                    if (creature != null && !creature.isStaticObj() && MyGdxGame.phone)
                        dealDamage(true, creature);
                    else
                        dealDamage(true, rot);
                }

                attack = false;
            }
            else if(magic && getMagDamage() > 0 &&
            getInventory().getIndexInMainHand() == 0 && getInventory().getIndexInSecHand() == 0){
                setAttackDist(NumberButton.magicPar[2] * 10);
                Creature creature = (Creature) getNearestEnemy(Creature.class);
                Projectile projectile = null;
                if(isMaterialMagicType() < 2) {
                    int stacks = isMaterialMagicType() == 0 ? (int)NumberButton.magicPar[0] : -1;
                    setMagDamage(NumberButton.magicPar[1]);
                    projectile = GameScreen.addProjectile(new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 635, 465, 50, 50), NumberButton.magicPar[4], getZ() + (float) (getRect().getzSize() * 0.5), getMagDamage(), this, 2, 0, stacks);
                    //projectile.setColor(new Color(150f / 256, 0, 150f / 256, 1));
                    Color color = null;
                    for(DealtEffect dealtEffect : getDealtEffects()){
                        if(color == null){
                            color = dealtEffect.getColor().cpy();
                        }
                        else{
                            color.lerp(dealtEffect.getColor(), 0.5f);
                        }
                    }
                    if(isWithFreezing()){
                        if(color != null)
                            color.lerp(new Color(26f / 256, 175f / 256, 225f / 256, 1), 0.5f);
                        else
                            color = new Color(26f / 256, 175f / 256, 225f / 256, 1);
                    }
                    if(color == null){
                        color = new Color(220f / 256, 220f / 256, 220f / 256, 1);
                    }
                    projectile.setColor(color);
                    projectile.setScale(NumberButton.magicPar[3] / 50);
                    projectile.setAutoRotate(autoCast);
                    setProjectile(projectile);
                }

                if(!MyGdxGame.phone){
                    float xd = GameScreen.getMouseX() - getStage().getViewport().getWorldWidth() / 2;
                    float yd = GameScreen.getMouseY() - 15 - getStage().getViewport().getWorldHeight() / 2;
                    setNeedDist((float) Math.sqrt(Math.pow(xd, 2) + Math.pow(yd, 2)));
                }

                if(creature != null && !creature.isStaticObj() && MyGdxGame.phone)
                    dealDamage(false, creature);
                else
                    dealDamage(false, rot);

                setAttackDist(NumberButton.magicPar[2] * 10);

                if(projectile != null)
                    projectile.setMaxDist(getAttackDist() / 10);

                setProjNum((int)NumberButton.magicPar[5]);
                setProjAngle(NumberButton.magicPar[6]);

                magic = false;
            }

            Slot.firstSlot = null;
            Workbench.craftTable = null;

            GameScreen.setVisible(false);
        }

        if(GameScreen.dialogReader.getTalker() != null && GameScreen.isKeyDown(8) ||
                GameScreen.dialogReader.getTalker() != null && Gdx.input.isTouched() && GameScreen.getMouseY() > 250){
            GameScreen.dialogReader.getTalker().setLookInv(false);

            GameScreen.dialogReader.setTalker(null);
        }

        if(GameScreen.isKeyDown(9) || GameScreen.pressedKey == 9){
            if(!inStan()) {
                /*float rot;
                if (!GameScreen.phone) {
                    float touchX = GameScreen.getMouseX();
                    float touchY = GameScreen.getMouseY();
                    rot = (float) (180 * Math.atan2(touchY - getStage().getViewport().getWorldHeight() / 2, touchX - getStage().getViewport().getWorldWidth() / 2) / Math.PI);
                } else {
                    rot = this.rot;
                }*/

                castMagic(rot);
            }
        }

        /*double v = Math.pow(getX() - GameScreen.squareSize * GameScreen.caveCord, 2) + Math.pow(getY() - GameScreen.squareSize * GameScreen.caveCord, 2);
        if(v <= Math.pow(GameScreen.caveDist - 16, 2) && getZ() < 74 && GameScreen.getDarkCof() < 0.5){
            GameScreen.setDarkCof(GameScreen.getDarkCof() + 0.02f);
        }
        else if(GameScreen.getDarkCof() > 0){
            GameScreen.setDarkCof(GameScreen.getDarkCof() - 0.02f);
        }*/
        //!qPush && Gdx.input.isKeyPressed(GameScreen.buttons.get(7)) ||
        //!Gdx.input.isKeyPressed(GameScreen.buttons.get(7)) && qPush && !GameScreen.phone

        if(!qPush && Gdx.input.isTouched() && GameScreen.getMouseY() <= 250){
            if(GameScreen.dialogReader.getTalker() != null)
                GameScreen.dialogReader.getTalker().updatePhraseNum();
            qPush = true;
        }
        else if(qPush && !Gdx.input.isTouched())
            qPush = false;

        animate();

        GameScreen.pressedKey = -1;

        if(underWaterZ < -getRect().getzSize() * 0.8 && air > 0){
            air--;
        }
        else if(air < maxAir){
            air += 10;
            air = Math.min(air, maxAir);
        }

        if(air <= 0){
            dealDamage(0.5f, 0, this);
        }
    }

    private Workbench w = new Workbench(1);

    public void move(float rot){
        int n = move((float) (Math.cos(Math.toRadians(rot)) * getSpeed()), (float) (Math.sin(Math.toRadians(rot)) * getSpeed()), 0);
        if(n == 0){
            setMovingDist(getSpeed());
        }
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
