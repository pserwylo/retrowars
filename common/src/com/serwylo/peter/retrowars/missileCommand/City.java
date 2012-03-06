package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.serwylo.peter.retrowars.SpriteManager;

public class City extends Actor
{

	private static final int CITY_HEALTH = 3;
	
	private static TextureRegion[] cityStateTextures;
	
	private Vector2 position;
	
	private int health = CITY_HEALTH;
	
	/**
	 * @param x The x coordinate of the screen where the city is to be placed (the y
	 * coordinate will always be zero).
	 */
	public City( int x )
	{
		this.position = new Vector2( x, 10 );
		if ( cityStateTextures == null )
		{
			cityStateTextures = SpriteManager.getCityStates();
		}
	}
	
	/**
	 * If the city has been hit {@link CITY_HEALTH} times, then it is no longer alive.
	 * @return
	 */
	public boolean isAlive()
	{
		return this.health > 0;
	}
	
	/**
	 * Tells us how many more missiles this city can take before it is dead.
	 * @return
	 */
	public int getHealth()
	{
		return this.health;
	}
	
	/**
	 * Call this when the city has been hit, and it will lower its health.
	 */
	public void hitByMissile()
	{
		if ( this.health > 0 )
		{
			this.health --;
		}
	}
	
	/**
	 * The current position of this city.
	 * @return
	 */
	public Vector2 getPosition()
	{
		return this.position;
	}
	
	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) 
	{
		int index = CITY_HEALTH - health;
		TextureRegion toDraw = cityStateTextures[ index ];
		batch.draw( toDraw, this.position.x - toDraw.getRegionWidth()/2, this.position.y );
	}

	@Override
	public boolean touchDown( float x, float y, int pointer ) 
	{
		return false;
	}

	@Override
	public void touchUp( float x, float y, int pointer ) 
	{
		
	}

	@Override
	public void touchDragged( float x, float y, int pointer ) 
	{
		
	}

	@Override
	public Actor hit( float x, float y ) 
	{
		return null;
	}

}
