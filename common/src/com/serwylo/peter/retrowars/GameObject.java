package com.serwylo.peter.retrowars;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject 
{

	public abstract void update( float deltaTime );
	
	public abstract void render( SpriteBatch batch );
	
}
