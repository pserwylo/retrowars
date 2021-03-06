package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.serwylo.peter.retrowars.RetroGame;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.AssetManager;

public class Bullet extends GameObject
{
	
	/**
	 * Used for Box2D collision filtering.
	 */
	public static final short CATEGORY_BIT = 2; 
	
	public static final int SPEED = 25;
	
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
	
	private boolean isAlive = true;
	
	/**
	 * 
	 * @param position
	 * @param shipVelocity
	 * @param angle In Radians
	 */
	public Bullet( Vector2 position, Vector2 shipVelocity, float angle )
	{
		if ( bulletSprite == null )
		{
			bulletSprite = AssetManager.getBulletSprite();
		}
		
		this.sprite = bulletSprite;

		// Convert the orientation into a velocity vector which will be added to the ships
		// velocity (which was passed into this constructor).
		Vector2 bulletVelocity = new Vector2( 0, 1.0f ).rotate( angle * MathUtils.radiansToDegrees );
		bulletVelocity.x *= SPEED;
		bulletVelocity.y *= SPEED;
		bulletVelocity.add( shipVelocity );

		CircleShape shape = new CircleShape();
		shape.setRadius( 0.5f );
		this.helpInit( new Vector2( 1.0f, 1.0f ), position, shape, Bullet.CATEGORY_BIT, Asteroid.CATEGORY_BIT );
		this.b2Body.applyLinearImpulse( bulletVelocity, this.b2Body.getPosition() );

		this.birthTime = System.currentTimeMillis();
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}
	
	public void markForDestruction()
	{
		this.isAlive = false;
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
		if ( this.isAlive )
		{
			GraphicsUtils.wrapObjectAroundScreen( this );
			this.isAlive = ( ( System.currentTimeMillis() < this.birthTime + LIFE ) );
		}

		if ( !this.isAlive )
		{
			RetroGame.getInstance().queueForDestruction( this.b2Body );
		}
	}
	
	public void render( SpriteBatch batch )
	{
		long age = System.currentTimeMillis() - this.birthTime;
		float alpha = 1.0f;
		if ( age > FADE_AFTER )
		{
			alpha = (float)( LIFE - age )  / FADE_AFTER;
		}
		this.helpDrawSprite( batch, alpha );
	}
	
}
