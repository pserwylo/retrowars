package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.SpriteManager;
import com.serwylo.peter.retrowars.collisions.ICollidable;

/**
 * Keeps traveling until it hits {@link target}, when it then tells the game that it has
 * reached its destination. Seeing as I can't tell exactly when it hits the target, and it 
 * probably wont exactly hit the target, I will just measure the distance between the
 * target and the missile. When the distance starts to get larger than its smallest value, 
 * it must be moving away from the point again. 
 * @author pete
 *
 */
public class FriendlyMissile implements ICollidable
{
	
	public static final int SPEED = 150;
	
	private static Sprite bulletSprite;
	
	private Vector2 position, velocity, target;
	
	private float distanceToTargetSquared = Float.MAX_VALUE;
	
	private Rectangle boundingRect;
	
	public FriendlyMissile( Vector2 start, Vector2 target )
	{
		this.position = start.cpy();
		
		this.velocity = start.cpy().rotate( 180 ).add( target );
		this.velocity.nor();
		this.velocity.x *= SPEED;
		this.velocity.y *= SPEED;
		
		this.target = target;
		
		if ( bulletSprite == null )
		{
			bulletSprite = SpriteManager.getBulletSprite();
		}
		
		this.boundingRect = new Rectangle(
			this.position.x,
			this.position.y,
			bulletSprite.getWidth(),
			bulletSprite.getHeight());
	}
	
	/**
	 * Updates the position of the bullet
	 * @param delta
	 */
	public boolean update( float delta )
	{
		this.position.x += this.velocity.x * delta;
		this.position.y += this.velocity.y * delta;
	
		this.boundingRect.x = this.position.x;
		this.boundingRect.y = this.position.y;
		
		float distSquared = this.position.dst2( this.target );
		if ( distSquared < this.distanceToTargetSquared )
		{
			// Still approaching the target...
			this.distanceToTargetSquared = distSquared;
			return true;
		}
		else
		{
			// Notify the game, so that it can replace me with an explosion.
			return false;
		}
	}
	
	public void render( SpriteBatch batch )
	{
		bulletSprite.setPosition( this.position.x, this.position.y );
		bulletSprite.draw( batch );
	}

	@Override
	public Rectangle getBoundingRect() 
	{
		return this.boundingRect;
	}
	
}
