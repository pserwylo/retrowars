package com.serwylo.peter.retrowars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class GraphicsUtils 
{

	public static void wrapScreen( Vector2 position )
	{
		if ( position.x < 0 )
		{
			position.x = Gdx.graphics.getWidth();
		}
		else if ( position.y < 0 )
		{
			position.y = Gdx.graphics.getHeight();
		}
		else if ( position.x > Gdx.graphics.getWidth() )
		{
			position.x = 0;
		}
		else if ( position.y > Gdx.graphics.getHeight() )
		{
			position.y = 0;
		}
	}
	
}
