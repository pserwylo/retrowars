package com.serwylo.retrowars.desktop;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.backends.jogl.JoglApplicationConfiguration;
import com.serwylo.peter.retrowars.Retrowars;
import com.serwylo.peter.retrowars.gui.screens.MainMenu;

public class RetrowarsDesktop
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		JoglApplicationConfiguration config = new JoglApplicationConfiguration();
		config.fullscreen = false;
		config.width = 800;
		config.height = 480;
		config.title = "Retrowars";
		
		new JoglApplication( new Retrowars(), config );
	}

}
