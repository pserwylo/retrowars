package com.serwylo.peter.retrowars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteManager 
{

	public static Sprite getShipSprite()
	{
		Texture shipTexture = new Texture( Gdx.files.internal("assets/ship.png" ) );
		Sprite ship = new Sprite( shipTexture, 0, 0, 16, 32 );
		ship.setOrigin( 8, 16 );
		return ship;
	}

	public static Sprite getBulletSprite()
	{
		Texture bulletTexture = new Texture( Gdx.files.internal("assets/bullet.png" ) );
		Sprite bullet = new Sprite( bulletTexture, 0, 0, 8, 8 );
		bullet.setOrigin( 4, 4 );
		return bullet;
	}

	public static TextureRegion[] getCityStates()
	{
		Texture cityTexture = new Texture( Gdx.files.internal("assets/city-stages.png" ) );
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
		Texture towerTexture = new Texture( Gdx.files.internal("assets/tower.png" ) );
		Sprite tower = new Sprite( towerTexture, 0, 0, 32, 16 );
		tower.setOrigin( 16, 0 );
		return tower;
	}
	
}
