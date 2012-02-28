package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.SpriteManager;

public class Missile 
{
	
	public static final int SPEED = 50;
	
	private static Sprite bulletSprite;
	
	private Vector2 position, velocity;
	
	private City target;
	
	public Missile( Vector2 start, City target )
	{
		this.position = start;
		
		this.velocity = start.cpy().rotate( 180 ).add( target.getPosition() );
		this.velocity.nor();
		this.velocity.x *= SPEED;
		this.velocity.y *= SPEED;
		
		this.target = target;
		
		if ( bulletSprite == null )
		{
			bulletSprite = SpriteManager.getBulletSprite();
		}
	}
	
	/**
	 * Not used by this class much, but having it available makes it much easier to handle
	 * the situation when the city finally gets hit.
	 * @return
	 */
	public City getTarget()
	{
		return this.target;
	}
	
	/**
	 * Updates the position of the bullet
	 * @param delta
	 */
	public boolean update( float delta )
	{
		this.position.x += this.velocity.x * delta;
		this.position.y += this.velocity.y * delta;
		return ( this.position.y > this.target.getPosition().y );
	}
	
	public void render( SpriteBatch batch )
	{
		bulletSprite.setPosition( this.position.x, this.position.y );
		bulletSprite.draw( batch );
	}
	
}
