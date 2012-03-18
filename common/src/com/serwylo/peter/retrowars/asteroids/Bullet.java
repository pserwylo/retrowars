package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;

public class Bullet extends GameObject
{
	
	/**
	 * Used for Box2D collision filtering.
	 */
	public static final short CATEGORY_BIT = 2; 
	
	public static final int SPEED = 50000;
	
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
	
	private boolean isAlive;
	
	public Bullet( Vector2 position, Vector2 shipVelocity, float angle )
	{
		if ( bulletSprite == null )
		{
			bulletSprite = SpriteManager.getBulletSprite();
		}

		// Convert the orientation into a velocity vector which will be added to the ships
		// velocity (which was passed into this constructor).
		Vector2 bulletVelocity = new Vector2( 0, 1.0f ).rotate( angle );
		bulletVelocity.x *= SPEED;
		bulletVelocity.y *= SPEED;
		bulletVelocity.add( shipVelocity );

		this.init( bulletSprite, position, Bullet.CATEGORY_BIT, Asteroid.CATEGORY_BIT );
		this.b2Body.applyLinearImpulse( bulletVelocity, this.b2Body.getPosition() );

		this.birthTime = System.currentTimeMillis();
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}
	
	/**
	 * Updates the position of the bullet, and marks it as ready to remove if
	 * it has lived for a certain period of time...
	 * @param delta
	 * @return If the bullet has been around longer than {@link LIFE}, then it will 
	 * return false.
	 */
	public void update( float deltaTime )
	{
		// GraphicsUtils.wrapVectorAroundScreen( this.b2Body.getPosition() );
		// this.b2Body.applyForceToCenter( this.force );
		// this.isAlive = ( ( System.currentTimeMillis() < this.birthTime + LIFE ) );
	}
	
	public void render( SpriteBatch batch )
	{
		bulletSprite.setPosition( this.b2Body.getPosition().x - bulletSprite.getWidth() / 2, this.b2Body.getPosition().y - bulletSprite.getHeight() / 2 );
		long age = System.currentTimeMillis() - this.birthTime;
		float alpha = 1.0f;
		if ( age > FADE_AFTER )
		{
			alpha = (float)( LIFE - age )  / FADE_AFTER;
		}
		bulletSprite.draw( batch, alpha );
	}
	
}
