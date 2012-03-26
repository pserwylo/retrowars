package com.serwylo.peter.retrowars.asteroids;

import com.serwylo.peter.retrowars.scores.GameScore;
import com.serwylo.peter.retrowars.scores.ScoreItem;

public class AsteroidsScore extends GameScore 
{
	
	private static int ASTEROID_SCORE = 10000;
	
	public ScoreItem addScore( Asteroid asteroid )
	{
		double scoreValue = ASTEROID_SCORE / ( asteroid.getSize() + 1 );
		ScoreItem score = new ScoreItem( scoreValue, asteroid.getB2Body().getPosition() );
		super.addScore( score );
		return score;
	}
	
}
