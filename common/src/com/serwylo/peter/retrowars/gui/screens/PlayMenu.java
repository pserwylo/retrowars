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
import com.serwylo.peter.retrowars.asteroids.AsteroidsGame;
import com.serwylo.peter.retrowars.missileCommand.MissileCommandGame;

public class PlayMenu extends Menu implements ClickListener
{

	private static PlayMenu singleton;

	public static PlayMenu getInstance( Retrowars app )
	{
		if ( singleton == null )
		{
			singleton = new PlayMenu( app );
		}
		return singleton;
	}
	
	/**
	 * Keep references to the menu buttons so that we can use ourself as a 
	 * click listener.
	 */
	private TextButton missileCommandButton, asteroidsButton, backButton;
	
	public PlayMenu( Retrowars app )
	{        
		super( app );
	}
	
	@Override
	public void click( Actor actor, float x, float y )
	{
		if ( actor == this.backButton )
		{
			this.app.setScreen( MainMenu.getInstance( this.app ) );
		}
		else if ( actor == this.missileCommandButton )
		{
			MissileCommandGame game = new MissileCommandGame();
			this.app.setScreen( game );
		}
		else if ( actor == this.asteroidsButton )
		{
			AsteroidsGame game = new AsteroidsGame();
			this.app.setScreen( game );
		}
	}
	
	@Override
	protected void init( int width, int height )
	{
        TableLayout layout = this.uiTable.getTableLayout();

        this.asteroidsButton = new TextButton( "Asteroids", this.uiSkin.getStyle( TextButtonStyle.class ), "asteroidsButton" );
        this.asteroidsButton.setClickListener( this );
        layout.register( this.asteroidsButton );

        this.missileCommandButton = new TextButton( "Missile Command", this.uiSkin.getStyle( TextButtonStyle.class ), "missileCommandButton" );
        this.missileCommandButton.setClickListener( this );
        layout.register( this.missileCommandButton );

        this.backButton = new TextButton( "Back", this.uiSkin.getStyle( TextButtonStyle.class ), "backButton" );
        this.backButton.setClickListener( this );
        layout.register( this.backButton );

        layout.parse( Gdx.files.internal( "ui/screens/play-menu.txt" ).readString() );
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
