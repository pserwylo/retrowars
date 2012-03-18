package com.serwylo.peter.retrowars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GraphicsUtils 
{

	public static void drawDebugRect( Rectangle rect, Camera camera )
	{
		GraphicsUtils.drawDebugRect( rect, camera, 1.0f, 1.0f, 1.0f );
	}
	
	public static void drawDebugRect( Rectangle rect, Camera camera, float r, float g, float b )
	{
    	ImmediateModeRenderer10 debug = new ImmediateModeRenderer10( 4 );
    	debug.begin( camera.combined, GL10.GL_LINE_LOOP );
	    
	    	debug.color( r, g, b, 1.0f );
    		debug.vertex( rect.x, rect.y, 0 );

	    	debug.color( r, g, b, 1.0f );
	    	debug.vertex( rect.x + rect.width, rect.y , 0 );

	    	debug.color( r, g, b, 1.0f );
	    	debug.vertex( rect.x + rect.width, rect.y + rect.height, 0 );

	    	debug.color( r, g, b, 1.0f );
	    	debug.vertex( rect.x, rect.y + rect.height, 0 );
    	
	    debug.end();
	}
	
	/**
	 * Given a vector, if it passes either end of the screen, then we will move it to the opposite
	 * side. 
	 * @param position
	 */
	public static void wrapVectorAroundScreen( Vector2 position )
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
	
	/**
	 * Renders a sprite all around the screen, depending on if it is near the edge or not.
	 * For example, if it is a large sprite, and it is partially overlapping with the top right 
	 * corner, then we want it to partially be drawn in the bottom left corner (whichever part
	 * of the sprite was wrapping around).
	 * 
	 * Keep in mind that the sprite will be centred around the position variable. 
	 * 
	 * @param sprite
	 * @param position
	 * @param batch
	 */
	public static void drawSpriteWithScreenWrap( Sprite sprite, Vector2 position, SpriteBatch batch )
	{
		float x = position.x - sprite.getWidth() / 2;
		float y = position.y - sprite.getHeight() / 2;
		
		sprite.setPosition( x, y );
		sprite.draw( batch );
		
		// In the following comparisons, I probably should compare to just sprite.getHeight()/2 
		// and sprite.getWidth()/2, however there is no real harm in wrapping it further than 
		// necessary. It will just result in the sprite being drawn off screen and not being
		// visible...

		boolean crossingTop = ( y + sprite.getHeight() > Gdx.graphics.getHeight() );
		boolean crossingBottom = ( y - sprite.getHeight() < 0 );
		
		boolean crossingRight = ( x + sprite.getWidth() > Gdx.graphics.getWidth() );
		boolean crossingLeft = ( x - sprite.getWidth() < 0 );
		
		// Does it cross the TOP of the screen?
		if ( crossingTop )
		{
			sprite.setPosition( x, y - Gdx.graphics.getHeight() );
			sprite.draw( batch );

			// Do we need to draw it in the opposite corner too?
			if ( crossingLeft )
			{
				sprite.setPosition( x + Gdx.graphics.getWidth(), y - Gdx.graphics.getHeight() );
				sprite.draw( batch );
			}
			else if ( crossingRight )
			{
				sprite.setPosition( x - Gdx.graphics.getWidth(), y - Gdx.graphics.getHeight() );
				sprite.draw( batch );
			}
		}
		// Does it cross the BOTTOM of the screen?
		else if ( crossingBottom )
		{
			sprite.setPosition( x, y + Gdx.graphics.getHeight() );
			sprite.draw( batch );

			// Do we need to draw it in the opposite corner too?
			if ( crossingLeft )
			{
				sprite.setPosition( x + Gdx.graphics.getWidth(), y + Gdx.graphics.getHeight() );
				sprite.draw( batch );
			}
			else if ( crossingRight )
			{
				sprite.setPosition( x - Gdx.graphics.getWidth(), y + Gdx.graphics.getHeight() );
				sprite.draw( batch );
			}
		}
		
		// Does it cross the RIGHT of the screen?
		if ( crossingRight )
		{
			sprite.setPosition( x - Gdx.graphics.getWidth(), y );
			sprite.draw( batch );
		}
		// Does it cross the LEFT of the screen?
		else if ( crossingLeft )
		{
			sprite.setPosition( x + Gdx.graphics.getWidth(), y );
			sprite.draw( batch );
		}
		
	}
	
}
