package retrowars.scoring;

import retrowars.engine.Engine;
import retrowars.module.premade.Explosion;
import retrowars.module.premade.Missile;
/*
 * Created on 7/05/2007
 *
 */
import retrowars.module.premade.Tank;

/**
 * @author peser1
 *
 */
public class MissileDefenceScore extends GameScore
{

	/**
	 * Should be easier to deal with our own scoring mechanism for each game, 
	 * and then normalise it before ourputing it to anywhere.
	 */
	private int mcScore = 0;
	
	private long lastTankHit;	//The system time of the last missile hit (for combo's)
	
	/**
	 * The amount of points to give for each missile shot down (to be affected by various multipliers)
	 */
	private int POINTS_MISSILE_HIT = 10000;
	
	/**
	 * Any missile hits within this time (ms) will be awarded a bonus.
	 */
	private long MULTIPLIER_TIME_LIMIT = 8000;
	
	public static long IDLE_COMBO_TIME = 2000;
	
	public static double COMBO_NORMALIZER = 1.5;
	
	private static final String MISSILE_HIT = "Missile Hit";

	private int combo;

	private int eventCombo;

	public static int COMBO_MIN_SIZE = 4;
	
	public MissileDefenceScore(Engine owner)
	{
		super(owner);
	}
	
	/**
	 * Get the amount of hits that defines a combo in Missile Command.
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
	
	public void checkCombo()
	{
		if (comboing && System.currentTimeMillis() - lastTankHit > IDLE_COMBO_TIME)
		{
			//Clear the combo, and tell it whether to preserve it or nott...
			clearCombo((currentCombo.size() >= COMBO_MIN_SIZE));
			//System.out.println("Clearing Combo");
		}
	}
	
	public void completeCombo()
	{
		if (currentCombo != null)
			clearCombo((currentCombo.size() >= COMBO_MIN_SIZE));
	}
	
	protected long normaliseScore()
	{
		return score;
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
		long lastTime = MULTIPLIER_TIME_LIMIT - (System.currentTimeMillis() - lastTankHit);
		
		if (lastTime > 0)
		{
			return 1 + lastTime / MULTIPLIER_TIME_LIMIT;
		}
		else
		{
			return 1;	//No multiplier
		}
	}

	/**
	 * Call this when an enemy <i>Missile</i> is hit by a friendly <i>Explosion</i>.
	 * Keep track of how many missiles an explosion has taken out for combo purposes.
	 */
	public void tankHit(Tank tank, Missile missile)
	{
		int value = (int)(POINTS_MISSILE_HIT * getMultiplier());
		score += value;
		ScoreItem item = new ScoreItem(value, MISSILE_HIT, missile.getLocation());
		addScore(item);
		
		lastTankHit = System.currentTimeMillis();
	}
	
	/**
	 * Call this when a friendly <i>City</i> is hit by an enemy <i>Missile</i>.
	 *
	 */
	public void cityHit()
	{
		
	}
	
	/**
	 * Call this when a friendly <i>Turret</i> is hit by an enemy <i>Missile</i>.
	 *
	 */
	public void turretHit()
	{
		
	}
	
}
