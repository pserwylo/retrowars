package com.serwylo.peter.retrowars.scores;

import com.badlogic.gdx.math.Vector2;

public class ScoreItem 
{

	protected double score;
	
	protected Vector2 location;
	
	protected long createdTime;
	
	public ScoreItem( double score, Vector2 location )
	{
		this.score = score;
		this.location = location.cpy();
		this.createdTime = System.currentTimeMillis();
	}
	
	public double getScore()
	{
		return this.score;
	}
	
	public Vector2 getLocation()
	{
		return this.location;
	}
	
	public long getCreatedTime()
	{
		return this.createdTime;
	}
	
}
