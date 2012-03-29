package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.serwylo.peter.retrowars.AssetManager;
import com.serwylo.peter.retrowars.RetroGame;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.collisions.ICollidable;

/**
 * Keeps traveling until it hits {@link target}, when it then tells the game that it has
 * reached its destination. Seeing as I can't tell exactly when it hits the target, and it 
 * probably wont exactly hit the target, I will just measure the distance between the
 * target and the missile. When the distance starts to get larger than its smallest value, 
 * it must be moving away from the point again. 
 */
public class FriendlyMissile extends GameObject
{

	public static final short CATEGORY_BIT = 8;
	
	public static final float SPEED = 0.05f;
	
	private boolean isAlive = true;
	
	private static Sprite bulletSprite;
	
	private Vector2 target;
	
	private float distanceToTargetSquared = Float.MAX_VALUE;
	
	public FriendlyMissile( Vector2 start, Vector2 target )
	{
		this.target = target;
		
		if ( bulletSprite == null )
		{
			bulletSprite = AssetManager.getBulletSprite();
		}
		this.setSprite( bulletSprite );
	
		Vector2 size = new Vector2( 0.1f, 0.1f );
		this.helpInit( size, start.cpy(), FriendlyMissile.CATEGORY_BIT, Missile.CATEGORY_BIT );
		
		Vector2 impulse = start.cpy().rotate( 180 ).add( target );
		impulse.nor();
		impulse.x *= SPEED;
		impulse.y *= SPEED;
		
		this.b2Body.applyLinearImpulse( impulse, start );
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}
	
	/**
	 * Updates the position of the bullet
	 * @param delta
	 */
	public void update( float delta )
	{	
		float distSquared = this.b2Body.getPosition().dst2( this.target );
		if ( distSquared < this.distanceToTargetSquared )
		{
			// Still approaching the target...
			this.distanceToTargetSquared = distSquared;
			this.isAlive = true;
		}
		else
		{
			// Notify the game, so that it can replace me with an explosion.
			RetroGame.getInstance().queueForDestruction( this );
			this.isAlive = false;
		}
	}
	
	public void render( SpriteBatch batch )
	{
		this.helpDrawSprite( batch );
	}
	
}
