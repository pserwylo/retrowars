package com.serwylo.peter.retrowars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;

public class AssetManager 
{
	

	private static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	private static BitmapFont hudFont;
	
	public static BitmapFont getHudFont()
	{
		if ( hudFont == null )
		{
			hudFont = TrueTypeFontFactory.createBitmapFont( 
				Gdx.files.internal( "fonts/PressStart2P/PressStart2P.ttf" ), 
				FONT_CHARACTERS,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(),
				16,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()
			);
		}
		
		return hudFont;
	}

	public static Sprite getShipSprite()
	{
		Texture shipTexture = new Texture( Gdx.files.internal("ship.png" ) );
		Sprite ship = new Sprite( shipTexture, 0, 0, 16, 32 );
		ship.setOrigin( 8, 16 );
		return ship;
	}

	/**
	 * The asteroids are organised into groups of 3.
	 * The first three are large, then medium, small and tiny.
	 * These are 128, 64, 32 and 16 pixels in diameter respectively.
	 * @return
	 */
	public static Sprite[] getAsteroidSprites()
	{
		Texture asteroidsTexture = new Texture( Gdx.files.internal("asteroids.png" ) );
		TextureRegion[] asteroidRegions = {
				
			// Tiny asteroids
			new TextureRegion( asteroidsTexture, 16, 0, 16, 16 ),
			new TextureRegion( asteroidsTexture, 0, 16, 16, 16 ),
			new TextureRegion( asteroidsTexture, 16, 16, 16, 16 ),
			
			// Small asteroids
			new TextureRegion( asteroidsTexture, 32, 0, 32, 32 ),
			new TextureRegion( asteroidsTexture, 0, 32, 32, 32 ),
			new TextureRegion( asteroidsTexture, 32, 32, 32, 32 ),
			
			// Medium asteroids
			new TextureRegion( asteroidsTexture, 64, 0, 64, 64 ),
			new TextureRegion( asteroidsTexture, 0, 64, 64, 64 ),
			new TextureRegion( asteroidsTexture, 64, 64, 64, 64 ),
			
			// Large asteroids
			new TextureRegion( asteroidsTexture, 128, 0, 128, 128 ),
			new TextureRegion( asteroidsTexture, 0, 128, 128, 128 ),
			new TextureRegion( asteroidsTexture, 128, 128, 128, 128 )
		};
		
		Sprite[] sprites = new Sprite[ asteroidRegions.length ];
		for ( int i = 0; i < asteroidRegions.length; i ++ )
		{
			Sprite sprite = new Sprite( asteroidRegions[ i ] );
			sprite.setOrigin( asteroidRegions[ i ].getRegionWidth() / 2, asteroidRegions[ i ].getRegionHeight() / 2 );
			sprites[ i ] = sprite;
		}
		
		return sprites;
	}

	public static Sprite getBulletSprite()
	{
		Texture bulletTexture = new Texture( Gdx.files.internal("bullet.png" ) );
		Sprite bullet = new Sprite( bulletTexture, 0, 0, 8, 8 );
		bullet.setOrigin( 4, 4 );
		return bullet;
	}

	public static Sprite getParticleSprite()
	{
		Texture particleTexture = new Texture( Gdx.files.internal("particle.png" ) );
		Sprite particle = new Sprite( particleTexture, 0, 0, 8, 8 );
		particle.setOrigin( 4, 4 );
		return particle;
	}

	public static TextureRegion[] getCityStates()
	{
		Texture cityTexture = new Texture( Gdx.files.internal("city-stages.png" ) );
		TextureRegion[] regions = {
				new TextureRegion( cityTexture, 0, 0, 32, 32 ),
				new TextureRegion( cityTexture, 32, 0, 32, 32 ),
				new TextureRegion( cityTexture, 0, 32, 32, 32 ),
				new TextureRegion( cityTexture, 32, 32, 32, 32 )
		};
		return regions;
	}

	public static Sprite getTowerSprite()
	{
		Texture towerTexture = new Texture( Gdx.files.internal("tower.png" ) );
		Sprite tower = new Sprite( towerTexture, 0, 0, 32, 16 );
		tower.setOrigin( 16, 0 );
		return tower;
	}
	
}
