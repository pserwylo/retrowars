package com.serwylo.retrowars.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.serwylo.peter.retrowars.Retrowars;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class RetrowarsActivity extends AndroidApplication
{

	@Override
	public void onCreate ( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// config.useWakelock = true;
		this.initialize( new Retrowars(), config );
	}

	
}