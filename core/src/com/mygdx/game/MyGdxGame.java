package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.scenes.GameScreen;
import com.mygdx.game.scenes.Lobby;

public class MyGdxGame extends Game {
	public MyInterface context;
	public MyGdxGame(){

	}

	public static boolean phone = false;
	public MyGdxGame(boolean phone){
		MyGdxGame.phone = phone;
	}

	public MyGdxGame(boolean phone, MyInterface myInterface){
		context = myInterface;
		MyGdxGame.phone = phone;
	}

	private Screen gameScreen;
	@Override
	public void create () {
		gameScreen = new Lobby(this);
		this.setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();

		if(getScreen() instanceof GameScreen && GameScreen.player.getStage() == null){
			gameScreen = new GameScreen(this);
			this.setScreen(gameScreen);
		}
	}
	
	@Override
	public void dispose () {
		gameScreen.dispose();
	}
}
