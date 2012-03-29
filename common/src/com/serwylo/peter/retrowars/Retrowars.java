package com.serwylo.peter.retrowars;

import com.badlogic.gdx.ApplicationListener;
import com.serwylo.peter.retrowars.asteroids.AsteroidsGame;
import com.serwylo.peter.retrowars.gui.screens.MainMenu;
import com.serwylo.peter.retrowars.missileCommand.MissileCommandGame;

public class Retrowars extends com.badlogic.gdx.Game implements ApplicationListener 
{

	@Override
	public void create() 
	{
		this.setScreen( MainMenu.getInstance( this ) );
	}

}
