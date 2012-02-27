package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.serwylo.peter.retrowars.Game;

public class AsteroidsGame extends Game
{

	/**
	 * The ship needs to be positioned in the centre of the screen, but when
	 * we create it we don't know where the centre of the screen is. Instead,
	 * we will wait until the first time resize(...) is called. This flag makes
	 * sure we only reposition the ship the first time its resized.
	 */
	private boolean startedFlag = false;
	
	private Ship ship;
	
	public void create()
	{
		super.create();
		this.ship = new Ship();
		this.stage.addActor( this.ship );
	}
	
	@Override
	public void resize( int width, int height )
	{
		super.resize( width, height );
		
		// The first time we resize we need to position the ship.
		if ( !startedFlag )
		{
			this.ship.getPosition().x = width / 2;
			this.ship.getPosition().y = height / 2;
			startedFlag = true;
		}
	}
	
	public void update()
	{
		this.stage.act( Gdx.graphics.getDeltaTime() );
	}
	
	public void render()
	{
		this.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
		this.stage.draw();
	}
	
}
