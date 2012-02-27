package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;

public class Bullet 
{
	
	public static final int SPEED = 200;
	
	/**
	 * Number of milliseconds the bullet is alive for. When it has been around for this long,
	 * then it is marked as inactive and the Ship will remove it. As it approaches this,
	 * then it will start to fade away so that it doesn't just disappear from the screen.
	 */
	public static int LIFE = 2000;
	
	/**
	 * The amount of milliseconds before the bullet starts to fade away (should be smaller
	 * than \LIFE).
	 */
	public static int FADE_AFTER = 1500;
	
	private static Sprite bulletSprite;
	
	private long birthTime;
	
	private Vector2 position, velocity;
	
	public Bullet( Vector2 position, Vector2 velocity, Vector2 orientation )
	{
		this.position = position;
		
		orientation.nor();
		orientation.x *= SPEED;
		orientation.y *= SPEED;
		
		this.velocity = velocity.add( orientation );
		
		if ( bulletSprite == null )
		{
			bulletSprite = SpriteManager.getBulletSprite();
		}
		
		this.birthTime = System.currentTimeMillis();
	}
	
	/**
	 * Updates the position of the bullet, and marks it as ready to remove if
	 * it has lived for a certain period of time...
	 * @param delta
	 * @return If the bullet has been around longer than \LIFE, then it will 
	 * return false.
	 */
	public boolean update( float delta )
	{
		this.position.x += this.velocity.x * delta;
		this.position.y += this.velocity.y * delta;
		GraphicsUtils.wrapScreen( this.position );
		
		return ( System.currentTimeMillis() < this.birthTime + LIFE );
	}
	
	public void render( SpriteBatch batch )
	{
		bulletSprite.setPosition( this.position.x, this.position.y );
		long age = System.currentTimeMillis() - this.birthTime;
		float alpha = 1.0f;
		if ( age > FADE_AFTER )
		{
			alpha = (float)( LIFE - age )  / FADE_AFTER;
		}
		bulletSprite.draw( batch, alpha );
	}
	
}
