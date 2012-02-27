package com.serwylo.peter.retrowars.asteroids;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;

/**
 * @author Peter Serwylo
 */
public class Ship extends Actor
{

	// Input keys for desktop version. The Android version will use a virtual d-pad.
	private static final int KEY_ACCELERATE = Input.Keys.UP;
	private static final int KEY_LEFT = Input.Keys.LEFT;
	private static final int KEY_RIGHT = Input.Keys.RIGHT;
	private static final int KEY_SHOOT = Input.Keys.SPACE;

	/**
	 * Maximum velocity the speed can take. 
	 * I am unsure of the units yet, right now its just pixels...
	 */
	private static final int MAX_SPEED = 3;
	
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
	
	private Vector2 position, velocity, orientation;
	
	private boolean isThrusting = false;
	
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public Ship()
	{
		this.shipSprite = SpriteManager.getShipSprite();
		this.position = new Vector2( 0, 0 );
		this.velocity = new Vector2( 0, 0 );
		this.orientation = new Vector2( 0, ACCELERATION );
	}
	
	/**
	 * Current position of the ship.
	 * @return
	 */
	public Vector2 getPosition()
	{
		return this.position;
	}
	
	@Override
	public void act( float delta )
	{
		this.position.add( this.velocity );
		GraphicsUtils.wrapScreen( this.position );
		
		for ( Bullet bullet : this.bullets )
		{
			if ( !bullet.update( delta ) )
			{
				// bullets.remove( bullet );	
			}
		}
		
		Input input = Gdx.app.getInput();
		this.isThrusting = ( input.isKeyPressed( KEY_ACCELERATE ) );
		if ( this.isThrusting )
		{
			this.velocity.add( new Vector2( this.orientation ).mul( delta ) );
			System.out.println( this.velocity.len() );
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
		
		if ( input.isKeyPressed( KEY_SHOOT ) )
		{
			this.fire();
		}
	}
	
	/**
	 * Create a new bullet, and add it to the list of bullets so that it will
	 * get updated and rendered to the screen.
	 */
	private void fire()
	{
		Bullet bullet = new Bullet( 
			new Vector2( this.position ), 
			new Vector2( this.velocity ), 
			new Vector2( this.orientation ) 
		);
		this.bullets.add( bullet );
	}
	
	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) 
	{
		this.shipSprite.setRotation( this.orientation.angle() - 90 );
		this.shipSprite.setPosition( this.position.x, this.position.y );
		this.shipSprite.draw( batch );

		for ( Bullet bullet : this.bullets )
		{
			bullet.render( batch );
		}
	}

	@Override
	public Actor hit( float x, float y ) 
	{
		return null;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2) 
	{
		return false;
	}

	@Override
	public void touchDragged(float arg0, float arg1, int arg2) 
	{	
	
	}

	@Override
	public void touchUp(float arg0, float arg1, int arg2) 
	{
		
	}
	
}
