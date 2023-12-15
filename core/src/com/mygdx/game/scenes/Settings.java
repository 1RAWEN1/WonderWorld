package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.objects.buttons.settings.BackButton;
import com.mygdx.game.objects.buttons.settings.ChangeControlButton;
import com.mygdx.game.objects.buttons.settings.Scroller;
import com.mygdx.game.objects.serialization.SettingsSave;

import java.io.IOException;

public class Settings implements Screen {
    private final Stage stage;
    private final Label label = new Label("Default", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
    public Settings(MyGdxGame game){
        stage = new Stage(new FitViewport(1200, 750));
        Gdx.input.setInputProcessor(stage);

        BackButton backButton = new BackButton(game);
        backButton.setPosition(0, 650);
        stage.addActor(backButton);

        GameScreen.addButtons();
        for(int i = 0; i < GameScreen.buttons.size(); i++){
            ChangeControlButton changeControlButton = new ChangeControlButton(i);
            changeControlButton.setPosition(555, 700 - 30 * i);
            stage.addActor(changeControlButton);

            String str = "";
            switch(i){
                case 0:
                    str = "Move up";
                    break;
                case 1:
                    str = "Move down";
                    break;
                case 2:
                    str = "Move right";
                    break;
                case 3:
                    str = "Move left";
                    break;
                case 4:
                    str = "Jump";
                    break;
                case 5:
                    str = "Inventory";
                    break;
                case 6:
                    str = "Action";
                    break;
                case 7:
                    str = "Pause";
                    break;
                case 8:
                    str = "Close menu";
                    break;
                case 9:
                    str = "Teleport";
                    break;
                case 10:
                    str = "Cast magic";
                    break;
                case 11:
                    str = "Kick";
                    break;
                case 12:
                    str = "Ude shield";
                    break;
            }

            Label label1 = new Label(str, new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            label1.setPosition(470, 705 - 30 * i);
            stage.addActor(label1);
        }

        label.setPosition(580, 280);

        Scroller scroller = new Scroller(100);
        scroller.setPosition(480, 300);
        stage.addActor(scroller);
        Label label1 = new Label("Music volume", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        label1.setPosition(370, 303);
        stage.addActor(label1);

        try {
            SettingsSave save = GameScreen.getSettingsSave();
            scroller.updateImage(save.getVolume());
        }
        catch(Exception e) {}

        stage.addActor(label);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        label.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen.reloadButton();

                for(Actor act : stage.getActors())
                    if(act instanceof ChangeControlButton)
                        ((ChangeControlButton) act).reload();
                    else if(act instanceof Scroller)
                        ((Scroller) act).updateImage(1);

                try {
                    GameScreen.saveButtons();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
