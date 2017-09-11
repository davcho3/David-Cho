/*
 * David Cho
 * file:// HighScore.java
 * 
 * An individual person
 * Holds the person's name and their respective score, in seconds (achieved at the same instance the main counter stopped)
 */

package minesweeper;

public class HighScore
{
	private String 	name;
	private int 	highScore;
	
	// constructor with 2Param string for name and an int for the person's score
	HighScore(String s, int hs)
	{
		name = s;
		highScore = hs;
	}
	
	/////////////////////////////GETTER START////////////////////////////////
	public String getName()
	{
		return name;
	}
	
	public int getScore()
	{
		return highScore;
	}
	
	///////////////////////////GETTER END////////////////////////////////
}
