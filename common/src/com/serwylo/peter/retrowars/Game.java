package com.serwylo.peter.retrowars;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.serwylo.peter.retrowars.collisions.ICollidable;
import com.serwylo.peter.retrowars.collisions.QuadTree;

public abstract class Game implements ApplicationListener 
{

	protected Stage stage;
	
	protected static QuadTree<ICollidable> quadTree;
	
	public static QuadTree<ICollidable> getQuadTree() 
	{ 
		return quadTree; 
	}
	
	@Override
	public void create() 
	{
		this.stage = new Stage( 0, 0, true );
	}

	@Override
	public void dispose() 
	{
		this.stage.dispose();
	}

	@Override
	public void pause() 
	{
	
	}

	@Override
	public void resize( int width, int height ) 
	{
		this.stage.setViewport( width, height, true );
		quadTree = new QuadTree<ICollidable>( new Rectangle( 0, 0, width, height ) );
	}

	@Override
	public void resume() 
	{
		
	}

}
