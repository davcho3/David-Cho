/*
 * David Cho
 * file:// Panel.java
 * 
 * Main panel, contains all the buttons(all 100 of them)
 * also contains a variable(counter) of class TimerCounter too keep track of time
 * contains a variable(mines) that keeps track of all the mines left
 */

package minesweeper;

import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Panel
{
	private JPanel 				panel;
	private static Buttons[] 	buttons;
	private static TimerCounter	counter;
	private static boolean 		timerOn;
	private static int			totalPoints;
	private static int			minesLeft;
	private static MinesLeft	mines;
	
	// constructor
	Panel()
	{
		minesLeft = 10;
		totalPoints = 1;
		
		// Initializing MineLeft Class
		mines = new MinesLeft();
		
		// Initializing TimerCounter
		counter = new TimerCounter();
		timerOn = false;
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(10,10));
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	}
	/////////////////////////////////BUTTON START//////////////////////////////////////
	
	public static TimerCounter getTimer()
	{
		return counter;
	}
	
	////////////////////////////////BUTTON START//////////////////////////////////////
	
	/////////////////////////////////BUTTON START//////////////////////////////////////
	/*
	 * creates all the buttons
	 * Initialize all the buttons as well, all 100 of them
	 * adds all the buttons to the panel
	 */
	public void createButtons()
	{
		buttons = new Buttons[100];
		
		for(int i = 0; i < 100; i++)
		{
			buttons[i] = new Buttons(i);
			panel.add(buttons[i].getButton());
		}
	}
	
	/*
	 * adds 10 random mines
	 */
	public static void addMines()
	{
		Random rand = new Random();
		
		int i = 0;
		while(i != 10)
		{
			int num = rand.nextInt(100);
			
			// checks if the random number generated is already a mine
			if(buttons[num].isButtonMine())
				continue;
			
			buttons[num].setMine();
			i++;
		}
	}
	
	// getter for getting minesLeft class
	public MinesLeft getMinesLeftPanel()
	{
		return mines;
	}
	
	/*
	 * returns an Integer @ no 0Param
	 * 
	 * checks all the eight direction of the button and returns the amount of mines present
	 */
	private static int checkAdjacent(int val)
	{
		int ret = 0;
		
		if((val%10) == 0)
		{	
			if(val == 0)
			{
				if(buttons[val+1].isButtonMine())
					ret++;
				if(buttons[val+10].isButtonMine())
					ret++;
				if(buttons[val+11].isButtonMine())
					ret++;
			}
			else if(val == 90)
			{
				if(buttons[val+1].isButtonMine())
					ret++;
				if(buttons[val-10].isButtonMine())
					ret++;
				if(buttons[val-9].isButtonMine())
					ret++;
			}
			else if((val >= 10) && (val <= 80))
			{
				if(buttons[val+1].isButtonMine())
					ret++;
				if(buttons[val-10].isButtonMine())
					ret++;
				if(buttons[val-9].isButtonMine())
					ret++;
				if(buttons[val+11].isButtonMine())
					ret++;
				if(buttons[val+10].isButtonMine())
					ret++;
			}	
		}
		else if((val%10) == 9)
		{	
			if(val == 9)
			{
				if(buttons[val-1].isButtonMine())
					ret++;
				if(buttons[val+10].isButtonMine())
					ret++;
				if(buttons[val+9].isButtonMine())
					ret++;
			}
			else if(val == 99)
			{
				if(buttons[val-1].isButtonMine())
					ret++;
				if(buttons[val-10].isButtonMine())
					ret++;
				if(buttons[val-11].isButtonMine())
					ret++;
			}
			else if((val >= 19) && (val <= 89))
			{
				if(buttons[val-10].isButtonMine())
					ret++;
				if(buttons[val-11].isButtonMine())
					ret++;
				if(buttons[val+10].isButtonMine())
					ret++;
				if(buttons[val+9].isButtonMine())
					ret++;
			}
		}
		else if(((val%100) >= 91) && ((val%100) <= 98)) // this means we are dealing with BOTTOM border
		{
			if(buttons[val-1].isButtonMine())
				ret++;
			if(buttons[val+1].isButtonMine())
				ret++;
			if(buttons[val-10].isButtonMine())
				ret++;
			if(buttons[val-11].isButtonMine())
				ret++;
			if(buttons[val-9].isButtonMine())
				ret++;
		}
		else if(((val%100) <= 8) && ((val%100) >= 1)) // this means we are dealing with TOP border
		{
			if(buttons[val-1].isButtonMine())
				ret++;
			if(buttons[val+1].isButtonMine())
				ret++;
			if(buttons[val+10].isButtonMine())
				ret++;
			if(buttons[val+11].isButtonMine())
				ret++;
			if(buttons[val+9].isButtonMine())
				ret++;
		}
		else
		{
			if(buttons[val-1].isButtonMine())
				ret++;
			if(buttons[val+1].isButtonMine())
				ret++;
			if(buttons[val+10].isButtonMine())
				ret++;
			if(buttons[val+11].isButtonMine())
				ret++;
			if(buttons[val+9].isButtonMine())
				ret++;
			if(buttons[val-10].isButtonMine())
				ret++;
			if(buttons[val-11].isButtonMine())
				ret++;
			if(buttons[val-9].isButtonMine())
				ret++;
		}
		
		return ret;
	}
	
	/*
	 * looks at all the neighboring members
	 * if the neighboring member is not a mine
	 * explodes it
	 * else mark the amount of neighboring mines present
	 */
	public static void blowMines(int val)
	{
		if(timerOn == false)
		{
			counter.activateTimer();
			timerOn = true;
		}
		
		if(val > 99 || val < 0)
			return;
		
		if(buttons[val].getRightClick() > 0)
			return;
		
		if(buttons[val].isButtonAvailable() == false)
			return;
		
		buttons[val].setButtonAvailable(false);
		buttons[val].getButton().setIcon(new ImageIcon("button_0.gif"));
		
		// updating total points
		totalPoints++;
		
		if((val%10) == 0)
		{
			if(val == 0)
			{
				int ret = checkAdjacent(val);
				if(ret == 0)
				{
					blowMines(val+10);
					blowMines(val+11);
					blowMines(val+1);
				}
				else
					buttons[val].getButton().setIcon(new ImageIcon("button_" + ret + ".gif"));
			}
			else if(val == 90)
			{
				int ret = checkAdjacent(val);
				if(ret == 0)
				{
					blowMines(val-10);
					blowMines(val-9);
					blowMines(val+1);
				}
				else
					buttons[val].getButton().setIcon(new ImageIcon("button_" + ret + ".gif"));
			}
			else
			{
				int ret = checkAdjacent(val);
				if(ret == 0)
				{
					blowMines(val-10);
					blowMines(val-9);
					blowMines(val+1);
					blowMines(val+11);
					blowMines(val+10);
				}
				else
					buttons[val].getButton().setIcon(new ImageIcon("button_" + ret + ".gif"));
			}
		}
		else if((val%10) == 9)
		{
			if(val == 9)
			{
				int ret = checkAdjacent(val);
				if(ret == 0)
				{
					blowMines(val-1);
					blowMines(val+9);
					blowMines(val+10);
				}
				else
					buttons[val].getButton().setIcon(new ImageIcon("button_" + ret + ".gif"));
			}
			else if(val == 99)
			{
				int ret = checkAdjacent(val);
				if(ret == 0)
				{
					blowMines(val-1);
					blowMines(val-10);
					blowMines(val-11);
				}
				else
					buttons[val].getButton().setIcon(new ImageIcon("button_" + ret + ".gif"));
			}
			else
			{
				int ret = checkAdjacent(val);
				if(ret == 0)
				{
					blowMines(val-1);
					blowMines(val-10);
					blowMines(val-11);
					blowMines(val+9);
					blowMines(val+10);
				}
			}
		}
		else if(((val%100) >= 91) && ((val%100) <= 98)) // this means we are dealing with BOTTOM border
		{
			int ret = checkAdjacent(val);
			if(ret == 0)
			{
				blowMines(val-1);
				blowMines(val+1);
				blowMines(val-11);
				blowMines(val-10);
				blowMines(val-9);
			}
			else
				buttons[val].getButton().setIcon(new ImageIcon("button_" + ret + ".gif"));
		}
		else if(((val%100) <= 8) && ((val%100) >= 1)) // this means we are dealing with TOP border
		{
			int ret = checkAdjacent(val);
			if(ret == 0)
			{
				blowMines(val-1);
				blowMines(val+1);
				blowMines(val+9);
				blowMines(val+10);
				blowMines(val+11);
			}
			else
				buttons[val].getButton().setIcon(new ImageIcon("button_" + ret + ".gif"));
		}
		else
		{
			int ret = checkAdjacent(val);
			if(ret == 0)
			{
				blowMines(val-11);
				blowMines(val-10);
				blowMines(val-9);
				blowMines(val-1);
				blowMines(val+1);
				blowMines(val+9);
				blowMines(val+10);
				blowMines(val+11);
			}
			else
				buttons[val].getButton().setIcon(new ImageIcon("button_" + ret + ".gif"));
		}
			
	}
	
	/*
	 * no return val and @ 0Param
	 * gets called right after user presses the button each time
	 * checks to see if the user has met the high score requirement
	 */
	public static void checkIfWon()
	{
		if(totalPoints >= 90)
		{
			if(counter.getOutputTime() < menuBar.getHighScore())
			{
				String s = "Looks like you made it top ten.\nPlease enter your firstname in the box";
				String result = JOptionPane.showInputDialog(null, s + "\nEnter your name:");
				
				// gets rid of all the extra space if the user accidently enters first and last name
				result = result.replaceAll("\\s+","");
				
				menuBar.addNewPlayer(new HighScore(result, counter.getOutputTime()));
				
				disableAllButtons();
				resetButtons();
			}
			
			String s = "Congratulations, you won the game!";
			JOptionPane.showMessageDialog (null, s);
		}
	}
	
	/*
	 * no return and @ 0Param
	 * the method gets called when a user accidently clicks on a mine
	 * gets called within the Buttons class
	 * displays all the mines present inside the game
	 */
	public static void displayMines()
	{
		for(int i = 0; i < 100; i++)
		{
			if((buttons[i].getRightClick() == 1) && (buttons[i].isButtonMine()))
			{
				buttons[i].getButton().setIcon(new ImageIcon("button_bomb_x.gif"));
				continue;
			}
			if(buttons[i].isButtonMine())
				buttons[i].getButton().setIcon(new ImageIcon("button_bomb_blown.gif"));
		}
	}
	
	/*
	 * no return and @ 0Param
	 * 
	 * disables all the mines
	 * gets called right after displayMines() method
	 * gets called in Buttons class after the user accidently clicks on a mine
	 */
	public static void disableAllButtons()
	{
		for(int i = 0; i < 100; i++)
			buttons[i].setButtonAvailable(false);
	}
	
	public static int getMinesLeft()
	{
		return minesLeft;
	}
	
	public static void decMinesLeft()
	{
		minesLeft--;
	}
	
	/*
	 * no returns @ 0Param
	 * resets all the buttons
	 * get called when the user presses on Reset button and selects Reset from the menu menu->Reset
	 * makes an individual call to resetButton() within the Buttons class
	 */
	public static void resetButtons()
	{
		minesLeft = 10;
		totalPoints = 1;
		counter.stopTimer();
		timerOn = false;
		
		for(int i = 0; i < 100; i++)
			buttons[i].resetButton();
	}
	
	/*
	 * returns all the buttons
	 * all 100 of them
	 */
	public Buttons[] getButton()
	{
		return buttons;
	}
	
	/////////////////////////////////BUTTON END//////////////////////////////////////
	
	public JPanel getPanel()
	{
		return panel;
	}
}