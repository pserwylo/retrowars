package com.serwylo.peter.retrowars;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Provides some basic properties that *most* game objects will have, such as a 
 * Box2D body and a libgdx sprite. This does not mean that *all* game objects must
 * have these. In fact you can simply not use them at all if you want (e.g. if you
 * require several bodies and sprites). The reason they are here is so that
 * utility functions can be provided which will be able to make life easier for
 * the several game objects which *do* have just one sprite and one b2Body.
 */
public abstract class GameObject 
{

	protected Sprite sprite;
	
	protected Body b2Body;
	
	/**
	 * The fixture for {@link b2Body} which defines the shape of the sprites bounding box.
	 */
	protected Fixture b2SpriteFixture;
	
	public abstract void update( float deltaTime );
	
	public abstract void render( SpriteBatch batch );
	
	public Body getB2Body()
	{
		return this.b2Body;
	}
	
	/**
	 * Sets up a Box2D body for the {@link sprite}, and stores the {@link sprite} as a member 
	 * property of the object.
	 * 
	 * If no shape is passed in, then the shape will be a circle with a diameter of half the
	 * {@link sprite}'s height. 
	 * 
	 * The body will be a {@link BodyType.DynamicBody}. 
	 * 
	 * @param sprite
	 * @param position
	 * @param categoryBits
	 * @param maskBits
	 */
	protected void init( Sprite sprite, Vector2 position, Shape shape, short categoryBits, short maskBits )
	{
		BodyDef b2BodyDef = new BodyDef();
		b2BodyDef.position.x = position.x;
		b2BodyDef.position.y = position.y;
		b2BodyDef.type = BodyType.DynamicBody;
		this.b2Body = Game.getInstance().getWorld().createBody( b2BodyDef );

		if ( shape == null )
		{
			shape = new CircleShape();
			shape.setRadius( sprite.getHeight() / 2 );
		}
		
		FixtureDef b2FixtureDef = new FixtureDef();
		b2FixtureDef.shape = shape;
		b2FixtureDef.density = 1;
		b2FixtureDef.restitution = 0.5f;
		b2FixtureDef.filter.categoryBits = categoryBits;
		b2FixtureDef.filter.maskBits = maskBits;
		this.b2SpriteFixture = this.b2Body.createFixture( b2FixtureDef );
		
		shape.dispose();
		
		this.sprite = sprite;
	}

	protected void init( Sprite sprite, Vector2 position, short categoryBits )
	{
		this.init( sprite, position, null, categoryBits, (short)0xFFFF );
	}
	
	protected void init( Sprite sprite, Vector2 position, short categoryBits, short maskBits )
	{
		this.init( sprite, position, null, categoryBits, maskBits );
	}
	
	/**
	 * @param sprite
	 * @param position
	 */
	protected void init( Sprite sprite, Vector2 position )
	{
		this.init( sprite, position, null, (short)0x0001, (short)0xFFFF );
	}
	
}
