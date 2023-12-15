package com.mygdx.game.objects.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.objects.characters.Player;
import com.mygdx.game.objects.characters.neutral.Villager;
import com.mygdx.game.objects.characters.withInventory.Workbench;
import com.mygdx.game.scenes.GameScreen;

public class Slot extends Actor {
    private final Texture texture = GameScreen.loadTexture("inventory\\slot.png");
    private Item item;
    private WithInventory cr;

    private int index;

    public Slot(WithInventory cr){
        this.cr = cr;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static Slot firstSlot = null;
    private static Slot secondSlot;
    private boolean touched = false;

    @Override
    public void act(float delta) {
        if(isVisible() && Gdx.input.isTouched() && touchThis() && !touched && firstSlot != this){
            if(firstSlot == null && item != null) {
                firstSlot = this;
            }
            else if(firstSlot != null){
                secondSlot = this;
                replace();
            }

            touched = true;
        }
        else if(isVisible() && Gdx.input.isTouched() && touchThis() && !touched && item != null){
            if(getInv() == GameScreen.player.getInventory()){
                getItem().use(GameScreen.player);

                firstSlot = null;
            }

            touched = true;
        }
        else if(!isVisible() || !Gdx.input.isTouched() && touched){
            touched = false;
        }
    }

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX();
        float touchY = GameScreen.getMouseY();

        return touchX >= (getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2) && touchX <= getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2 + texture.getWidth()
                && touchY >= (getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2) && touchY <= getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2 + texture.getHeight();
    }

    public Slot getPlayerSlot(){
        if(firstSlot.getCreature() instanceof Villager && secondSlot.getCreature() instanceof Player ||
                secondSlot.getCreature() instanceof Villager && firstSlot.getCreature() instanceof Player)
            return firstSlot.getCreature() instanceof Player ? firstSlot : secondSlot;
        else
            return null;
    }

    public Slot getVillagerSlot(){
        if(firstSlot.getCreature() instanceof Villager && secondSlot.getCreature() instanceof Player ||
                secondSlot.getCreature() instanceof Villager && firstSlot.getCreature() instanceof Player)
            return firstSlot.getCreature() instanceof Player ? secondSlot : firstSlot;
        else
            return null;
    }

    private void replace(){
        boolean replace;
        if(getPlayerSlot() == null ||
        GameScreen.player.getMoney() + (int)(getPlayerSlot().getItem() != null ? getPlayerSlot().getItem().getPrice() * 0.7 : 0) >= (getVillagerSlot().getItem() != null ? getVillagerSlot().getItem().getPrice() : 0)) {
            if(getPlayerSlot() != null){
                GameScreen.player.setMoney(GameScreen.player.getMoney() - ((getVillagerSlot().getItem() != null ? getVillagerSlot().getItem().getPrice() : 0)
                        - (int)(getPlayerSlot().getItem() != null ? getPlayerSlot().getItem().getPrice() * 0.7 : 0)));
            }

            if (Workbench.craftTable == null)
                replace = secondSlot.getInv().setItem(secondSlot.getIndex(), firstSlot.getItem(), secondSlot.getItem() != null ? secondSlot.getItem().getItemSize() : 0);
            else {
                replace = secondSlot.getInv().setItem(secondSlot.getIndex(), firstSlot.getItem(), Workbench.craftTable.getItemDelta(firstSlot.getItem().getIndex()));
            }
            if (replace) {
                boolean r = !firstSlot.getInv().setItem(firstSlot.getIndex(), secondSlot.getItem(), firstSlot.getItem().getItemSize());
                if (r) {
                    secondSlot.getInv().setItem(secondSlot.getIndex(), secondSlot.getItem(), firstSlot.getItem().getItemSize());
                } else if (Workbench.craftTable != null && (secondSlot.getCreature() == Workbench.craftTable ||
                        firstSlot.getCreature() == Workbench.craftTable)) {
                    Workbench.craftTable.wasteResources(firstSlot.getItem().getIndex());
                }
            }

            firstSlot = null;
        }
    }

    public Slot(Item item){
        this.item = item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    Texture bar;

    private final Pixmap pm = new Pixmap((int)(texture.getWidth() * 0.8), 4, Pixmap.Format.RGBA4444);
    private void updateEnduranceBar(){
        pm.setColor(Color.CLEAR);
        pm.fill();

        pm.setColor(Color.GREEN);
        pm.fillRectangle(0, 0, (int) ((int)(texture.getWidth() * 0.8) * (float)item.getEndurance() / item.getMaxEndurance()), 4);
        pm.setColor(Color.BLACK);
        pm.drawRectangle(0, 0, (int)(texture.getWidth() * 0.8), 4);

        if(bar != null) bar.dispose();
        bar = new Texture(pm);
    }

    public WithInventory getCreature(){
        return cr;
    }

    public Inventory getInv() {
        return cr.getInventory();
    }

    public void setInv(WithInventory inv) {
        this.cr = inv;
    }

    public Item getItem() {
        return item;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        if (firstSlot == this) {
            color.set(1, 0.9f, 0, 1);
        } else {
            color.set(1, 1, 1, 1);
        }
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY());

        if (item != null) {
            batch.setColor(item.getColor());
            batch.draw(item.getItemTexture(), getX() - (item.getItemTexture().getRegionWidth() / 2) + (texture.getWidth() / 2),
                    getY() - (item.getItemTexture().getRegionHeight() / 2) + (texture.getHeight() / 2));

            if (item.getMaxEndurance() != 0) {
                updateEnduranceBar();
                batch.draw(bar, getX() + (int) (texture.getWidth() * 0.1), getY() + 10);
            }
        }
    }
}
