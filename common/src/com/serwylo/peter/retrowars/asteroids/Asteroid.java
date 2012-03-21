package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.serwylo.peter.retrowars.Game;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;
import com.serwylo.peter.retrowars.collisions.ICollidable;

public class Asteroid extends GameObject
{

	/**
	 * Used for Box2D collision filtering.
	 */
	public static final short CATEGORY_BIT = 4;

	public static final int SIZE_LARGE = 0;
	public static final int SIZE_MEDIUM = 1;
	public static final int SIZE_SMALL = 2;
	public static final int SIZE_TINY = 3;
	
	public static final int SPEED = 500;
	
	private static Sprite[] asteroidSprites;
	
	private int size;
	
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
			asteroidSprites = SpriteManager.getAsteroidSprites();
		}
		
		int spriteIndex = 0;
		if ( size < 0 || size > SIZE_TINY )
		{
			throw new IllegalArgumentException( "Size must be SIZE_TINY, SIZE_SMALL, SIZE_MEDIUM or SIZE_LARGE" );
		}
		
		int i = (int)( Math.random() * 3 ) + ( spriteIndex * 3 );
		this.sprite = asteroidSprites[ i ];
		this.size = size;
		
		float radius = (float)Math.pow( 2.0, this.size );
		
		CircleShape shape = new CircleShape();
		shape.setRadius( radius );
		this.helpInit( new Vector2( radius * 2, radius * 2 ), position, shape, Asteroid.CATEGORY_BIT );
		this.b2Body.applyLinearImpulse( velocity, this.b2Body.getPosition() );

	}
	
	/**
	 * The diameter (in pixels) of this asteroid's sprite texture.
	 * @return
	 */
	public int getSizePixels() 
	{
		return this.size;
	}
	
	/**
	 * Updates the position of the asteroid, and wraps it around the screen if necessary.
	 * @param delta
	 */
	public void update( float delta )
	{
		GraphicsUtils.wrapVectorAroundScreen( this.b2Body.getPosition() );
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
