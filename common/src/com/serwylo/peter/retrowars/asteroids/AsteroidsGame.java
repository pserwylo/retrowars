package com.serwylo.peter.retrowars.asteroids;

import java.util.Iterator;
import java.util.LinkedList;

import retrowars.scoring.AsteroidsScore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.serwylo.peter.retrowars.Game;
import com.serwylo.peter.retrowars.collisions.DelayedCollisionProcessor;
import com.serwylo.peter.retrowars.scores.GameScore;

public class AsteroidsGame extends Game implements ContactListener
{

	// Input keys for desktop version. The Android version will use a virtual d-pad.
	private static final int KEY_ACCELERATE = Input.Keys.UP;
	private static final int KEY_LEFT = Input.Keys.LEFT;
	private static final int KEY_RIGHT = Input.Keys.RIGHT;
	private static final int KEY_SHOOT = Input.Keys.SPACE;
	
	private Ship ship;
	
	private AsteroidsScore score;
	
	private LinkedList<Asteroid> asteroids = new LinkedList<Asteroid>();
	
	/**
	 * Creates a new asteroid, and places it on the screen, being careful not to place it on top of the ship.
	 * @param size Either {@link Asteroid.SIZE_TINY}, {@link Asteroid.SIZE_SMALL}, {@link Asteroid.SIZE_MEDIUM},
	 * or {@link Asteroid.SIZE_LARGE}.
	 * @return
	 */
	private Asteroid createAsteroid( int size )
	{
		float w = this.getWorldWidth();
		float h = this.getWorldHeight();
		int minDistanceFromShip = size * 2;
		int minDistanceFromShip2 = minDistanceFromShip * minDistanceFromShip;
		
		Vector2 position = new Vector2();
		
		do
		{
			position.x = (float)( Math.random() * w );
			position.y = (float)( Math.random() * h );
		} while ( Math.abs( this.ship.getPosition().dst2( position ) ) < minDistanceFromShip2 );
		
		Asteroid asteroid = new Asteroid( size, position, Asteroid.generateRandomVelocity() );
		this.asteroids.add( asteroid );
		return asteroid;
	}
	
	@Override
	protected void init( int width, int height )
	{
		Gdx.input.setInputProcessor( this.inputHandler );
		this.world.setContactListener( this );
		
		this.updateCameraViewport( 10, 1 );
		this.ship = new Ship();
		
		this.ship.getB2Body().setTransform( this.getWorldWidth() / 2, this.getWorldHeight() / 2, 0.0f );

		for ( int i = 0; i < 1; i ++ )
		{
			this.createAsteroid( Asteroid.SIZE_TINY );
			this.createAsteroid( Asteroid.SIZE_SMALL );
			this.createAsteroid( Asteroid.SIZE_MEDIUM );
			this.createAsteroid( Asteroid.SIZE_LARGE );
		}
	}
	
	public GameScore getScore()
	{
		return this.score;
	}
	
	@Override
	protected void update( float deltaTime )
	{
		Iterator<Asteroid> it = this.asteroids.iterator();
		while ( it.hasNext() )
		{
			Asteroid asteroid = it.next();
			asteroid.update( deltaTime );
		}
		
		this.ship.update( deltaTime );
	}
	
	@Override
	public void render()
	{
		super.render();

		SpriteBatch batch = new SpriteBatch();
		batch.setProjectionMatrix( this.camera.projection );
		batch.setTransformMatrix( this.camera.view );
		batch.begin();
		Iterator<Asteroid> it = this.asteroids.iterator();
		while ( it.hasNext() )
		{
			Asteroid asteroid = it.next();
			asteroid.render( batch );
		}
		this.ship.render( batch );
		batch.end();
		
		// this.renderDebug();
	}

	private InputProcessor inputHandler = new InputAdapter() {
			
		@Override
		public boolean keyDown( int keyCode ) 
		{
			if ( keyCode == KEY_SHOOT )
			{
				ship.fire( true );
			}
			else if ( keyCode == KEY_ACCELERATE )
			{
				ship.thrust( true );
			}
			else if ( keyCode == KEY_LEFT )
			{
				ship.turnLeft( true );
			}
			else if ( keyCode == KEY_RIGHT )
			{
				ship.turnRight( true );
			}
			return true;
		}
	
		@Override
		public boolean keyUp( int keyCode ) 
		{
			if ( keyCode == KEY_SHOOT )
			{
				ship.fire( false );
			}
			else if ( keyCode == KEY_ACCELERATE )
			{
				ship.thrust( false );
			}
			else if ( keyCode == KEY_LEFT )
			{
				ship.turnLeft( false );
			}
			else if ( keyCode == KEY_RIGHT )
			{
				ship.turnRight( false );
			}
			return true;
		}
	
		@Override
		public boolean touchDown( int arg0, int arg1, int arg2, int arg3 ) 
		{
			System.out.println( "Touch Down" );
			return false;
		}
		
	};

	@Override
	public void beginContact( Contact contact ) 
	{
		Asteroid asteroid = null;
		Object a = contact.getFixtureA().getUserData();
		Object b = contact.getFixtureB().getUserData();
		Object other = null;
		
		if ( a instanceof Asteroid )
		{
			asteroid = (Asteroid)a;
			other = b;
		}
		else if ( b instanceof Asteroid )
		{
			 asteroid = (Asteroid)b;
			 other = a;
		}
		
		if ( other == this.ship )
		{
		}
		else if ( other instanceof Bullet )
		{
			this.addCollisionProcessor( new AsteroidBulletCollision( asteroid, (Bullet)other ) );
		}
	}
	
	@Override
	public void endContact( Contact contact ) 
	{
		
	}

	@Override
	public void preSolve( Contact contact, Manifold oldManifold ) 
	{
		
	}

	@Override
	public void postSolve( Contact contact, ContactImpulse impulse ) 
	{
		
	}
	
	/**
	 * The collision processors need to be subclasses, so that they can access the state of 
	 * the world in an easy way. This processor needs to remove and (maybe) add new asteroids.
	 */
	public class AsteroidBulletCollision implements DelayedCollisionProcessor
	{
		
		private Asteroid asteroid;
		private Bullet bullet;
		
		public AsteroidBulletCollision( Asteroid asteroid, Bullet bullet )
		{
			this.asteroid = asteroid;
			this.bullet = bullet;
		}
		
		/**
		 * Removes the asteroid and the bullet from the screen.
		 * If the asteroid was not a {@link Asteroid.SIZE_TINY} asteroid, then it will be split into
		 * more, smaller asteroids.
		 */
		public void process()
		{
			int size = this.asteroid.getSize();
			if ( size > Asteroid.SIZE_TINY )
			{
				Vector2 originalVelocity = this.asteroid.getB2Body().getLinearVelocity();
				for ( int i = 0; i < 3; i ++ )
				{
					Asteroid newAsteroid = new Asteroid( size - 1, this.asteroid.getB2Body().getPosition(), Asteroid.generateRandomVelocity().add( originalVelocity ) );
					asteroids.add( newAsteroid );
				}
			}
			
			this.bullet.markForDestruction();
			asteroids.remove( this.asteroid );
			queueForDestruction( this.asteroid );
		}
	}
}
