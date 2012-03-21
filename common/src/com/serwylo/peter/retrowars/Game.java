package com.serwylo.peter.retrowars;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.serwylo.peter.retrowars.collisions.ICollidable;
import com.serwylo.peter.retrowars.collisions.QuadTree;

public abstract class Game implements ApplicationListener
{

	protected Box2DDebugRenderer debugRenderer;
	
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
	
	protected Vector2 worldSize = new Vector2( 0.0f, 0.0f );
	
	/**
	 * This is the size of the game world, rather than the screen size.
	 * It is size for which I want Box2D objects to move around. 
	 * @return
	 */
	public Vector2 getWorldSize()
	{
		return this.worldSize;
	}
	
	public float getWorldWidth()
	{
		return this.worldSize.x;
	}
	
	public float getWorldHeight()
	{
		return this.worldSize.y;
	}
	
	private float metresPerPixel = 1.0f;
	
	/**
	 * Scale factor between Box2D and pixels on the screen. 
	 * Used for scaling positions of sprite's for pixel perfect rendering.
	 * @return
	 */
	public float getMetresPerPixel()
	{
		return this.metresPerPixel;
	}
	
	/**
	 * The camera, which is setup to work in a 2D environment, and configured so that we
	 * can think in metres, rather than pixels. This will not only help appease Box2D which
	 * prefers these types of units, but also makes it easier to specify speeds of objects
	 * which seem reasonable.
	 */
	protected OrthographicCamera camera;
	
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
		this.debugRenderer = new Box2DDebugRenderer();
	}
	
	@Override
	public void create() 
	{
	}

	@Override
	public void dispose() 
	{
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
	protected abstract void init( int screenWidth, int screenHeight );
	
	/**
	 * The update method for your particular game. 
	 * This is where you should do things like update the Box2D world simulation, and process
	 * user input. 
	 * @param deltaTime The number of seconds since last update frame.
	 */
	protected abstract void update( float deltaTime );
	
	protected void updateGame( float deltaTime )
	{
		this.world.step( deltaTime, 8, 3 );
	}
	
	/**
	 * I don't like that libgdx intermingles render with update, so I added the {@link update} method, 
	 * but it will only get called if you call super.render() from your {@link render} method. There is nothing
	 * stopping you calling this yourself from your {@link render} method if you want to completely override
	 * this method. 
	 */
	@Override
	public void render()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();
		this.updateGame( deltaTime );
		this.update( deltaTime );
		
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		this.camera.update();
        this.camera.apply( gl );
	}
	
	/**
	 * Viewport is drawn in Green, Screen border is drawn in blue, World in red.
	 * Box2D stuff drawn with the {@link debugRenderer}.
	 */
	public void renderDebug()
	{
		this.camera.update();
		this.camera.apply( Gdx.gl10 );
        this.debugRenderer.render( this.world, this.camera.combined );
        
        Rectangle viewportRect = new Rectangle( 3, 3, this.camera.viewportWidth - 6, this.camera.viewportHeight - 6 );
        GraphicsUtils.drawDebugRect( viewportRect, this.camera, 0.0f, 1.0f, 0.0f );
        
        Rectangle worldRect = new Rectangle( 6, 6, this.worldSize.x - 9, this.worldSize.y - 9 );
        GraphicsUtils.drawDebugRect( worldRect, this.camera, 1.0f, 0.0f, 0.0f );
        
        Rectangle screenRect = new Rectangle( 1, 1, this.camera.viewportWidth - 2, this.camera.viewportHeight - 2 );
        GraphicsUtils.drawDebugRect( screenRect, this.camera, 0.0f, 0.0f, 1.0f );
	}

	@Override
	public void resize( int width, int height ) 
	{
		
		// The first time we resize the window, we are actually configuring the display
		// for the first time...
		if ( this.world == null )
		{
			// No gravity, because this is probably a top down game...
			this.world = new World( new Vector2( 0.0f, 0.0f ), true );
			
			this.camera = new OrthographicCamera( width, height );
			this.updateCameraViewport( 1, 1 );
			
			// Let the subclass do whatever initialization it requires...
			this.init( width, height );
		}
	}
	
	/**
	 * Depending on the size of your world, you should configure the {@link camera} so that its size is 
	 * the appropriate size in metres. This method will make sure that each {@link pixels} pixels on the
	 * screen represent {@link metres} metres in the game world.
	 * 
	 * It will also set the world size, which is the size of the world which fits in the available screen
	 * real estate.
	 * 
	 * 
	 *   <--- Screen: 800px, World : 40m -->
	 * 
	 *  +-----------------------------------+
	 *  |                                   | ^
	 *  |                                   | |
	 *  |                                   | |
	 *  |                                   | 
	 *  |                                   | Screen: 400px
	 *  |                                   | World: 20m
	 *  |                                   | 
	 *  |                                   | |
	 *  |                                   | |
	 *  |                                   | v
	 *  +-----------------------------------+
	 *  
	 * 
	 * 
	 * 
	 * 
	 * @param pixels
	 * @param metres
	 */
	public void updateCameraViewport( int pixels, float metres )
	{
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		if ( w == 0 || h == 0 )
		{
			// Why do I *need* a 'throws' statement for other types of exceptions other than this one?
			throw new IllegalArgumentException( "Cannot update camera viewport, the screen size has not yet been determined." );
		}
		
		this.metresPerPixel = metres / pixels;
		Gdx.app.log( "METRES_PER_PIXEL", this.metresPerPixel + "" );
		this.camera.viewportWidth = w * this.metresPerPixel;
		this.camera.viewportHeight = h * this.metresPerPixel;

		Gdx.app.log( "SCREEN", "Size: (" + w + ", " + h + ")" );		
		Gdx.app.log( "VIEWPORT", "Size: (" + this.camera.viewportWidth + ", " + this.camera.viewportHeight + ")" );
		
		this.worldSize.x = this.camera.viewportHeight;
		this.worldSize.y = this.camera.viewportHeight;

		this.camera.position.x = this.worldSize.x / 2;
		this.camera.position.y = this.worldSize.y / 2;
		this.camera.position.z = 0;
		// this.camera.zoom = 1 / factor;
	}
	

	@Override
	public void resume() 
	{
		
	}

}
