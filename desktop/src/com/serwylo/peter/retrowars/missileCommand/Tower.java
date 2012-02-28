package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.serwylo.peter.retrowars.SpriteManager;

public class Tower extends Actor
{

	private static Sprite towerSprite;
	
	private Vector2 position;
	
	private long timeSinceLastFire = 0;
	
	public static final int RELOAD_TIME = 1000;
	
	/**
	 * @param x The x coordinate of the screen where the tower is to be placed (the y
	 * coordinate will always be zero).
	 */
	public Tower( int x )
	{
		this.position = new Vector2( x, 10 );
		if ( towerSprite == null )
		{
			towerSprite = SpriteManager.getTowerSprite();
		}
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
	
	/**
	 * The current position of this tower.
	 * @return
	 */
	public Vector2 getPosition()
	{
		return this.position;
	}
	
	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) 
	{
		towerSprite.setPosition( this.position.x, this.position.y );
		towerSprite.draw( batch );
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
