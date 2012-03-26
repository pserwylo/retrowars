package com.serwylo.peter.retrowars;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.serwylo.peter.retrowars.scores.GameScore;
import com.serwylo.peter.retrowars.scores.ScoreItem;

/**
 * Displays total score, currently visible {@link ScoreItem}s, as well has health information.
 * If playing multiplayer, it will also display the status of other online players.
 */
public class HUD 
{

	private static HUD instance;
	
	public static void render( Game game )
	{
		if ( instance == null )
		{
			instance = new HUD();
		}
		
		instance.renderHud( game );
	}
	
	/**
	 * Number of pixels the score will float up as it fades out and dissapears.
	 */
	public static final int SCORE_FLOAT_DISTANCE = 50;
	
	protected Camera camera; 
	
	protected BitmapFont font;
	
	protected DecimalFormat formatter;

	public HUD()
	{
		this.camera = new OrthographicCamera( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
		this.font = AssetManager.getHudFont();
		this.formatter = new DecimalFormat( "###,###" );

	}
	
	protected void renderHud( Game game )
	{
		double scale = game.getMetresPerPixel();
		
		SpriteBatch batch = new SpriteBatch();
		batch.begin();

		this.font.setColor( 1.0f, 1.0f, 1.0f, 1.0f );
		this.font.draw( batch, "Score: " + this.formatter.format( game.getScore().getTotalScore() ), 10, 10 );
		
		for ( ScoreItem score : game.getScore().getScores() )
		{
			float factor = ( float )( System.currentTimeMillis() - score.getCreatedTime() ) / GameScore.SHOW_SCORE_DURATION;
			factor = Interpolation.pow2.apply( factor );
			
			float offset = (float)SCORE_FLOAT_DISTANCE * factor;
			this.font.setColor( 1.0f, 1.0f, 1.0f, 1.0f - factor );
			this.font.draw( batch, score.getScore() + "", (int)( score.getLocation().x / scale ), (int)( score.getLocation().y / scale + offset ) );
		}
		batch.end();
		
	}
	
}
