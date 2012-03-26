package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.serwylo.peter.retrowars.AssetManager;
import com.serwylo.peter.retrowars.Game;
import com.serwylo.peter.retrowars.GameObject;

public class City extends GameObject
{

	public static final short CATEGORY_BIT = 1;

	private static final int CITY_HEALTH = 3;
	
	private static TextureRegion[] cityStateTextures;
	
	private int health = CITY_HEALTH;
	
	/**
	 * @param x The x coordinate of the screen where the city is to be placed (the y
	 * coordinate will always be zero).
	 */
	public City( int x )
	{
		if ( cityStateTextures == null )
		{
			cityStateTextures = AssetManager.getCityStates();
		}
		
		this.setSprite( cityStateTextures[ 0 ] );

		Vector2 position = new Vector2( x, 1.0f );
		Vector2 size = new Vector2( 1.0f, 1.0f );
		PolygonShape shape = new PolygonShape();
		shape.setAsBox( size.x / 2, size.y / 2 );
		
		this.helpInit( size, position, shape, City.CATEGORY_BIT, Missile.CATEGORY_BIT );
		this.b2SpriteFixture.setSensor( true );
		
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

		int index = CITY_HEALTH - this.health;
		this.setSprite( cityStateTextures[ index ] );
	}
	
	@Override
	public void render( SpriteBatch batch ) 
	{
		this.helpDrawSprite( batch );
	}

	@Override
	public void update( float deltaTime ) 
	{
		
	}

}
