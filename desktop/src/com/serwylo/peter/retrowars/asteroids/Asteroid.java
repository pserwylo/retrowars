package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;

public class Asteroid 
{

	public static final int SPRITE_LARGE = 0;
	public static final int SPRITE_MEDIUM = 1;
	public static final int SPRITE_SMALL = 2;
	public static final int SPRITE_TINY = 3;

	public static final int SIZE_LARGE = 128;
	public static final int SIZE_MEDIUM = 64;
	public static final int SIZE_SMALL = 32;
	public static final int SIZE_TINY = 16;
	
	public static final int SPEED = 500;
	
	private static Sprite[] asteroidSprites;
	
	private Vector2 position, velocity;
	
	private int size;
	
	private Sprite currentSprite;
	
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
		this.position = position.cpy();
		this.velocity = velocity.cpy();
		
		if ( asteroidSprites == null )
		{
			asteroidSprites = SpriteManager.getAsteroidSprites();
		}
		
		int spriteIndex = 0;
		switch( size )
		{
		case SIZE_TINY:
			spriteIndex = SPRITE_TINY;
			break;
		case SIZE_SMALL:
			spriteIndex = SPRITE_SMALL;
			break;
		case SIZE_MEDIUM:
			spriteIndex = SPRITE_MEDIUM;
			break;
		case SIZE_LARGE:
			spriteIndex = SPRITE_LARGE;
			break;
		default:
			throw new IllegalArgumentException( "Size must be SIZE_TINY, SIZE_SMALL, SIZE_MEDIUM or SIZE_LARGE" );
		}
		
		this.size = size;
		int i = (int)( Math.random() * 3 ) + ( spriteIndex * 3 );
		System.out.println( spriteIndex + " - " + i );
		this.currentSprite = asteroidSprites[ i ]; 
	}
	
	/**
	 * The diameter (in pixels) of this asteroid.
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
		this.position.x += this.velocity.x * delta;
		this.position.y += this.velocity.y * delta;
		GraphicsUtils.wrapVectorAroundScreen( this.position );
	}
	
	/**
	 * Draws the sprite to the screen, but its bounding box crosses over any particular 
	 * edge of the screen, draw it on the opposing side too.
	 * @param batch
	 */
	public void render( SpriteBatch batch )
	{
		GraphicsUtils.drawSpriteWithScreenWrap( this.currentSprite, this.position, batch );
		
	}
	
}
