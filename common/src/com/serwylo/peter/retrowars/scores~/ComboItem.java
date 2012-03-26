package com.serwylo.peter.retrowars.scores;

import java.util.ArrayList;

/**
 * Keeps track of a single string of scores which happened within a close period of time. 
 * If no score is added to this {@link ComboItem} within a period of time, then it will 
 * terminate and the {@link GameScore} should file and render it.
 */
public class ComboItem
{
    
	/**
	 * The scores which make up this combo.
	 */
	private ArrayList<ScoreItem> scores = new ArrayList<ScoreItem>();
	
	/**
	 * Time that the combo started.
	 */
	private long startTime = 0;
	
	/**
	 * Time since the combo item was last appended to with a new score. 
	 */
	private long lastAdd = System.currentTimeMillis();
	
	private GameScore score;
    
	public ComboItem( GameScore score ) 
	{
		this.score = score;
	}
	
	public void addScore( ScoreItem s )
	{
		this.scores.add( s );
		this.lastAdd = System.currentTimeMillis();
	}
	
	public int getSize()
	{
		return this.scores.size();
	}
	
}
