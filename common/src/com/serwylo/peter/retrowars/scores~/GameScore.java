package com.serwylo.peter.retrowars.scores;

import java.util.ArrayList;

import com.serwylo.peter.retrowars.Game;

public abstract class GameScore
{	
	/**
	 * I don't like ever loosing data, so all the scores will be kept here incase they are ever needed...
	 * But it should not be iterated over each game, because that could have major scaling issues.
	 */
	private ArrayList<ScoreItem> scores;
	private ArrayList<ComboItem> combos;

	/**
	 * List of scores that are part of the current combo...
	 */
	protected ComboItem currentCombo;
	
	/**
	 * So that we do not iterate over the entire list each loop, just render what needs to be...
	 */
	private ArrayList<ScoreItem> scoresToRender;
	private ArrayList<ComboItem> combosToRender;
	
	/**
	 * This score represents the level a player can achieve.
	 * Remember, high scores make players happy, so lots of zero's!
	 * 
	 * TODO: Write in generalised standardisation rules in this javadoc block so that developers of other games generate about the same scores regardless of the gameplay. 
	 */
	protected long score = 0;
	
	/**
	 * Are we currently in a combo?
	 */
	protected boolean comboing = false;
	
	protected int comboLength = 5;
	
	public final static int COMBO_RENDER_TIME = 200;
	
	private Game owner;
	
	public GameScore( Game owner )
	{
		this.owner = owner;
		scores = new ArrayList<ScoreItem>();
		combos = new ArrayList<ComboItem>();
		currentCombo = new ComboItem(this);
		scoresToRender = new ArrayList<ScoreItem>();
		combosToRender = new ArrayList<ComboItem>();
	}
	
	/**
	 * Each subclass of Score should feel free to implement its own scoring system to make life easier.
	 * When the actual RetroWars qualified score is expected, the subclasses score should be converted to
	 * a score that is fair and just in the RetroWars scoring scheme.
	 * 
	 * If a different scoring system is not required, just return <i>score</i>.
	 * 
	 * @return A normailsed score for the given game.
	 */
	protected abstract long normaliseScore();
	
	/**
	 * Games will have different kinds of combos, this is a way to specify
	 * what a good length for a combo is for a particular game.
	 * 
	 * @return Minimum length of a combo...
	 */
	protected abstract double comboNormaliser();
    
	/**
	 * Get the score from this object.
	 * @return Score for the associated game.
	 */
	public final long getScore()
	{
		return score;
	}
	
	/**
	 * Gives a list of all score items in the game.
	 * @return List of all score items.
	 */
	public final ArrayList<ScoreItem> getScoreItems()
	{
		return scores;
	}
	
	/**
	 * Gives a list of all score items in the game.
	 * @return List of all score items.
	 */
	public final ArrayList<ComboItem> getComboItems()
	{
		return combos;
	}
	
	public final ComboItem getCurrentCombo() {
		return currentCombo;
	}
	
	public abstract long getComboTime();
	
	protected final void clearCombo(boolean keep)
	{
		if (keep)
		{	
			combosToRender.add(currentCombo);
			combos.add(currentCombo);
			owner.triggerServerEvent((int)(currentCombo.size()*comboNormaliser()));
		}
		comboing = false;
		currentCombo = null;
	}
	
	/**
	 * Append another score item to the currently running combo.
	 * @param score The score item to be added...
	 * @param comboFlag If set to true, this ScoreItem is added to the current combo. 
	 */
	protected void addScore(ScoreItem score)
	{
		scores.add(score);
		scoresToRender.add(score);
		if (comboing)
		{
			currentCombo.addScore(score);
		}
		else
		{
			comboing = true;
			currentCombo = new ComboItem(this);
			currentCombo.addScore(score);
		}
	}
	
	abstract void checkCombo();
	
	/**
	 * The current combo for this game score object.
	 * @return Vector of score items that are in a combo.
	 */
	public final ArrayList<ComboItem> getCombosToRender()
	{
		return combosToRender;
	}
	
	/**
	 * Return all the scores that need to be rendered on screen...
	 * @return Scores which want attention...
	 */
	public final ArrayList<ScoreItem> getScoresToRender()
	{
		return scoresToRender;
	}
	
	public final String getFormattedScore()
	{
		long playScore = score;
		String formatted = "";
		while (playScore >= 1000)
		{
			formatted = "," + playScore % 1000 + formatted;
			playScore /= 1000;
		}
		formatted = playScore + formatted;
		return formatted;
	}
}
