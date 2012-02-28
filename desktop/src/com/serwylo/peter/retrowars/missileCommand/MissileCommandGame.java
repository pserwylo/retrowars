package com.serwylo.peter.retrowars.missileCommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.Game;

public class MissileCommandGame extends Game 
{

	public static final int TIME_BETWEEN_ATTACKS = 2000;
	
	/**
	 * The time (in milliseconds) when the next enemy missile will fire.
	 */
	private long nextFire;

	/**
	 * This is kept as a minor convenience, so that when we are firing new enemy missiles,
	 * we don't need to continually iterate over cities looking just for live ones.
	 */
	private ArrayList<City> aliveCities;
	private ArrayList<City> cities;
	private ArrayList<Tower> towers;
	
	private LinkedList<Missile> enemyMissiles = new LinkedList<Missile>();
	private LinkedList<FriendlyMissile> friendlyMissiles = new LinkedList<FriendlyMissile>();
	
	@Override
	public void create()
	{
		super.create();

		this.cities = new ArrayList<City>( 4 );
		this.aliveCities = new ArrayList<City>( 4 );
		for ( int i = 0; i < 4; i ++ )
		{
			City city = new City( 0 );
			this.stage.addActor( city );
			this.cities.add( city );
			this.aliveCities.add( city );
		}
		
		this.towers = new ArrayList<Tower>( 3 );
		for ( int i = 0; i < 3; i ++ )
		{
			Tower tower = new Tower( 0 );
			this.stage.addActor( tower );
			this.towers.add( tower );
		}
		
		this.queueMissile();
	}
	
	/**
	 * Update the stage and its children, then check if we need to fire a new missile.
	 * Also, update the missiles (they are not part of the stage and thus need to be told to
	 * update manually). After updating the missiles, check if they have reached their destination
	 * (the city) and if so, let the city know about it.
	 */
	public void update()
	{
		float delta = Gdx.graphics.getDeltaTime();
		this.stage.act( delta );
		
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
			boolean active = missile.update( delta );
			if ( !active )
			{
				enemyIt.remove();
				this.cityHit( missile.getTarget() );
			}
		}

		// Update all of the friendly missiles, removing if necessary...
		Iterator<FriendlyMissile> it = this.friendlyMissiles.iterator();
		while( it.hasNext() )
		{
			FriendlyMissile missile = it.next();
			boolean active = missile.update( delta );
			if ( !active )
			{
				it.remove();
			}
		}
		
		Input input = Gdx.app.getInput();
		
		if ( input.justTouched() )
		{
			this.fireFriendlyMissile( new Vector2( input.getX(), Gdx.graphics.getHeight() - input.getY() ) );
		}
	
	}
	
	/**
	 * Called when an enemy missile reaches its destination.
	 * Tells the city it has been hit.
	 * Removes the city from list of aliveCities if it has just been killed. 
	 * Adds some graphical and audio feedback.
	 * @param city
	 */
	private void cityHit( City city )
	{
		boolean wasAlive = city.isAlive();
		
		city.hitByMissile();
		
		// If this was the missile which killed it (because you can have dead cities being hit
		// by missiles)...
		if ( wasAlive && !city.isAlive() )
		{
			int index = this.aliveCities.indexOf( city );
			if ( index >= 0 )
			{
				this.aliveCities.remove( index );
			}
			
			if ( this.aliveCities.size() == 0 )
			{
				this.gameOver();
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
			float dist = tower.getPosition().dst2( target );
			if ( tower.readyToFire() && dist < closestDistance )
			{
				closestDistance = dist;
				closestTower = tower;
			}
		}
		
		if ( closestTower != null )
		{
			Vector2 start = closestTower.getPosition();
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
			int startX = (int)( Math.random() * Gdx.graphics.getWidth() );
			int cityIndex = (int)( Math.random() * this.aliveCities.size() );
			Missile missile = new Missile( new Vector2( startX, Gdx.graphics.getHeight() ), this.aliveCities.get( cityIndex ) );
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
	public void render()
	{
		this.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		this.stage.draw();
		
		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		
		for ( Missile missile : this.enemyMissiles )
		{
			missile.render( batch );
		}
		
		for ( FriendlyMissile missile : this.friendlyMissiles )
		{
			missile.render( batch );
		}
		
		batch.end();
	}
	
	/**
	 * Place two towers (@) and four cities (#) appropriately
	 * Here is a mock-up of what it should look like:
	 *  
	 *  |--@--#--#--@--#--#--@--|
	 * 
	 */
	@Override
	public void resize( int width, int height )
	{
		super.resize( width, height );
		
		int spacing = width / 8;
		
		// Tower 1 is at position 1
		this.towers.get( 0 ).getPosition().x = spacing;
		
		// City 1 is at position 2
		this.cities.get( 0 ).getPosition().x = spacing * 2;
		
		// City 2 is at position 3
		this.cities.get( 1 ).getPosition().x = spacing * 3;
		
		// Tower 1 is at position 4
		this.towers.get( 1 ).getPosition().x = spacing * 4;
		
		// City 3 is at position 5
		this.cities.get( 2 ).getPosition().x = spacing * 5;
		
		// City 4 is at position 6
		this.cities.get( 3 ).getPosition().x = spacing * 6;
		
		// Tower 1 is at position 7
		this.towers.get( 2 ).getPosition().x = spacing * 7;
	}

}
