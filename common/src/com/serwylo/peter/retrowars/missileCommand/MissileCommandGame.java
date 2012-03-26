package com.serwylo.peter.retrowars.missileCommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.serwylo.peter.retrowars.Game;
import com.serwylo.peter.retrowars.asteroids.Asteroid;
import com.serwylo.peter.retrowars.asteroids.Bullet;
import com.serwylo.peter.retrowars.asteroids.AsteroidsGame.AsteroidBulletCollision;
import com.serwylo.peter.retrowars.collisions.DelayedCollisionProcessor;
import com.serwylo.peter.retrowars.scores.GameScore;

public class MissileCommandGame extends Game 
{

	public static final int TIME_BETWEEN_ATTACKS = 2000;
	
	/**
	 * The time (in milliseconds) when the next enemy missile will fire.
	 */
	private long nextFire;

	private MissileCommandScore score = new MissileCommandScore();
	
	/**
	 * This is kept as a minor convenience, so that when we are firing new enemy missiles,
	 * we don't need to continually iterate over cities looking just for live ones.
	 */
	private ArrayList<City> aliveCities;
	private ArrayList<City> cities;
	private ArrayList<Tower> towers;
	
	private LinkedList<Missile> enemyMissiles = new LinkedList<Missile>();
	private LinkedList<FriendlyMissile> friendlyMissiles = new LinkedList<FriendlyMissile>();

	/**
	 * Place two towers (@) and four cities (#) appropriately
	 * Here is a mock-up of what it should look like:
	 *  
	 *  |--@--#--#--@--#--#--@--|
	 */
	@Override
	protected void init( int width, int height ) 
	{
		Gdx.input.setInputProcessor( this.inputProcessor );
		this.world.setContactListener( this.contactListener );
		this.updateCameraViewport( 32, 1 );
		
		this.cities = new ArrayList<City>( 4 );
		this.aliveCities = new ArrayList<City>( 4 );
		for ( int i = 0; i < 4; i ++ )
		{
			City city = new City( 0 );
			this.cities.add( city );
			this.aliveCities.add( city );
		}
		
		this.towers = new ArrayList<Tower>( 3 );
		for ( int i = 0; i < 3; i ++ )
		{
			Tower tower = new Tower( 0 );
			this.towers.add( tower );
		}
	
		float spacing = this.getWorldWidth() / 8;
		
		// Tower 1 is at position 1
		this.towers.get( 0 ).getB2Body().setTransform( new Vector2( spacing, 1.0f ), 0.0f );
		
		// City 1 is at position 2
		this.cities.get( 0 ).getB2Body().setTransform( new Vector2( spacing * 2, 1.0f ), 0.0f );
		
		// City 2 is at position 3
		this.cities.get( 1 ).getB2Body().setTransform( new Vector2( spacing * 3, 1.0f ), 0.0f );
		
		// Tower 1 is at position 4
		this.towers.get( 1 ).getB2Body().setTransform( new Vector2( spacing * 4, 1.0f ), 0.0f );
		
		// City 3 is at position 5
		this.cities.get( 2 ).getB2Body().setTransform( new Vector2( spacing * 5, 1.0f ), 0.0f );
		
		// City 4 is at position 6
		this.cities.get( 3 ).getB2Body().setTransform( new Vector2( spacing * 6, 1.0f ), 0.0f );
		
		// Tower 1 is at position 7
		this.towers.get( 2 ).getB2Body().setTransform( new Vector2( spacing * 7, 1.0f ), 0.0f );
			
		this.queueMissile();
	}

	@Override
	public GameScore getScore() 
	{
		return this.score;
	}
	
	/**
	 * Update the stage and its children, then check if we need to fire a new missile.
	 * Also, update the missiles (they are not part of the stage and thus need to be told to
	 * update manually). After updating the missiles, check if they have reached their destination
	 * (the city) and if so, let the city know about it.
	 */
	@Override
	protected void update( float delta )
	{
		// Is it time to fire another enemy missile down?
		if ( System.currentTimeMillis() > this.nextFire )
		{
			this.fireMissile();
			this.queueMissile();
		}

		// Update all of the enemy missiles, removing if necessary...
		Iterator<Missile> enemyIt = this.enemyMissiles.iterator();
		while ( enemyIt.hasNext() )
		{
			Missile missile = enemyIt.next();
			missile.update( delta );
			if ( !missile.isAlive() )
			{
				enemyIt.remove();
			}
		}

		// Update all of the friendly missiles, removing if necessary...
		Iterator<FriendlyMissile> it = this.friendlyMissiles.iterator();
		while( it.hasNext() )
		{
			FriendlyMissile missile = it.next();
			missile.update( delta );
			if ( !missile.isAlive() )
			{
				it.remove();
			}
		}
	}
	
	/**
	 * Called when all the cities have been killed.
	 */
	private void gameOver()
	{
		
	}
	
	/**
	 * Iterate through the towers and see which one is closest. 
	 * Once identified, a missile will be created which launches from there.
	 * @param target
	 */
	private void fireFriendlyMissile( Vector2 target )
	{
		Tower closestTower = null;
		float closestDistance = Float.MAX_VALUE;
		for ( Tower tower : this.towers )
		{
			float dist = tower.getB2Body().getPosition().dst2( target );
			if ( tower.readyToFire() && dist < closestDistance )
			{
				closestDistance = dist;
				closestTower = tower;
			}
		}
		
		if ( closestTower != null )
		{
			Vector2 start = closestTower.getB2Body().getPosition();
			FriendlyMissile missile = new FriendlyMissile( start, target );
			this.friendlyMissiles.add( missile );
			closestTower.fire();
		}
	}
	
	/**
	 * Creates a new missile on screen, beginning somewhere randomly along the top of screen.
	 * It will target a random city.
	 */
	private void fireMissile()
	{
		if ( this.aliveCities.size() > 0 )
		{
			int startX = (int)( Math.random() * this.getWorldWidth() );
			int cityIndex = (int)( Math.random() * this.aliveCities.size() );
			Missile missile = new Missile( new Vector2( startX, this.getWorldWidth() ), this.aliveCities.get( cityIndex ) );
			this.enemyMissiles.add( missile );
		}
	}
	
	/**
	 * Specify the next time of firing, based on a random range 50% each side of TIME_BETWEEN_ATTACKS.
	 */
	private void queueMissile()
	{
		int timeUntilNextMissile = (int)( TIME_BETWEEN_ATTACKS * ( Math.random() * 0.5 + 1 ) );
		this.nextFire = System.currentTimeMillis() + timeUntilNextMissile;
	}
	
	/**
	 * Before rendering, this will call the update() method.
	 * The stage is responsible for rendering the cities, because they are actors in the scene graph.
	 * The missiles need to be told to render here manually.
	 */
	@Override
	public void render()
	{
		super.render();

		SpriteBatch batch = this.createSpriteBatch();
		batch.begin();

		for ( City city : this.cities )
		{
			city.render( batch );
		}

		for ( Tower tower : this.towers )
		{
			tower.render( batch );
		}
		
		for ( Missile missile : this.enemyMissiles )
		{
			missile.render( batch );
		}
		
		for ( FriendlyMissile missile : this.friendlyMissiles )
		{
			missile.render( batch );
		}
		
		batch.end();
		
		this.renderDebug();
	}
	
	private ContactListener contactListener = new ContactListener() 
	{
		
		@Override
		public void preSolve( Contact contact, Manifold oldManifold ) { }
		
		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) { }
		
		@Override
		public void endContact(Contact contact) { }
		
		@Override
		public void beginContact( Contact contact ) 
		{ 
			Missile missile = null;
			Object a = contact.getFixtureA().getUserData();
			Object b = contact.getFixtureB().getUserData();
			Object other = null;
			
			if ( a instanceof Missile )
			{
				missile = (Missile)a;
				other = b;
			}
			else if ( b instanceof Missile )
			{
				 missile = (Missile)b;
				 other = a;
			}
			
			if ( other instanceof City )
			{
				addCollisionProcessor( new MissileCityDelayedProcessor( missile, (City)other ) );
			}
		}
	};
	
	private InputProcessor inputProcessor = new InputAdapter() 
	{
		
		@Override
		public boolean touchDown( int x, int y, int pointer, int button ) 
		{
			Vector3 pos = new Vector3( x, y, 0.0f );
			camera.unproject( pos );
			fireFriendlyMissile( new Vector2( pos.x, pos.y ) );
			return true;
		}
		
	};

	/**
	 * Created to deal with when an enemy missile reaches its destination.
	 * Tells the city it has been hit.
	 * Removes the city from list of aliveCities if it has just been killed. 
	 * Adds some graphical and audio feedback.
	 */	
	private class MissileCityDelayedProcessor implements DelayedCollisionProcessor
	{

		private Missile missile;
		private City city;
		
		public MissileCityDelayedProcessor( Missile missile, City city )
		{
			this.missile = missile;
			this.city = city;
		}
		

		@Override
		public void process() 
		{
			Gdx.app.log( "HIT", "City, missile" );
			this.missile.markForDestruction();
			
			boolean wasAlive = city.isAlive();
			city.hitByMissile();
			
			// If this was the missile which killed it (because dead cities will still get hit
			// by missiles)...
			if ( wasAlive && !city.isAlive() )
			{
				int index = aliveCities.indexOf( city );
				if ( index >= 0 )
				{
					aliveCities.remove( index );
				}
				
				if ( aliveCities.size() == 0 )
				{
					gameOver();
				}
			}
		}
	};
	
}
