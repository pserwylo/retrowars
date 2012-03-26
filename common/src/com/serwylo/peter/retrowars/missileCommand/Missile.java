package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.AssetManager;
import com.serwylo.peter.retrowars.Game;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.asteroids.AsteroidsGame;
import com.serwylo.peter.retrowars.collisions.ICollidable;

public class Missile extends GameObject
{

	public static final short CATEGORY_BIT = 4;
	
	public static final float SPEED = 0.03f;
	
	private static Sprite bulletSprite;
	
	private City target;
	
	private boolean isAlive = true;
	
	public Missile( Vector2 start, City target )
	{
		this.target = target;
		
		if ( bulletSprite == null )
		{
			bulletSprite = AssetManager.getBulletSprite();
		}
		
		this.sprite = bulletSprite;

		Vector2 position = start.cpy();
		Vector2 size = new Vector2( 0.1f, 0.1f );
		this.helpInit( size, position, Missile.CATEGORY_BIT, (short)( City.CATEGORY_BIT | Explosion.CATEGORY_BIT ) );

		Vector2 impulse = start.cpy().rotate( 180 ).add( target.getB2Body().getPosition() );
		impulse.nor();
		impulse.x *= SPEED;
		impulse.y *= SPEED;
		
		this.b2Body.applyLinearImpulse( impulse, position );
	}
	
	/**
	 * Not used by this class much, but having it available makes it much easier to handle
	 * the situation when the city finally gets hit.
	 * @return
	 */
	public City getTarget()
	{
		return this.target;
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}
	
	/**
	 * Updates the position of the bullet
	 * @param delta
	 */
	public void update( float delta )
	{
		// Box2D should deal with collisions of cities, but if, for some reason this doesn't
		// hit a city and keeps moving, get rid of it when it goes off the screen...
		if ( this.b2Body.getPosition().x <= 0.0f )
		{
			this.markForDestruction();
		}
	}
	
	/**
	 * Sets the isAlive flag to false and asks the {@link Game} to remove the associated Box2D object
	 * once the world simulation is done. Next time the {@link Game.update} method is called it will 
	 * remove the because of the {@link isAlive} flag.
	 */
	public void markForDestruction()
	{
		this.isAlive = false;
		Game.getInstance().queueForDestruction( this );
	}
	
	public void render( SpriteBatch batch )
	{
		this.helpDrawSprite( batch );
	}
	
}
