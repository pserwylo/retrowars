package com.serwylo.peter.retrowars;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.RemoteInput;
import com.serwylo.peter.retrowars.asteroids.AsteroidsGame;
import com.serwylo.peter.retrowars.missileCommand.MissileCommandGame;

public class Retrowars implements ApplicationListener 
{

	private Game game;
	
	@Override
	public void create() 
	{
		// Gdx.input = new RemoteInput();
		this.game = new AsteroidsGame();
		this.game.create();
	}

	@Override
	public void dispose() 
	{
		this.game.pause();
	}

	@Override
	public void pause() 
	{
		this.game.pause();
	}

	@Override
	public void render() 
	{
		this.game.render();
	}

	@Override
	public void resize( int width, int height ) 
	{
		this.game.resize( width, height );
	}

	@Override
	public void resume() 
	{
		this.game.resume();
	}

}
