package com.serwylo.peter.retrowars.missileCommand;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.serwylo.peter.retrowars.SpriteManager;
import com.serwylo.peter.retrowars.asteroids.AsteroidsGame;
import com.serwylo.peter.retrowars.collisions.ICollidable;

public class Missile implements ICollidable
{
	
	public static final int SPEED = 50;
	
	private static Sprite bulletSprite;
	
	private Vector2 position, velocity;
	
	private City target;
	
	private Rectangle boundingRect;
	
	public Missile( Vector2 start, City target )
	{
		this.position = start;
		
		this.velocity = start.cpy().rotate( 180 ).add( target.getPosition() );
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
			bulletSprite.getHeight()
		);

		MissileCommandGame.getQuadTree().insert( this );
	}
	
	/**
	 * Not used by this class much, but having it available makes it much easier to handle
	 * the situation when the city finally gets hit.
	 * @return
	 */
	public City getTarget()
	{
		return this.target;
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
		this.boundingRect.x = this.position.y;

		MissileCommandGame.getQuadTree().update( this );
				
		boolean keep = ( this.position.y > this.target.getPosition().y );

		if ( !keep )
		{
			MissileCommandGame.getQuadTree().remove( this );
		}
		
		return keep;
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
