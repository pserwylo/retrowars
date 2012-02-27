package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.serwylo.peter.retrowars.SpriteManager;

public class City extends Actor
{

	private static Sprite citySprite;
	
	private int x;
	
	/**
	 * @param x The x coordinate of the screen where the city is to be placed (the y
	 * coordinate will always be zero).
	 */
	public City( int x )
	{
		this.x = x;
		if ( citySprite == null )
		{
			citySprite = SpriteManager.getCitySprite();
		}
	}
	
	/**
	 * The current X location of this city.
	 * @return
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * @see \getX()
	 * @return
	 */
	public void setX( int value )
	{
		this.x = value;
	}
	
	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) 
	{
		citySprite.setPosition( this.x, 10 );
		citySprite.draw( batch );
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
