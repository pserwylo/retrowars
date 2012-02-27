package com.serwylo.peter.retrowars.missileCommand;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.serwylo.peter.retrowars.Game;

public class MissileCommandGame extends Game 
{

	private ArrayList<City> cities = new ArrayList<City>();
	private ArrayList<Tower> towers = new ArrayList<Tower>();
	
	@Override
	public void create()
	{
		super.create();
		for ( int i = 0; i < 4; i ++ )
		{
			City city = new City( 0 );
			this.stage.addActor( city );
			this.cities.add( city );
		}
		
		for ( int i = 0; i < 3; i ++ )
		{
			Tower tower = new Tower( 0 );
			this.stage.addActor( tower );
			this.towers.add( tower );
		}
	}
	
	public void update()
	{
		this.stage.act( Gdx.graphics.getDeltaTime() );
	}
	
	public void render()
	{
		this.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
		this.stage.draw();
	}
	
	/**
	 * Place the cities appropriately.
	 */
	@Override
	public void resize( int width, int height )
	{
		super.resize( width, height );

		// Create three towers (@) and four cities (#) and place them appropriately
		// Here is a mock-up of what it should look like:
		// 
		// |--@--#--#--@--#--#--@--|
		
		int spacing = width / 8;
		
		// Tower 1 is at position 1
		this.towers.get( 0 ).setX( spacing );
		
		// City 1 is at position 2
		this.cities.get( 0 ).setX( spacing * 2 );
		
		// City 2 is at position 3
		this.cities.get( 1 ).setX( spacing * 3 );
		
		// Tower 1 is at position 4
		this.towers.get( 1 ).setX( spacing * 4 );
		
		// City 3 is at position 5
		this.cities.get( 2 ).setX( spacing * 5 );
		
		// City 4 is at position 6
		this.cities.get( 3 ).setX( spacing * 6 );
		
		// Tower 1 is at position 7
		this.towers.get( 2 ).setX( spacing * 7 );
	}

}
