package com.mygdx.game.objects.characters.neutral;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.characters.Talker;
import com.mygdx.game.objects.characters.withInventory.Interior;
import com.mygdx.game.objects.decor.obs.CaveWall;
import com.mygdx.game.objects.decor.nature.Tree;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class Villager extends Talker {
    private final String picName;
    private boolean knight = false;
    private boolean mage = false;
    private boolean elf = false;

    private float homeX;
    private float homeY;

    public boolean isElf() {
        return elf;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
        setTeam(follow ? 1 : 0);
    }

    public Villager(boolean mage){
        super(new Rectangle3D(new Rectangle(0, 0, 20, 10), 0, 50));
        getRect().setCircle(true);

        boolean female = false;
        String picName = "characters\\villager2.png";
        if(!GameScreen.hasKnight){
            picName = "characters\\villager.png";
            GameScreen.hasKnight = true;
            knight = true;
        }
        else if(mage){
            picName = "characters\\villager.png";
            this.mage = true;
        }
        else if(!GameScreen.hasElf){
            picName = "characters\\elf.png";
            female = true;
            elf = true;

            GameScreen.hasElf = true;
        }
        else if(Math.random() < 0.2){
            picName = "characters\\villager.png";
        }
        else if(Math.random() <= 0.3){
            picName = "characters\\sara.png";
            female = true;
        }

        setCanOpenDoor(true);

        setTexture(GameScreen.loadRegion(picName, 0, 64 * 8, 64, 64));

        setSpeed(2);

        setMass(80);

        setCooldown(500);

        setCritChance(0.2f);

        setRectDelta(3);

        setMaxHp(20, true);

        setCanLookInv(!knight && !mage && !elf);

        setMaxAgility(15);

        if(mage){
            setMP(1200);

            setMagDamage(4);

            setAttackDist(160);

            Item item = new Item(65);
            getInventory().getItems()[1] = item;
        }
        else if(elf){
            setCooldown(700);

            setMagDamage(-1);

            setAttackDist(50);

            setAttackRange(60);

            setMagicCost(10);
        }
        else if(knight){
            Item item = new Item(1);
            getInventory().getItems()[0] = item;

            setMaxAgility(25);

            Item item2 = new Item(62);
            getInventory().getItems()[2] = item2;
            Item item3 = new Item(63);
            getInventory().getItems()[3] = item3;
            Item item4 = new Item(64);
            getInventory().getItems()[4] = item4;
        }
        else {
            Item item = null;
            if (Math.random() < 0.5 && !female) {
                item = new Item(2);
            } else if (Math.random() < 0.5 && !female) {
                item = new Item(3);
            }
            else if(picName.equals("characters\\villager2.png")){
                item = new Item(2);
            }

            getInventory().getItems()[0] = item;
        }

        if(Math.random() < 0.2){
            Item item2 = new Item((int)(97.3 + Math.random()));
            getInventory().add(item2, this);
        }

        if(mage) {
            phrases.add("If you found me, then you are a strong warrior, but\nyour skills are enough for now only for the simplest spells.");
            phrases.add("I'll give you the energy, but you'll use the spell\nyourself. The wizard conjures with spirit, not with\nartifacts, remember this!");
            phrases.add("Now imagine the place in front of you. Focus on that\nthought! And try to imagine yourself in this new place.");
            phrases.add("Practice your skill!");
            phrases.add("Perfect, now I'm going to teach you how to take a fight.\nHere it will be necessary to make certain movements.");
            phrases.add("Spread your arms, then gather the energy and materialize\nthe shard. Now you can drop it. However, it can\nseriously hurt your opponent, so use this spell wisely!");
            phrases.add("You are already ready for battle! Rumor has it that there\nis a cave in the Northwest from which the animated\nskeletons come out. I don't believe it, but it's worth\nchecking out, can you? This will help you.");
        }
        else if(knight){
            phrases.add("They must have let it slip to you that I know an old\nwizard?");
            phrases.add("Anyway, I'm ready to tell you where to find him, but\nfirst you have to beat me in a fair fight.");
            phrases.add("New match?");
            phrases.add("Not enough power.");
            phrases.add("Not bad! Now I see a worthy warrior! Go to the Southeast\nand you will find what you are looking for.");
        }
        else if(elf){
            phrases.add("I see you're going on a trip. They say there are many\ndangers in the world, but together we can overcome everything.");
        }
        else{
            phrases.add("Hello! What a good day!");
            phrases.add("Hello! How are you?");
            phrases.add("What a beautiful sky!");
            phrases.add("I like this weather!");
        }

        this.picName = picName;
    }

    public Villager(boolean knight, boolean mage, String picName){
        super(new Rectangle3D(new Rectangle(0, 0, 20, 10), 0, 50));
        getRect().setCircle(true);

        setCanOpenDoor(true);

        setTexture(GameScreen.loadRegion(picName, 0, 64 * 8, 64, 64));

        setSpeed(2);

        setMass(80);

        setCooldown(500);

        setCritChance(0.2f);

        setRectDelta(3);

        setMaxHp(20, true);

        elf = picName.equals("characters\\elf.png");

        setCanLookInv(!knight && !mage);

        setMaxAgility(15);

        if(mage){
            setMP(1200);

            setMagDamage(4);

            setAttackDist(160);

            Item item = new Item(65);
            getInventory().getItems()[1] = item;

            this.mage = mage;
        }
        else if(elf){
            setCooldown(700);

            setMagDamage(-1);

            setAttackDist(50);

            setAttackRange(60);

            setMagicCost(10);
        }
        else if(knight){
            setMaxAgility(25);

            this.knight = knight;
        }

        if(mage) {
            phrases.add("If you found me, then you are a strong warrior, but\nyour skills are enough for now only for the simplest spells.");
            phrases.add("I'll give you the energy, but you'll use the spell\nyourself. The wizard conjures with spirit, not with\nartifacts, remember this!");
            phrases.add("Now imagine the place in front of you. Focus on that\nthought! And try to imagine yourself in this new place.");
            phrases.add("Practice your skill!");
            phrases.add("Perfect, now I'm going to teach you how to take a fight.\nHere it will be necessary to make certain movements.");
            phrases.add("Spread your arms, then gather the energy and materialize\nthe shard. Now you can drop it. However, it can\nseriously hurt your opponent, so use this spell wisely!");
            phrases.add("You are already ready for battle! Rumor has it that there\nis a cave in the Northwest from which the animated\nskeletons come out. I don't believe it, but it's worth\nchecking out, can you? This will help you.");
        }
        else if(knight){
            phrases.add("They must have let it slip to you that I know an old\nwizard?");
            phrases.add("Anyway, I'm ready to tell you where to find him, but\nfirst you have to beat me in a fair fight.");
            phrases.add("New match?");
            phrases.add("Not enough power.");
            phrases.add("Not bad! Now I see a worthy warrior! Go to the Southeast\nand you will find what you are looking for.");
        }
        else if(elf){
            phrases.add("I see you're going on a trip. They say there are many\ndangers in the world, but together we can overcome everything.");
        }
        else{
            phrases.add("Hello! What a good day!");
            phrases.add("Hello! How are you?");
            phrases.add("What a beautiful sky!");
            phrases.add("I like this weather!");
        }

        this.picName = picName;
    }

    public String getPicName() {
        return picName;
    }

    private float targetX;
    private float targetY;

    private static Creature mainEnemy = null;
    public boolean addMage = true;

    private long lastTimeGetSpell = 0;
    private final int timeDelta = 30000;

    private boolean start = true;

    private MyActor targetObject;

    public boolean isKnight() {
        return knight;
    }

    @Override
    public void act(float delta) {
        if(start){
            MyActor inter = getObjectInRange(500, Interior.class);
            if(inter != null){
                homeX = inter.getCentralX() - 32;
                homeY = inter.getY() + 64;
            }

            start = false;
        }
        if(elf && GameScreen.player.getMagDamage() > 0){
            setCanLookInv(false);
        }
        if(!isLookInv()) {
            if(elf){
                if(!follow || GameScreen.player.getHp() < getMaxHp()){
                    Creature creature = (Creature) getNearestObjectInRange(300, Creature.class);

                    if(creature != null && !creature.isStaticObj() && getEnemy() == null &&
                    creature.getHp() < creature.getMaxHp()){
                        targetObject = creature;
                    }
                    else{
                        targetObject = null;
                    }
                }
                else{
                    targetObject = GameScreen.player;
                }
            }
            else {
                if(mainEnemy instanceof Villager || mainEnemy != null && mainEnemy.getStage() == null)
                    mainEnemy = null;

                if(getEnemy() != null && getEnemy().getStage() == null){
                    setEnemy(null);
                }
                if (getEnemy() instanceof Villager) {
                    setEnemy(null);
                }
                if (getEnemy() != null) {
                    targetObject = getEnemy();

                    if (!knight && !mage)
                        mainEnemy = getEnemy();
                } else if (mainEnemy != null) {
                    targetObject = mainEnemy;

                    setEnemy(mainEnemy);
                } else if (GameScreen.darkCof > 0.3) {
                    targetX = homeX;
                    targetY = homeY - 50;
                } else if (getInventory().getItemsSize() < getInventory().getInventorySize() / 2 && !knight && !mage) {
                    targetObject = null;
                    if (getInventory().getIndexInMainHand() == 0) {
                        targetObject = getNearestObjectInRange(300, Tree.class);
                        if (targetObject != null && ((Tree) targetObject).getTreeType() != 1) {
                            targetObject = null;
                        }
                    } else if (getInventory().getIndexInMainHand() == 2) {
                        targetObject = getNearestObjectInRange(300, Tree.class);
                    } else if (getInventory().getIndexInMainHand() == 3) {
                        targetObject = getNearestObjectInRange(300, CaveWall.class);
                    }
                } else {
                    targetObject = null;
                }

                if(knight) {
                    if (getHp() <= getMaxHp() / 4) {
                        setHp(getMaxHp() / 4);
                        setEnemy(null);

                        if(addMage){
                            Villager mage = new Villager(true);
                            mage.setPosition(55 * 32 - 16, 15 * 32);
                            getStage().addActor(mage);
                            addMage = false;

                            phraseNum = 4;
                            GameScreen.dialogReader.setTalker(this);
                        }
                    } else if (getHp() <= getMaxHp() * 2 / 3) {
                        setAttackSpeedBoost(1.3f);
                    }
                }
            }

            if (targetObject != null) {
                targetX = targetObject.getCentralX();
                targetY = targetObject.getRectCenterY();
            }
            else if(elf && getEnemy() != null){
                float targetRot = getOppositeRot(getRotTo(getEnemy()));
                targetX = getX() + (float) (Math.cos(Math.toRadians(targetRot)) * 64);
                targetY = getY() + (float) (Math.sin(Math.toRadians(targetRot)) * 64);
            }
            else if(GameScreen.darkCof <= 0.3){
                randomMove();
            }

            if(getDist(targetX, targetY) >= 50 || !elf)
                moveToCords(targetX, targetY);

            if(!cooldown()) {
                if (!elf) {
                    if (getEnemy() != null) {
                        if (checkObject(getEnemy()) && (!mage || getMagicPower() > 90)) {
                            dealDamage(!mage, getEnemy());
                        }
                    } else if (targetObject != null) {
                        if (checkObject(targetObject)) {
                            if (targetObject instanceof Creature && (!mage || getMagicPower() > 90))
                                dealDamage(!mage, (Creature) targetObject);
                            else if((!mage || getMagicPower() > 60))
                                dealDamage(!mage, getRotTo(targetObject));
                        }
                    }
                } else {
                    if (targetObject != null && (!(targetObject instanceof Creature) || ((Creature) targetObject).getHp() < ((Creature) targetObject).getMaxHp())) {
                        if (checkObject(targetObject) && getMagicPower() > 20) {
                            dealDamage(false, (Creature) targetObject);
                        }
                    }
                }
            }

            if(mage)
                setUpdateAnimation(!cooldown());

            if(!knight && !mage && getInventory().getAllItems().size() > 0 && Math.random() < 0.0005){
                int index = (int)(Math.random() * getInventory().getAllItems().size());
                getInventory().getAllItems().remove(index);
            }

            animate();
        }

        setLookInv(false);
    }

    private void randomMove(){
        if (targetX == 0 && targetY == 0 || getDist(targetX, targetY) < 50) {
            targetX = getCentralX() + (float) (Math.random() * 32 * 10) - 32 * 5;
            targetY = getY() + (float) (Math.random() * 32 * 10) - 32 * 5;

            targetX = Math.min(32 * 69, Math.max(16, targetX));
            targetY = Math.min(32 * 69, Math.max(16, targetY));
        }
    }

    private boolean follow;
    public void updatePhraseNum(){
        if(mage){
            if(phraseNum + 1 < phrases.size() && (phraseNum < 3 ||
            TimeUtils.timeSinceMillis(lastTimeGetSpell) >= timeDelta)) {
                phraseNum++;
                if(phraseNum == 2){
                    lastTimeGetSpell = TimeUtils.millis();
                    GameScreen.player.setMagicType(1);

                    GameScreen.setHelpText("To dash use" + (!MyGdxGame.phone ? " E." : ""), 9);
                }
                else if(phraseNum == 5){
                    GameScreen.player.setMagDamage(4);

                    GameScreen.setHelpText("To deal magic damage use"  + (!MyGdxGame.phone ? " R." : 0), 10);
                }
                else if(phraseNum == 6){
                    Item item = new Item(65);
                    GameScreen.player.getInventory().add(item, GameScreen.player);
                }
            }
        }
        else if(knight){
            if(phraseNum < 2) {
                phraseNum++;
                if (phraseNum == 2) {
                    setEnemy(GameScreen.player);
                    setLookInv(false);

                    GameScreen.dialogReader.setTalker(null);
                }
            }
            else if(phraseNum == 2){
                setEnemy(GameScreen.player);
                setLookInv(false);

                GameScreen.dialogReader.setTalker(null);
            }
            else if(phraseNum == 3 && GameScreen.player.getHp() > 6){
                phraseNum = 2;
            }
        }
        else if(elf){
            if(phraseNum < phrases.size() - 1){
                phraseNum++;
            }
            if(phraseNum == 0){
                setFollow(true);
                setLookInv(false);

                GameScreen.dialogReader.setTalker(null);
            }
        }
        else{
            phraseNum = (int)(Math.random() * phrases.size());
        }
    }

    public String getPhrase(){
        return phrases.get(phraseNum);
    }
}
