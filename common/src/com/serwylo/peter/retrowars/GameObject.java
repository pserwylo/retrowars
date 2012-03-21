package com.serwylo.peter.retrowars;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
	
	/**
	 * Width (in metres) of the object in the world.
	 */
	protected float width = 0.0f;

	/**
	 * Height (in metres) of the object in the world.
	 */
	protected float height = 0.0f;
	
	public abstract void update( float deltaTime );
	
	public abstract void render( SpriteBatch batch );
	
	public Body getB2Body()
	{
		return this.b2Body;
	}
	
	protected void helpDrawSprite( SpriteBatch batch )
	{
		Vector2 pos = this.b2Body.getPosition();
		
		this.sprite.setOrigin( this.width / 2, this.height / 2 );
		this.sprite.setSize( this.width, this.height );
		this.sprite.setRotation( MathUtils.radiansToDegrees * this.b2Body.getAngle() );
		this.sprite.setPosition( pos.x - this.width / 2, pos.y - this.height / 2 );
		this.sprite.draw( batch );
		
		/*batch.draw( 
			this.sprite, 
			pos.x - this.width / 2, 
			pos.y - this.height / 2, 
			this.width, 
			this.height
		);*/
	}
	
	/**
	 * Sets up a Box2D body for this game object.
	 * 
	 * Stores the {@link size} as {@link width} and {@link height} member properties.
	 * 
	 * If no shape is passed in, then the shape will be a circle with a diameter of half the
	 * {@link size}'s x (width) component. 
	 * 
	 * The body will be a {@link BodyType.DynamicBody}. 
	 * 
	 * @param size
	 * @param position
	 * @param shape If not null, this WILL BE DISPOSED OF when done.
	 * @param categoryBits
	 * @param maskBits
	 */
	protected void helpInit( 
		Vector2 size,
		Vector2 position, 
		Shape shape, 
		short categoryBits, 
		short maskBits )
	{
		this.width = size.x;
		this.height = size.y;
		
		BodyDef b2BodyDef = new BodyDef();
		b2BodyDef.position.x = position.x;
		b2BodyDef.position.y = position.y;
		b2BodyDef.type = BodyType.DynamicBody;
		this.b2Body = Game.getInstance().getWorld().createBody( b2BodyDef );

		if ( shape == null )
		{
			shape = new CircleShape();
			shape.setRadius( width / 2 );
		}
		
		FixtureDef b2FixtureDef = new FixtureDef();
		b2FixtureDef.shape = shape;
		b2FixtureDef.density = 1;
		b2FixtureDef.restitution = 0.5f;
		b2FixtureDef.filter.categoryBits = categoryBits;
		b2FixtureDef.filter.maskBits = maskBits;
		this.b2SpriteFixture = this.b2Body.createFixture( b2FixtureDef );
		
		shape.dispose();
	}

	protected void helpInit( Vector2 size, Vector2 position, short categoryBits )
	{
		this.helpInit( size, position, null, categoryBits, (short)0xFFFF );
	}

	protected void helpInit( Vector2 size, Vector2 position, Shape shape, short categoryBits )
	{
		this.helpInit( size, position, shape, categoryBits, (short)0xFFFF );
	}
	
	protected void helpInit( Vector2 size, Vector2 position, short categoryBits, short maskBits )
	{
		this.helpInit( size, position, null, categoryBits, maskBits );
	}
	
	/**
	 * Set
	 * @param size
	 * @param position
	 */
	protected void helpInit( Vector2 size, Vector2 position )
	{
		this.helpInit( size, position, null, (short)0x0001, (short)0xFFFF );
	}
	
}
