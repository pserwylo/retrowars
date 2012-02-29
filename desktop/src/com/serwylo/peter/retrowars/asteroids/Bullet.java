package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;

public class Bullet 
{
	
	public static final int SPEED = 500;
	
	/**
	 * Number of milliseconds the bullet is alive for. When it has been around for this long,
	 * then it is marked as inactive and the Ship will remove it. As it approaches this,
	 * then it will start to fade away so that it doesn't just disappear from the screen.
	 */
	public static int LIFE = 1250;
	
	/**
	 * The amount of milliseconds before the bullet starts to fade away (should be smaller
	 * than {@link LIFE}.
	 */
	public static int FADE_AFTER = 1000;
	
	private static Sprite bulletSprite;
	
	private long birthTime;
	
	private Vector2 position, velocity;
	
	public Bullet( Vector2 position, Vector2 shipVelocity, Vector2 orientation )
	{
		this.position = position.cpy();
		
		// Convert the orientation into a velocity vector which will be added to the ships
		// velocity (which was passed into this constructor).
		Vector2 bulletVelocity = orientation.cpy().nor();
		bulletVelocity.x *= SPEED;
		bulletVelocity.y *= SPEED;
		
		this.velocity = shipVelocity.cpy().add( bulletVelocity );
		
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
	 * @return If the bullet has been around longer than {@link LIFE}, then it will 
	 * return false.
	 */
	public boolean update( float delta )
	{
		this.position.x += this.velocity.x * delta;
		this.position.y += this.velocity.y * delta;
		GraphicsUtils.wrapVectorAroundScreen( this.position );
		
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
