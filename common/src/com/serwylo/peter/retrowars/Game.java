package com.serwylo.peter.retrowars;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.serwylo.peter.retrowars.collisions.ICollidable;
import com.serwylo.peter.retrowars.collisions.QuadTree;

public abstract class Game implements ApplicationListener 
{

	/**
	 * libgdx scenegraph used to render the world.
	 */
	protected Stage stage;
	
	/**
	 * @see getWorld
	 */
	protected World world;

	/**
	 * Box2D world object, which keeps track of all the physical objects in the scene.
	 */
	public World getWorld()
	{
		return this.world;
	}
	
	/**
	 * The camera, which is setup to work in a 2D environment, and configured so that we
	 * can think in metres, rather than pixels. This will not only help appease Box2D which
	 * prefers these types of units, but also makes it easier to specify speeds of objects
	 * which seem reasonable.
	 */
	protected OrthographicCamera camera;
	
	/**
	 * @deprecated (For when I was going to deal with the collisions myself, now I want to
	 * use Box2D).
	 */
	protected QuadTree<ICollidable> quadTree;
	
	private int screenWidth = 0, screenHeight = 0;
	
	public int getScreenWidth()
	{
		return this.screenWidth;
	}
	
	public int getScreenHeight()
	{
		return this.screenHeight;
	}
	
	public QuadTree<ICollidable> getQuadTree() 
	{ 
		return this.quadTree; 
	}
	
	private static Game currentGameInstance;
	
	/**
	 * Get a reference to the currently running game instance.
	 * This is useful to get a hold of global objects such as the camera, or the Box2D world.
	 * @return
	 */
	public static Game getInstance()
	{
		return Game.currentGameInstance;
	}
	
	public Game()
	{
		Game.currentGameInstance = this;
	}
	
	@Override
	public void create() 
	{
		this.stage = new Stage( 0, 0, true );
	}

	@Override
	public void dispose() 
	{
		this.stage.dispose();
	}

	@Override
	public void pause() 
	{
	
	}

	/**
	 * The first time the display is resized, we are given the width and height. 
	 * This is different than when call the create() method, when we don't actually
	 * know the size, let alone aspect ratio of the screen. This makes it hard to actually
	 * do any meaningful initialization during the create method. Instead, you may
	 * want to create most game objects in this method instead, once we know the size.
	 * 
	 * The camera will have already been configured by the Game class.
	 * @param width
	 * @param height
	 */
	protected abstract void init( int width, int height );
	
	/**
	 * The update method for your particular game. 
	 * This is where you should do things like update the Box2D world simulation, and process
	 * user input. 
	 * @param deltaTime The number of seconds since last update frame.
	 */
	protected abstract void update( float deltaTime );
	
	/**
	 * I don't like that libgdx intermingles render with update, so I added the {@link update} method, 
	 * but it will only get called if you call super.render() from your {@link render} method. There is nothing
	 * stopping you calling this yourself from your {@link render} method if you want to completely override
	 * this method. 
	 */
	@Override
	public void render()
	{
		this.update( Gdx.graphics.getDeltaTime() );
		
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		this.camera.update();
		this.camera.apply( gl );
	}
	
	@Override
	public void resize( int width, int height ) 
	{
		this.screenWidth = width;
		this.screenHeight = height;
		
		// The first time we resize the window, we are actually configuring the display
		// for the first time...
		if ( this.world == null )
		{
			// No gravity, because this is probably a top down game...
			this.world = new World( new Vector2( 0.0f, 0.0f ), true );
			this.quadTree = new QuadTree<ICollidable>( new Rectangle( 0, 0, width, height ) );
			
			this.camera = new OrthographicCamera( width, height );
			this.camera.position.set( 0, height / 2, 0 );
			
			// Let the subclass do whatever initialization it requires...
			this.init( width, height );
		}
	}
	
	/**
	 * Depending on the size of your world, you should configure the {@link camera} so that its size is 
	 * the appropriate size in metres. This method will take {@link minMetres} and try to fit the camera
	 * to that size. It does this by looking at the {@link Math.min} of the width and height of the window
	 * the game is running in, and setting that to {@link minMetres}.
	 * 
	 * @param minMetres The number of metres wide or high to make the game viewport. If the width of the
	 * screen is smaller, than this will be set to minMetres and height will be set appropriately 
	 * according to the aspect ratio, and vice-verca.
	 */
	public void updateCameraViewport( float minMetres )
	{
		if ( this.screenWidth == 0 || this.screenHeight == 0 )
		{
			// Why do I *need* a 'throws' statement for other types of exceptions other than this one?
			throw new IllegalArgumentException( "Cannot update camera viewport, the screen size has not yet been determined." );
		}
		
		if ( this.screenWidth < this.screenHeight )
		{
			float factor = minMetres / this.screenWidth;
			this.camera.viewportWidth = minMetres;
			this.camera.viewportHeight = this.screenHeight * factor;
		}
		else
		{
			float factor = minMetres / this.screenHeight;
			this.camera.viewportWidth = minMetres;
			this.camera.viewportHeight = this.screenHeight * factor;
		}
		
		this.camera.position.set( 0, this.camera.viewportHeight / 2, 0 );
	}
	

	@Override
	public void resume() 
	{
		
	}

}
