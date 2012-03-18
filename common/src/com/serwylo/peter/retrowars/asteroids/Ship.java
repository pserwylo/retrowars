package com.serwylo.peter.retrowars.asteroids;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;

/**
 * @author Peter Serwylo
 */
public class Ship extends GameObject
{

	/**
	 * Used for Box2D collision filtering.
	 */
	public static final short CATEGORY_BIT = 8;

	/**
	 * The number of milliseconds it takes to reload, before we can fire another bullet.
	 * Used in conjunction with {@link lastFireTime} to keep track of how often we fire.
	 */
	private static final int TIME_BETWEEN_SHOTS = 50;
	
	/**
	 * The time when we last fired a bullet.
	 * Used in conjunction with {@link TIME_BETWEEN_SHOTS} to keep track of how often we fire. 
	 */
	private long lastFireTime = 0;
	
	/**
	 * Maximum velocity the speed can take. 
	 * I am unsure of the units yet, right now its just pixels...
	 */
	private static final int MAX_SPEED = 5;
	
	/**
	 * Acceleration of the ship. As with MAX_SPEED, the units it is in are currently
	 * meaningless.
	 */
	private static final int ACCELERATION = 5;
	
	/**
	 * Rotation speed (degrees per second).
	 */
	private static final int ROTATE_SPEED = 360;
	
	private Sprite shipSprite;
	
	private boolean isThrusting = false;
	
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public Ship()
	{
		this.shipSprite = SpriteManager.getShipSprite();
		
		this.init( SpriteManager.getShipSprite(), new Vector2( 200, 10 ), Ship.CATEGORY_BIT, Asteroid.CATEGORY_BIT );
	}
	
	public void setThrusting( boolean isThrusting )
	{
		this.isThrusting = isThrusting;
	}
	
	public boolean isThrusting()
	{
		return this.isThrusting;
	}
	
	/**
	 * Moves the ship according to its velocity.
	 * Checks input to decide if we need to fire or not.
	 * Will also tell its bullets to update themselves (because they are not part of
	 * the scene graph and need to be updated manually).
	 */
	@Override
	public void update( float delta )
	{
		GraphicsUtils.wrapVectorAroundScreen( this.b2Body.getPosition() );
		
		Input input = Gdx.app.getInput();
		
		/*
		Iterator<Bullet> it = this.bullets.iterator();
		while( it.hasNext() )
		{
			Bullet bullet = it.next();
			if ( !bullet.update( delta ) )
			{
				it.remove();	
			}
		}
		
		this.isThrusting = ( input.isKeyPressed( KEY_ACCELERATE ) );
		if ( this.isThrusting )
		{
			this.velocity.add( new Vector2( this.orientation ).mul( delta ) );
			if ( this.velocity.len2() > MAX_SPEED * MAX_SPEED )
			{
				this.velocity = this.velocity.nor().mul( MAX_SPEED ); 
			}
		}

		if ( input.isKeyPressed( KEY_LEFT ) )
		{
			this.orientation.rotate( ROTATE_SPEED * delta );
		}
		
		if ( input.isKeyPressed( KEY_RIGHT ) )
		{
			this.orientation.rotate( -ROTATE_SPEED * delta );
		}
		*/
		
	}
	
	/**
	 * Create a new bullet, and add it to the list of bullets so that it will
	 * get updated and rendered to the screen.
	 */
	public void fire()
	{
		if ( this.lastFireTime + TIME_BETWEEN_SHOTS < System.currentTimeMillis() )
		{
			Bullet bullet = new Bullet( 
				this.b2Body.getPosition().cpy(), 
				this.b2Body.getLinearVelocity(), 
				this.b2Body.getAngle() 
			);
			this.bullets.add( bullet );
			this.lastFireTime = System.currentTimeMillis();
		}
	}

	@Override
	public void render( SpriteBatch batch ) 
	{

		this.shipSprite.setRotation( this.b2Body.getAngle() );
		GraphicsUtils.drawSpriteWithScreenWrap( this.shipSprite, this.b2Body.getPosition(), batch );

		for ( Bullet bullet : this.bullets )
		{
			bullet.render( batch );
		}
	}

	public Vector2 getPosition() 
	{
		return this.b2Body.getPosition();
	}
	
}
