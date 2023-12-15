package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.objects.buttons.lobby.PlayButton;
import com.mygdx.game.objects.buttons.lobby.SettingsButton;
import com.mygdx.game.objects.serialization.SettingsSave;

public class Lobby implements Screen {
    private final Stage stage;
    private final MyGdxGame game;
    public static Music music = Gdx.audio.newMusic(Gdx.files.internal("music/back.mp3"));
    public static final Label loadingLabel = new Label("Loading...", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
    public Lobby(MyGdxGame game) {
        try {
            SettingsSave save = GameScreen.getSettingsSave();
            music.setVolume(save.getVolume());
        }
        catch(Exception e) {}

        music.play();

        music.setLooping(true);

        this.game = game;
        stage = new Stage(new FitViewport(1200, 750));
        Gdx.input.setInputProcessor(stage);

        PlayButton playButton = new PlayButton();
        playButton.setPosition(450, 350);
        stage.addActor(playButton);

        if(!MyGdxGame.phone) {
            SettingsButton settingsButton = new SettingsButton(game);
            settingsButton.setPosition(450, 220);
            stage.addActor(settingsButton);
        }

        loadingLabel.setPosition(620, 330);
        loadingLabel.setVisible(false);
        stage.addActor(loadingLabel);
    }

    @Override
    public void show() {

    }

    private boolean change = false;
    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if(loadingLabel.isVisible()){
            if(change)
                game.setScreen(new GameScreen(game));
            change = true;
        }
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
