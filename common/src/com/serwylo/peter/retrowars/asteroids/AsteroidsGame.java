package com.serwylo.peter.retrowars.asteroids;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.Game;

public class AsteroidsGame extends Game
{

	// Input keys for desktop version. The Android version will use a virtual d-pad.
	private static final int KEY_ACCELERATE = Input.Keys.UP;
	private static final int KEY_LEFT = Input.Keys.LEFT;
	private static final int KEY_RIGHT = Input.Keys.RIGHT;
	private static final int KEY_SHOOT = Input.Keys.SPACE;
	
	private Ship ship;
	
	private LinkedList<Asteroid> asteroids = new LinkedList<Asteroid>();
	
	/**
	 * Creates a new asteroid, and places it on the screen, being careful not to place it on top of the ship.
	 * @param size Either {@link Asteroid.SIZE_TINY}, {@link Asteroid.SIZE_SMALL}, {@link Asteroid.SIZE_MEDIUM},
	 * or {@link Asteroid.SIZE_LARGE}.
	 * @return
	 */
	private Asteroid createAsteroid( int size )
	{
		float w = this.worldSize.x;
		float h = this.worldSize.y;
		int minDistanceFromShip = size * 2;
		int minDistanceFromShip2 = minDistanceFromShip * minDistanceFromShip;
		
		Vector2 position = new Vector2();
		
		do
		{
			position.x = (float)( Math.random() * w );
			position.y = (float)( Math.random() * h );
		} while ( Math.abs( this.ship.getPosition().dst2( position ) ) < minDistanceFromShip2 );

		// Come up with a random speed and direction, then convert it to a vector.
		int minSpeed = 20;
		int maxSpeed = 30;
		int speed = (int)( Math.random() * ( maxSpeed - minSpeed ) ) + minSpeed;
		double angle = ( Math.random() * Math.PI * 2 );
		Vector2 velocity = new Vector2( (float)Math.cos( angle ) * speed, (float)Math.sin( angle ) * speed );
		
		Asteroid asteroid = new Asteroid( size, position, velocity );
		this.asteroids.add( asteroid );
		return asteroid;
	}
	
	@Override
	protected void init( int width, int height )
	{
		Gdx.input.setInputProcessor( this.inputHandler );
		
		this.updateCameraViewport( 200 );
		this.ship = new Ship();
		
		this.ship.getPosition().x = width / 2;
		this.ship.getPosition().y = height / 2;

		for ( int i = 0; i < 10; i ++ )
		{
			this.createAsteroid( Asteroid.SIZE_TINY );
			this.createAsteroid( Asteroid.SIZE_SMALL );
			this.createAsteroid( Asteroid.SIZE_MEDIUM );
			/*this.createAsteroid( Asteroid.SIZE_LARGE );*/
		}
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
		batch.getProjectionMatrix().set( this.camera.combined );
		batch.begin();
		Iterator<Asteroid> it = this.asteroids.iterator();
		while ( it.hasNext() )
		{
			Asteroid asteroid = it.next();
			asteroid.render( batch );
		}
		this.ship.render( batch );
		batch.end();
		
		this.renderDebug();
	}

	private InputProcessor inputHandler = new InputProcessor() {
			
		@Override
		public boolean keyDown( int keyCode ) 
		{
			if ( keyCode == KEY_SHOOT )
			{
				ship.fire();
			}
			return true;
		}
	
		@Override
		public boolean keyTyped(char arg0) 
		{
			return false;
		}
	
		@Override
		public boolean keyUp(int arg0) 
		{
			return false;
		}
	
		@Override
		public boolean scrolled(int arg0) 
		{
			return false;
		}
	
		@Override
		public boolean touchDown( int arg0, int arg1, int arg2, int arg3 ) 
		{
			System.out.println( "Touch Down" );
			return false;
		}
	
		@Override
		public boolean touchDragged(int arg0, int arg1, int arg2) 
		{
			return false;
		}
	
		@Override
		public boolean touchMoved(int arg0, int arg1) 
		{
			return false;
		}
	
		@Override
		public boolean touchUp(int arg0, int arg1, int arg2, int arg3) 
		{
			return false;
		}
	};
}
