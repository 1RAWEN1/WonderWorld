package com.mygdx.game.objects.decor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.objects.characters.Talker;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class DialogReader extends Actor {
    private final Label label = new Label("", new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle(new BitmapFont(), Color.BLACK));
    private Talker talker;

    public DialogReader(){
        label.setFontScaleX(2f);
        label.setFontScaleY(2f);
    }

    public void setTalker(Talker talker) {
        this.talker = talker;
    }

    public void setText(String text){
        label.setText(text);
    }

    public Label getLabel() {
        return label;
    }

    public Talker getTalker(){
        return talker;
    }

    @Override
    public void act(float delta) {
        setVisible(talker != null);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        GameScreen.updateReaderPos(GameScreen.player.getX() - 700, GameScreen.player.getY() + GameScreen.player.getZ() - 150);

        if(talker != null) {
            GameScreen.player.setSeeInventory(false);
            GameScreen.setVisible(false);
            talker.setLookInv(true);
            setVisible(true);
            label.setText(talker.getPhrase());

            GameScreen.player.setRot(2);
            talker.setRot(2);
            talker.setAnimation(0);

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a);

            label.setX(getX() + 50);
            label.setY(getY() + 75);

            label.draw(batch, parentAlpha);

            talker.updateAnima();
            batch.setColor(talker.getColor());
            batch.draw(talker.getTexture(), getX() - 100, getY(), getOriginX(), getOriginY(),
                    talker.getPicWidth(), talker.getPicHeight(), 3f, 3f, getRotation());

            for (int i = 0; i < 6; i++) {
                Item item;
                if(i < 4) {
                    item = talker.getInventory().getItems()[i + 1];
                }
                else if(i == 4){
                    item = talker.getInventory().getItems()[5];
                }
                else{
                    item = talker.getInventory().getItems()[0];
                }
                if(item != null)
                    batch.draw(item.getTexture(), getX() - 100 + 3, getY(), getOriginX(), getOriginY(), talker.getPicWidth(), talker.getPicHeight(), 3f, 3f, getRotation());
            }

            batch.setColor(GameScreen.player.getColor());
            batch.draw(GameScreen.player.getTexture(), getX() + 750, getY(), getOriginX(), getOriginY(),
                    GameScreen.player.getPicWidth(), GameScreen.player.getPicHeight(), 3f, 3f, getRotation());

            for (int i = 0; i < 6; i++) {
                Item item;
                if(i < 4) {
                    item = GameScreen.player.getInventory().getItems()[i + 1];
                }
                else if(i == 4){
                    item = GameScreen.player.getInventory().getItems()[5];
                }
                else{
                    item = GameScreen.player.getInventory().getItems()[0];
                }
                if(item != null)
                    batch.draw(item.getTexture(), getX() + 753, getY(), getOriginX(), getOriginY(), GameScreen.player.getPicWidth(), GameScreen.player.getPicHeight(), 3f, 3f, getRotation());
            }

            if(talker.getStage() == null){
                setTalker(null);
            }
        }
        else if(isVisible()){
            setVisible(false);
        }
    }
}
