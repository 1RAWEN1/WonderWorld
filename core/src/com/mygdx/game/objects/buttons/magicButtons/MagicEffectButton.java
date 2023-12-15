package com.mygdx.game.objects.buttons.magicButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.objects.buttons.gameButtons.Joystick;
import com.mygdx.game.objects.characters.effects.DealtEffect;
import com.mygdx.game.scenes.GameScreen;

public class MagicEffectButton extends Actor {
    private final TextureRegion texture = new TextureRegion(GameScreen.loadTexture("buttons\\effectButtons.png"));
    private float deltaX;
    private float deltaY;
    private int effectType;
    private float magicCost = 0;
    public MagicEffectButton(int effectType, float deltaX, float deltaY){
        if(effectType == 0){
            texture.setRegion(40, 186, 100, 100);

            magicCost = 10;
        }
        else if(effectType == 1){
            texture.setRegion(142, 42, 100, 100);

            magicCost = 15;
        }
        else if(effectType == 2 || effectType == -1){
            texture.setRegion(40, 474, 100, 100);

            magicCost = 15;
        }
        else if(effectType == -4){
            texture.setRegion(141, 474, 100, 100);

            magicCost = 15;
        }
        else if(effectType == -3){
            texture.setRegion(245, 45, 100, 100);

            magicCost = 10;
        }
        else if(effectType == -2){
            texture.setRegion(350, 330, 100, 100);

            magicCost = 10;
        }
        else if(effectType == -5 || effectType == 3){
            texture.setRegion(141, 187, 100, 100);

            magicCost = 15;
        }
        else if(effectType == -6){
            texture.setRegion(245, 187, 100, 100);

            magicCost = 15;
        }

        this.effectType = effectType;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        setColor(new Color(0.7f, 0.7f, 0.7f, 1f));
    }

    public void setSelected(){
        setColor(new Color(1, 1, 1, 1));

        hasEffect = true;
    }

    private boolean tabPush = false;
    private boolean hasEffect = false;
    @Override
    public void act(float delta) {
        if(Gdx.input.isTouched(Joystick.index) && !tabPush){
            if(touchThis() && isVisible()) {
                hasEffect = !hasEffect;

                switch (effectType) {
                    case -2:
                    case -1:
                    case 2:
                    case -4:

                    case -5:
                    case 1:

                    case -3:
                    case 3:
                        if(hasEffect)
                            GameScreen.player.getDealtEffects().add(
                                    new DealtEffect(0, effectType));

                        break;

                    case 0:
                        GameScreen.player.setWithFreezing(!GameScreen.player.isWithFreezing());
                        break;

                    case -6:
                        GameScreen.player.setAutoCast(!GameScreen.player.isAutoCast());

                        break;

                }

                if(hasEffect) {
                    setColor(new Color(1, 1, 1, 1));

                    GameScreen.player.setMagicCost(GameScreen.player.getMagicCost() + magicCost);
                }
                else {
                    setColor(new Color(0.7f, 0.7f, 0.7f, 1f));

                    GameScreen.player.setMagicCost(GameScreen.player.getMagicCost() - magicCost);

                    for(DealtEffect dealtEffect : GameScreen.player.getDealtEffects())
                        if(dealtEffect.getType() == effectType) {
                            GameScreen.player.getDealtEffects().remove(dealtEffect);

                            break;
                        }
                }
            }
            tabPush = true;
        }
        else if(!Gdx.input.isTouched() && tabPush){
            tabPush = false;
        }

        setVisible(GameScreen.dialogReader.getTalker() == null && GameScreen.player.isSeeMagic());

        setPosition(GameScreen.player.getX() + deltaX, GameScreen.player.getY() + GameScreen.player.getZ() + deltaY);
    }

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX(Joystick.index);
        float touchY = GameScreen.getMouseY(Joystick.index);

        return touchX >= (getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2) && touchX <= getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2 + texture.getRegionWidth()
                && touchY >= (getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2) && touchY <= getY() - GameScreen.player.getZ() - GameScreen.player.getCentralY() + getStage().getViewport().getWorldHeight() / 2 + texture.getRegionHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(texture, getX(), getY());
    }
}
