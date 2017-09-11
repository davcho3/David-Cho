/*
 * David Cho
 * file:// containsMain.java
 * Buttons Images were used from
 * https://www.dropbox.com/sh/nscwvz2sz250r6p/AAAwcRupmWpJqfFfkxXCKEZ4a?dl=0
 * 
 * Contains Main() method inside this class
 */

package minesweeper;

import java.awt.Color;
import javax.swing.SwingUtilities;

public class containsMain 
{
	containsMain()
	{}
	
	public void runGame()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				mainView view = new mainView();
				menuBar menu = new menuBar();
				Panel panel = new Panel();
				
				// creating components
				panel.createButtons();
				
				// adding Mines to random 10 mines
				Panel.addMines();
				
				// adding the menu bar to the main frame
				view.setmenuBar(menu.getmenuBar());
				
				// adding MinesLeft, JPanel, and counterPanel to JFrame
				view.getPointPanel().add(panel.getMinesLeftPanel().getMinesLabel());
				view.getCounterPanel().add(Panel.getTimer().getTimerLabel());
				view.getPanel().add(panel.getPanel());
				
				// setting color for timer background to be black
				view.getCounterPanel().setBackground(Color.black);
				
				// setting the main counter to 0
				Panel.getTimer().stopTimer();
			}
		});
	}
	
	public static void main(String[] args) 
	{
		containsMain game = new containsMain();
		game.runGame();
	}

}