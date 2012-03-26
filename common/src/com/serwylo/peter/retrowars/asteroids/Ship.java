package com.serwylo.peter.retrowars.asteroids;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.serwylo.peter.retrowars.AssetManager;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.GraphicsUtils;

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
	private static final int TIME_BETWEEN_SHOTS = 100;
	
	/**
	 * The time when we last fired a bullet.
	 * Used in conjunction with {@link TIME_BETWEEN_SHOTS} to keep track of how often we fire. 
	 */
	private long lastFireTime = 0;
	
	/**
	 * Impacts the maximum velocity the speed can take, by restricting the terminal velocity.
	 * I'm unsure as to the exact conversion from this to max speed though.
	 */
	private static final float LINEAR_DAMPENING = 5.0f;
	
	/**
	 * Impacts the maximum rotational speed when turning.
	 */
	private static final float ANGULAR_DAMPENING = 15.0f;
	
	/**
	 * Acceleration of the ship. As with MAX_SPEED, the units it is in are currently
	 * meaningless.
	 */
	private static final int ACCELERATION = 50;
	
	/**
	 * Rotation speed (measured as the strength of the (box2d) angular impulse when turning).
	 */
	private static final float ROTATE_SPEED = 1.5f;
	
	private boolean isThrusting = false, isTurningRight = false, isTurningLeft = false, isFiring = false;
	
	private LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	
	private ParticleEmitter particles;
	
	public Ship()
	{
		this.sprite = AssetManager.getShipSprite();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox( 0.5f, 1.0f );
		this.helpInit( new Vector2( 1.0f, 2.0f ), new Vector2( 200, 10 ), shape, Ship.CATEGORY_BIT, Asteroid.CATEGORY_BIT );
		this.b2Body.setAngularDamping( ANGULAR_DAMPENING );
		
		try
		{
			this.particles = new ParticleEmitter( Gdx.files.internal( "particles" ).reader( 1024 ) );
			this.particles.setSprite( AssetManager.getParticleSprite() );
		}
		catch ( IOException ioe )
		{
			
		}
	}
	
	public void thrust( boolean toggle )
	{
		this.isThrusting = toggle;
		if ( this.isThrusting )
		{
			// Limit the speed while we are accelerating...
			this.b2Body.setLinearDamping( LINEAR_DAMPENING );
			this.particles.start();
			this.particles.setSprite( AssetManager.getParticleSprite() );
		}
		else
		{
			// But once we stop accelerating, we just want to float...
			this.b2Body.setLinearDamping( 0.0f );
			this.particles.allowCompletion();
		}
	}
	
	public void turnLeft( boolean toggle )
	{
		this.isTurningLeft = toggle;
	}
	
	public void turnRight( boolean toggle )
	{
		this.isTurningRight = toggle;
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
		GraphicsUtils.wrapObjectAroundScreen( this );
		
		if ( this.isFiring )
		{
			this.fireBullet();
		}
		
		if ( this.isThrusting )
		{
			float angle = MathUtils.radiansToDegrees * this.b2Body.getAngle() + 90;
			Vector2 force = new Vector2( Ship.ACCELERATION, 0 ).rotate( angle );
			this.b2Body.applyForceToCenter( force );
			
			Vector2 pos = this.b2Body.getPosition();
			this.particles.setPosition( pos.x, pos.y );
		}
		
		this.particles.update( delta );

		if ( this.isTurningLeft )
		{
			this.b2Body.applyAngularImpulse( ROTATE_SPEED );
		}

		if ( this.isTurningRight )
		{
			this.b2Body.applyAngularImpulse( -ROTATE_SPEED );
		}
		
		Iterator<Bullet> it = this.bullets.iterator();
		while ( it.hasNext() )
		{
			Bullet bullet = it.next();
			bullet.update( delta );
			if ( !bullet.isAlive() )
			{
				it.remove();
			}
		}
	
	}
	
	/**
	 * Toggles whether or not the ship is firing bullets.
	 * If it is, then bullets will be fired every TIME_BETWEEN_SHOTS milliseconds.
	 * @param toggle
	 */
	public void fire( boolean toggle )
	{
		this.isFiring = toggle;
	}

	/**
	 * Create a new bullet, and add it to the list of bullets so that it will
	 * get updated and rendered to the screen.
	 */
	protected void fireBullet()
	{
		if ( this.lastFireTime + TIME_BETWEEN_SHOTS < System.currentTimeMillis() )
		{
			Bullet bullet = new Bullet( 
				this.b2Body.getPosition(), 
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
		this.helpDrawSprite( batch );
		
		for ( Bullet bullet : this.bullets )
		{
			bullet.render( batch );
		}
		
		this.particles.draw( batch );
	}

	public Vector2 getPosition() 
	{
		return this.b2Body.getPosition();
	}

}
