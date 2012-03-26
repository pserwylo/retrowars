package com.serwylo.peter.retrowars.scores;

/**
 * Keeps track of an individual score item, for use in a {@link ComboItem}.
 */
public class ScoreItem
{
	
	private int value;
	
	private long createdTime;
	
	public ScoreItem( int value )
	{
		this.value = value;
		this.createdTime = System.currentTimeMillis();
	}
    
	/**
	 * The number of points for this score item.
	 */
	public int getValue()
	{
		return this.value;
	}
	
	/**
	 * Time that the score was created/added to the game.
	 * That is, when the player caused this score to be created.
	 */
	public long getCreatedTime()
	{
		return this.createdTime;
	}
	
}
