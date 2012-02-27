package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;

public class Missile 
{
	
	public static final int SPEED = 50;
	
	private static Sprite bulletSprite;
	
	private Vector2 position, velocity;
	
	public Missile( Vector2 start, Vector2 target )
	{
		this.position = start;
		
		this.velocity = start.cpy().rotate( 180 ).add( target );
		this.velocity.nor();
		this.velocity.x *= SPEED;
		this.velocity.y *= SPEED;
		
		if ( bulletSprite == null )
		{
			bulletSprite = SpriteManager.getBulletSprite();
		}
	}
	
	/**
	 * Updates the position of the bullet
	 * @param delta
	 */
	public void update( float delta )
	{
		this.position.x += this.velocity.x * delta;
		this.position.y += this.velocity.y * delta;
	}
	
	public void render( SpriteBatch batch )
	{
		bulletSprite.setPosition( this.position.x, this.position.y );
		bulletSprite.draw( batch );
	}
	
}
