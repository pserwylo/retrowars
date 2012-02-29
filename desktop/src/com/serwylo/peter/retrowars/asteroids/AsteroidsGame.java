package com.serwylo.peter.retrowars.asteroids;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.Game;

public class AsteroidsGame extends Game
{

	/**
	 * The ship needs to be positioned in the centre of the screen, but when
	 * we create it we don't know where the centre of the screen is. Instead,
	 * we will wait until the first time resize(...) is called. This flag makes
	 * sure we only reposition the ship the first time its resized.
	 */
	private boolean startedFlag = false;
	
	private Ship ship;
	
	private LinkedList<Asteroid> asteroids = new LinkedList<Asteroid>();
	
	public void create()
	{
		super.create();
		this.ship = new Ship();
		this.stage.addActor( this.ship );
	}
	
	/**
	 * Creates a new asteroid, and places it on the screen, being careful not to place it on top of the ship.
	 * @param size Either {@link Asteroid.SIZE_TINY}, {@link Asteroid.SIZE_SMALL}, {@link Asteroid.SIZE_MEDIUM},
	 * or {@link Asteroid.SIZE_LARGE}.
	 * @return
	 */
	private Asteroid createAsteroid( int size )
	{
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
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
	public void resize( int width, int height )
	{
		super.resize( width, height );
		
		// The first time we resize we need to position the ship.
		if ( !startedFlag )
		{
			this.ship.getPosition().x = width / 2;
			this.ship.getPosition().y = height / 2;

			for ( int i = 0; i < 0; i ++ )
			{
				this.createAsteroid( Asteroid.SIZE_TINY );
				this.createAsteroid( Asteroid.SIZE_SMALL );
				this.createAsteroid( Asteroid.SIZE_MEDIUM );
				this.createAsteroid( Asteroid.SIZE_LARGE );
			}
			
			Asteroid a = new Asteroid( Asteroid.SIZE_LARGE, new Vector2( 300, 300 ), new Vector2( -30, -30 ) );
			this.asteroids.add( a );
			
			startedFlag = true;
		}
	}
	
	public void update()
	{
		float delta = Gdx.graphics.getDeltaTime();
		this.stage.act( delta );
		
		Iterator<Asteroid> it = this.asteroids.iterator();
		while ( it.hasNext() )
		{
			Asteroid asteroid = it.next();
			asteroid.update( delta );
		}
	}
	
	public void render()
	{
		this.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
		this.stage.draw();

		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		Iterator<Asteroid> it = this.asteroids.iterator();
		while ( it.hasNext() )
		{
			Asteroid asteroid = it.next();
			asteroid.render( batch );
		}
		batch.end();
	}
	
}
