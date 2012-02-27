package com.serwylo.peter.retrowars;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Game implements ApplicationListener 
{

	protected Stage stage;
	
	@Override
	public void create() 
	{
		this.stage = new Stage( 0, 0, true );
	}

	@Override
	public void dispose() 
	{
		this.stage.dispose();
	}

	@Override
	public void pause() 
	{
	
	}

	@Override
	public void resize( int width, int height ) 
	{
		this.stage.setViewport( width, height, true );
	}

	@Override
	public void resume() 
	{
		
	}

}
