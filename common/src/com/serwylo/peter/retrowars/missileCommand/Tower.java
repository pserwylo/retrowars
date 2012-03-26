package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.serwylo.peter.retrowars.AssetManager;
import com.serwylo.peter.retrowars.GameObject;

public class Tower extends GameObject
{

	public static final short CATEGORY_BIT = 2;

	private static Sprite towerSprite;
	
	private long timeSinceLastFire = 0;
	
	public static final int RELOAD_TIME = 1000;
	
	/**
	 * @param x The x coordinate of the screen where the tower is to be placed (the y
	 * coordinate will always be zero).
	 */
	public Tower( int x )
	{
		if ( towerSprite == null )
		{
			towerSprite = AssetManager.getTowerSprite();
		}
		this.sprite = towerSprite;
		this.helpInit( new Vector2( 1.0f, 1.0f ), new Vector2( x, 1.0f ) );
	}
	
	/**
	 * Check if we have waited long enough since last time we fired.
	 * @return
	 */
	public boolean readyToFire()
	{
		return ( System.currentTimeMillis() > this.timeSinceLastFire + RELOAD_TIME );
	}
	
	/**
	 * 
	 */
	public void fire()
	{
		this.timeSinceLastFire = System.currentTimeMillis();
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
