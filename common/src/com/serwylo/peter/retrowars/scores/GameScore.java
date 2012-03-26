package com.serwylo.peter.retrowars.scores;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GameScore 
{
	
	public final static int SHOW_SCORE_DURATION = 750;
	
	protected List<ScoreItem> scores = new LinkedList<ScoreItem>();
	
	/**
	 * As we add new scores, we will add to the total score.
	 */
	protected double totalScore = 0.0f;
	
	public void addScore( ScoreItem score )
	{
		this.scores.add( score );
		this.totalScore += score.getScore();
	}
	
	public double getTotalScore()
	{
		return this.totalScore;
	}
	
	public List<ScoreItem> getScores()
	{
		return this.scores;
	}
	
	/**
	 * TODO: I'm torn here, because the HUD should be the one who decides how long a score is displayed for,
	 * but the game score should be responsible for keeping a list of scores to display. That means
	 * that the HUD would have to be removing items from our list here...
	 * @param delta
	 */
	public void update( float delta )
	{
		Iterator<ScoreItem> it = this.scores.iterator();
		while ( it.hasNext() )
		{
			ScoreItem score = it.next();
			boolean alive = System.currentTimeMillis() - score.getCreatedTime() < SHOW_SCORE_DURATION;
			if ( !alive )
			{
				it.remove();
			}	
		}
	}

}
