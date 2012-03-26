package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.serwylo.peter.retrowars.AssetManager;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.GraphicsUtils;

public class Asteroid extends GameObject
{

	/**
	 * Used for Box2D collision filtering.
	 */
	public static final short CATEGORY_BIT = 4;

	public static final int SIZE_LARGE = 3;
	public static final int SIZE_MEDIUM = 2;
	public static final int SIZE_SMALL = 1;
	public static final int SIZE_TINY = 0;

	public static final float MAX_SPEED = 50;
	public static final float MIN_SPEED = 20;
	
	private static Sprite[] asteroidSprites;
	
	private int size;
	
	public static Vector2 generateRandomVelocity()
	{
		float direction = (float)Math.random() * (float)Math.PI * 2;
		float speed = (float)Math.random() * ( MAX_SPEED - MIN_SPEED ) + MIN_SPEED;
		return new Vector2( (float)Math.cos( direction ) * speed, (float)Math.sin( direction ) * speed );
	}
	
	public static Asteroid createLarge( Vector2 position, Vector2 velocity )
	{
		return new Asteroid( SIZE_LARGE, position, velocity );
	}
	
	public static Asteroid createMedium( Vector2 position, Vector2 velocity )
	{
		return new Asteroid( SIZE_MEDIUM, position, velocity );
	}
	
	public static Asteroid createSmall( Vector2 position, Vector2 velocity )
	{
		return new Asteroid( SIZE_SMALL, position, velocity );
	}
	
	public static Asteroid createTiny( Vector2 position, Vector2 velocity )
	{
		return new Asteroid( SIZE_TINY, position, velocity );
	}
	
	public Asteroid( int size, Vector2 position, Vector2 velocity )
	{	
		if ( asteroidSprites == null )
		{
			asteroidSprites = AssetManager.getAsteroidSprites();
		}
		
		if ( size < 0 || size > SIZE_LARGE )
		{
			throw new IllegalArgumentException( "Size must be SIZE_TINY, SIZE_SMALL, SIZE_MEDIUM or SIZE_LARGE" );
		}
		
		int i = (int)( Math.random() * 3 + size * 3 );
		this.sprite = asteroidSprites[ i ];
		this.size = size;
		
		float diameter = (float)Math.pow( 2.0, this.size + 1 );
		
		CircleShape shape = new CircleShape();
		shape.setRadius( diameter / 2 );
		this.helpInit( new Vector2( diameter, diameter ), position, shape, Asteroid.CATEGORY_BIT, (short)( Ship.CATEGORY_BIT | Bullet.CATEGORY_BIT ) );
		this.b2Body.applyLinearImpulse( velocity, this.b2Body.getPosition() );

	}
	
	/**
	 * The diameter (in pixels) of this asteroid's sprite texture.
	 * @return
	 */
	public int getSize() 
	{
		return this.size;
	}
	
	/**
	 * Updates the position of the asteroid, and wraps it around the screen if necessary.
	 * @param delta
	 */
	public void update( float delta )
	{
		GraphicsUtils.wrapObjectAroundScreen( this );
	}
	
	/**
	 * Draws the sprite to the screen, but its bounding box crosses over any particular 
	 * edge of the screen, draw it on the opposing side too.
	 * @param batch
	 */
	public void render( SpriteBatch batch )
	{
		this.helpDrawSprite( batch );
		// GraphicsUtils.drawSpriteWithScreenWrap( this.sprite, this.b2Body.getPosition(), batch );
	}

}
