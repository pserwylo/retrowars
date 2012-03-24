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
	public static void wrapObjectAroundScreen( GameObject object )
	{
		Vector2 position = object.getB2Body().getPosition();
		
		if ( position.x < 0 )
		{
			position.x = Game.getInstance().getWorldWidth();
		}
		else if ( position.y < 0 )
		{
			position.y = Game.getInstance().getWorldHeight();
		}
		else if ( position.x > Game.getInstance().getWorldWidth() )
		{
			position.x = 0;
		}
		else if ( position.y > Game.getInstance().getWorldHeight() )
		{
			position.y = 0;
		}
		
		object.getB2Body().setTransform( position, object.getB2Body().getAngle() );
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
	public static void drawObjectWithScreenWrap( GameObject object, SpriteBatch batch )
	{
		Vector2 position = object.getB2Body().getPosition();
		
		float x = position.x - object.getWidth() / 2;
		float y = position.y - object.getHeight() / 2;
		
		object.helpDrawSprite( batch, position );
		
		// In the following comparisons, I probably should compare to just sprite.getHeight()/2 
		// and sprite.getWidth()/2, however there is no real harm in wrapping it further than 
		// necessary. It will just result in the sprite being drawn off screen and not being
		// visible...

		boolean crossingTop = ( y + object.getHeight() > Game.getInstance().getWorldHeight() );
		boolean crossingBottom = ( y - object.getHeight() < 0 );
		
		boolean crossingRight = ( x + object.getWidth() > Game.getInstance().getWorldWidth() );
		boolean crossingLeft = ( x - object.getWidth() < 0 );
		
		// Does it cross the TOP of the screen?
		if ( crossingTop )
		{
			object.helpDrawSprite( batch, new Vector2( x, y - Game.getInstance().getWorldHeight() ) );

			// Do we need to draw it in the opposite corner too?
			if ( crossingLeft )
			{
				object.helpDrawSprite( batch, new Vector2( x + Game.getInstance().getWorldWidth(), y - Game.getInstance().getWorldHeight() ) );
			}
			else if ( crossingRight )
			{
				object.helpDrawSprite( batch, new Vector2( x - Game.getInstance().getWorldWidth(), y - Game.getInstance().getWorldHeight() ) );
			}
		}
		// Does it cross the BOTTOM of the screen?
		else if ( crossingBottom )
		{
			object.helpDrawSprite( batch, new Vector2( x, y + Game.getInstance().getWorldHeight() ) );
			
			// Do we need to draw it in the opposite corner too?
			if ( crossingLeft )
			{
				object.helpDrawSprite( batch, new Vector2( x + Game.getInstance().getWorldWidth(), y + Game.getInstance().getWorldHeight() ) );
			}
			else if ( crossingRight )
			{
				object.helpDrawSprite( batch, new Vector2( x - Game.getInstance().getWorldWidth(), y + Game.getInstance().getWorldHeight() ) );
			}
		}
		
		// Does it cross the RIGHT of the screen?
		if ( crossingRight )
		{
			object.helpDrawSprite( batch, new Vector2( x - Game.getInstance().getWorldWidth(), y ) );
		}
		// Does it cross the LEFT of the screen?
		else if ( crossingLeft )
		{
			object.helpDrawSprite( batch, new Vector2( x + Game.getInstance().getWorldWidth(), y ) );
		}
		
	}
	
}
