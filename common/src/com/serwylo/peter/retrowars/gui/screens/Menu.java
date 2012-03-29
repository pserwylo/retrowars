package com.serwylo.peter.retrowars.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.serwylo.peter.retrowars.Retrowars;

abstract class Menu implements Screen 
{

	private boolean isInitialized = false;
	
	protected Stage stage;
	
	protected Skin uiSkin;
	
	protected Table uiTable;
	
	/**
	 * Required so that we can ask for other screens to be displayed.
	 */
	protected Retrowars app;

	public Menu( Retrowars app )
	{
		this.app = app;
	}
	
	protected abstract void init( int width, int height );
	
	@Override
	public void resize(int width, int height) 
	{
		if ( !this.isInitialized )
		{
			this.stage = new Stage( width, height, true );
	        this.uiSkin = new Skin( Gdx.files.internal( "ui/uiskin.json" ), Gdx.files.internal( "ui/uiskin.png") );
	        Gdx.input.setInputProcessor( this.stage );

	        this.uiTable = new Table( this.uiSkin );
	        this.uiTable.width = width;
	        this.uiTable.height = height;
	        this.stage.addActor( this.uiTable );
	        
			this.init( width, height );
		}
	}

	@Override
	public void render( float delta ) 
	{
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        this.stage.act( Gdx.graphics.getDeltaTime() );
        this.stage.draw();
        // Table.drawDebug( stage );
	}
	
	@Override
	public void show() 
	{
		
	}

	@Override
	public void hide() 
	{
		
	}

	@Override
	public void pause() 
	{
		
	}

	@Override
	public void resume() 
	{
		
	}

	@Override
	public void dispose() 
	{
		
	}

}
