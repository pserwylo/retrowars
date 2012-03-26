package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.serwylo.peter.retrowars.AssetManager;
import com.serwylo.peter.retrowars.Game;
import com.serwylo.peter.retrowars.GameObject;

public class Explosion extends GameObject
{

	public static final short CATEGORY_BIT = 16;

	private static Sprite[] explosionRegions;
	
	private long createdTime = 0;
	
	public static final int DURATION = 2500;
	
	public static final float MAX_SIZE = 2.0f;
	
	private boolean isAlive = true;
	
	public Explosion( Vector2 position )
	{
		/*if ( explosionSprites == null )
		{
			explosionSprites = AssetManager.getExplosionRegions();
		}*/
	
		this.createdTime = System.currentTimeMillis();
		this.sprite = null;
		this.helpInit( new Vector2( 0.1f, 0.1f ), position.cpy(), Explosion.CATEGORY_BIT, Missile.CATEGORY_BIT );
		this.b2SpriteFixture.setSensor( true );
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}
	
	@Override
	public void render( SpriteBatch batch ) 
	{
		// this.helpDrawSprite( batch );
	}

	@Override
	public void update( float deltaTime ) 
	{
		int age = (int)( System.currentTimeMillis() - this.createdTime );
		if ( age > DURATION )
		{
			this.isAlive = false;
			Game.getInstance().queueForDestruction( this );
		}
		
		float a = (float)age / DURATION;
		float size = MAX_SIZE * a;
		this.b2SpriteFixture.getShape().setRadius( size / 2 );
	}

}
