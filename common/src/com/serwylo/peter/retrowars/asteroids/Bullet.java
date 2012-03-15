package com.serwylo.peter.retrowars.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.serwylo.peter.retrowars.Game;
import com.serwylo.peter.retrowars.GameObject;
import com.serwylo.peter.retrowars.GraphicsUtils;
import com.serwylo.peter.retrowars.SpriteManager;
import com.serwylo.peter.retrowars.collisions.ICollidable;

public class Bullet extends GameObject
{
	
	public static final int SPEED = 500;
	
	/**
	 * Number of milliseconds the bullet is alive for. When it has been around for this long,
	 * then it is marked as inactive and the Ship will remove it. As it approaches this,
	 * then it will start to fade away so that it doesn't just disappear from the screen.
	 */
	public static int LIFE = 1250;
	
	/**
	 * The amount of milliseconds before the bullet starts to fade away (should be smaller
	 * than {@link LIFE}.
	 */
	public static int FADE_AFTER = 1000;
	
	private static Sprite bulletSprite;
	
	private long birthTime;
	
	private boolean isAlive;
	
	private Body b2Body;
	
	public Bullet( Vector2 position, Vector2 shipVelocity, float angle )
	{
		if ( bulletSprite == null )
		{
			bulletSprite = SpriteManager.getBulletSprite();
		}

		// Convert the orientation into a velocity vector which will be added to the ships
		// velocity (which was passed into this constructor).
		Vector2 bulletVelocity = new Vector2( 0, 1.0f ).rotate( angle );
		bulletVelocity.x *= SPEED;
		bulletVelocity.y *= SPEED;
		bulletVelocity.add( shipVelocity );

		PolygonShape b2Shape = new PolygonShape();
		b2Shape.setAsBox( bulletSprite.getWidth(), bulletSprite.getHeight() );
		// b2Shape.setRadius( bulletSprite.getWidth() / 2 );
		BodyDef b2BodyDef = new BodyDef();
		b2BodyDef.position.x = position.x;
		b2BodyDef.position.y = position.y;		
		b2BodyDef.linearVelocity.x = bulletVelocity.x;
		b2BodyDef.linearVelocity.y = bulletVelocity.y;
		this.b2Body = Game.getInstance().getWorld().createBody( b2BodyDef );
		this.b2Body.createFixture( b2Shape, 1 );
		b2Shape.dispose();
			
		this.birthTime = System.currentTimeMillis();
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}
	
	/**
	 * Updates the position of the bullet, and marks it as ready to remove if
	 * it has lived for a certain period of time...
	 * @param delta
	 * @return If the bullet has been around longer than {@link LIFE}, then it will 
	 * return false.
	 */
	public void update( float deltaTime )
	{
		GraphicsUtils.wrapVectorAroundScreen( this.b2Body.getPosition() );
		this.isAlive = ( ( System.currentTimeMillis() < this.birthTime + LIFE ) );
	}
	
	public void render( SpriteBatch batch )
	{
		bulletSprite.setPosition( this.b2Body.getPosition().x, this.b2Body.getPosition().y );
		long age = System.currentTimeMillis() - this.birthTime;
		float alpha = 1.0f;
		if ( age > FADE_AFTER )
		{
			alpha = (float)( LIFE - age )  / FADE_AFTER;
		}
		bulletSprite.draw( batch, alpha );
	}
	
}
