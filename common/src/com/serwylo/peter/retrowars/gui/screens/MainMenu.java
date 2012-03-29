package com.serwylo.peter.retrowars.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.serwylo.peter.retrowars.Retrowars;

public class MainMenu extends Menu implements ClickListener
{

	private static MainMenu singleton;

	public static MainMenu getInstance( Retrowars app )
	{
		if ( singleton == null )
		{
			singleton = new MainMenu( app );
		}
		return singleton;
	}
	
	/**
	 * Keep references to the menu buttons so that we can use ourself as a 
	 * click listener.
	 */
	private TextButton playButton, optionsButton, quitButton;
	
	public MainMenu( Retrowars app )
	{
		super( app );
	}
	
	@Override
	public void click( Actor actor, float x, float y )
	{
		if ( actor == this.playButton )
		{
			this.app.setScreen( PlayMenu.getInstance( this.app ) );
		}
		else if ( actor == this.quitButton )
		{
			Gdx.app.exit();
		}
	}
	
	@Override
	protected void init( int width, int height )
	{   
        TableLayout layout = this.uiTable.getTableLayout();

        this.playButton = new TextButton( "Play!", this.uiSkin.getStyle( TextButtonStyle.class ), "playButton" );
        this.playButton.setClickListener( this );
        layout.register( this.playButton );

        this.optionsButton = new TextButton( "Options", uiSkin.getStyle( TextButtonStyle.class ), "optionsButton" );
        this.optionsButton.setClickListener( this );
        layout.register( this.optionsButton );
         
        this.quitButton = new TextButton( "Quit", uiSkin.getStyle( TextButtonStyle.class ), "quitButton" );
        this.quitButton.setClickListener( this );
        layout.register( this.quitButton );

        layout.parse( Gdx.files.internal( "ui/screens/main-menu.txt" ).readString() );
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
