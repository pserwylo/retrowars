package com.serwylo.peter.retrowars.scores;

/**
 * In Asteroids, you simply get scores for hitting asteroids.
 * The larger the asteroid, the smaller the score (because it is easier to hit).
 * To get combo's, its as simple as hitting lots of asteroids in a short period of time.
 * The combo's will not be as generous as a game like Missile Command, because you
 * can fire a lot faster and the bullets travel a lot faster.
 */
public class AsteroidsScore extends GameScore
{
	
	/**
	 * The time of the last asteroid being hit.
	 */
	private long lastAsteroidHit;
	
	private int combo = 0;	//Stores the real combo.
	
	private int eventCombo = 0;	//Reset each time an event is triggered....
	
	/**
	 * The amount of points to give for each asteroid hit (to be affected by various multipliers)
	 */
	public static final int POINTS_ASTEROID_HIT = 2000;
	
	/**
	 * Any asteroid hits within this time (ms) will be awarded a bonus.
	 */
	public static final int MULTIPLIER_TIME_LIMIT = 500;
	
	/**
	 * 
	 */
	public static long IDLE_COMBO_TIME = 500;
	
	public static double COMBO_NORMALIZER = 0.25;
	
	/**
	 * How big a combo before it starts firing off events?
	 */
	public static int COMBO_MIN_SIZE = 5;
	
	public AsteroidsScore(Engine owner)
	{
		super(owner);
	}
	
	protected long normaliseScore()
	{
		return score;
	}
	
	protected void checkCombo()
	{
		if (comboing && System.currentTimeMillis() - lastAsteroidHit > IDLE_COMBO_TIME)
		{
			//Clear the combo, and tell it whether to preserve it or nott...
			clearCombo((currentCombo.size() >= COMBO_MIN_SIZE));
		}		
	}
	    
	/**
	 * Takes into account the last time a missile was hit and determins how much to multiply a score by.
	 * The shorter distance between missile hits, the greater the multiplier.
	 *  
	 * @return Multiplier (double >= 1)
	 */
	private double getMultiplier()
	{
		//										(			Time since last hit				)
		long lastTime = MULTIPLIER_TIME_LIMIT - (System.currentTimeMillis() - lastAsteroidHit);
		if (lastTime > 0)
		{
			return 2.0 - (float)lastTime / MULTIPLIER_TIME_LIMIT;
		}
		else
		{
			return 1;	//No multiplier
		}
	}
	
	/**
	 * Get the amount of hits that defines a combo in Asteroids.
	 * 
	 * @return Minimum combo size.
	 */
	public double comboNormaliser()
	{
		return COMBO_NORMALIZER;
	}

	public long getComboTime() {
		return IDLE_COMBO_TIME;
	}
	
	/**
	 * Call this when a friendly <i>Bullet</i> hits an enemy <i>Asteroid</i>.
	 * Keep track of how many missiles an explosion has taken out for combo purposes.
	 * 
	 * Event needed. If a combo has happened (or something like that), then return a value between 1 and 10 for the severity 
	 * 			of the event the game needs to triger...
	 */
	public void asteroidHit(Asteroid asteroid, Bullet bullet)
	{		
		int value = (int)(POINTS_ASTEROID_HIT * asteroid.getSize() * getMultiplier());
		score += value;
		ScoreItem item = new ScoreItem(value, ASTEROID_HIT, asteroid.getLocation());

		addScore(item);
		
		lastAsteroidHit = System.currentTimeMillis();
	}
	
}
