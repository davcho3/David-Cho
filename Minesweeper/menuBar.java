/*
 * David Cho
 * file:// menuBar.java
 * 
 * Contains MenuBar that gets placed inside the JFrame
 * Also contains all the ActinonListeners methods for each of the menu items
 * An ArrayList of HighScore class that keeps track of each person and their respective scores
 */

package minesweeper;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class menuBar 
{
	private JMenuBar 					createMenu;
	private JMenu 						game, help;
	private JMenuItem					reset, topTen, eXit, about, helpItem;
	private static ArrayList<HighScore>	score;
	
	menuBar()
	{
		createMenu = new JMenuBar();
		score = new ArrayList<HighScore>();
		populateArrayList();
		
		game = new JMenu("Game");
		help = new JMenu("Help");
		
		reset = 	new JMenuItem("Reset");
		topTen = 	new JMenuItem("Top Ten");
		eXit = 		new JMenuItem("Exit");
		helpItem = 	new JMenuItem("Help");
		about = 	new JMenuItem("About");
		
		// adding menu into menu-bar
		createMenu.add(game);
		createMenu.add(help);
		
		// adding items onto menu
		game.add(reset);
		game.add(topTen);
		game.add(eXit);
		
		help.add(helpItem);
		help.add(about);
		
		// adding action listeners
		reset.addActionListener(new resetAction());
		topTen.addActionListener(new topTenAction());
		eXit.addActionListener(new exitAction());
		
		helpItem.addActionListener(new helpAction());
		about.addActionListener(new aboutAction());
	}
	
	/*
	 * no returns val and no param
	 * 
	 * first, checks if a file exists
	 * if the file exists then gets all the content and puts it in the ArrayList<HighScore>
	 * else just exists 
	 */
	private void populateArrayList()
	{	
		File file = new File("topten.txt");
		
		if(file.exists() && file.isFile())
		{
			Scanner sc = null;
			try
			{
				sc = new Scanner(file);
			}
			catch(IOException ee)
			{}
			
			while(sc.hasNext())
			{
				String one = sc.next();
				String two = sc.next();
				
				int hs = Integer.parseInt(two);
				
				score.add(new HighScore(one, hs));
			}
		}
	}
	
	// getter method
	public JMenuBar getmenuBar()
	{
		return createMenu;
	}
	
	/*
	 * returns the top score currently present of any person
	 * since the 
	 */
	public static int getHighScore()
	{
		if(score.isEmpty())
			return 10000;
		
		return score.get(0).getScore();
	}
	
	/*
	 * adds a new player to the ArrayList
	 * gets called when a player get top score
	 */
	public static void addNewPlayer(HighScore hs)
	{
		score.add(hs);
		Collections.sort(score, new sortList());
		
		FileWriter fw;
		try
		{
			fw = new FileWriter(new File("topten.txt"));
			for(HighScore h: score)
			{
				fw.write(h.getName() + " " + h.getScore());
				fw.write(System.lineSeparator());
			}
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * ActionListener for topTen menu item menu->topTen
	 * Makes use of JOptionPane to show the list of topTen people and their respective scores
	 */
	private class topTenAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// clears the scores to avoid printing them twice if the user presses it twice or more
			score.clear();
			String toPrint = "";
			
			populateArrayList();
	  		
          if(score.size() > 0)
          {
				int i = 1;
				for(HighScore s : score)
				{
					if(i < 11)
					{
						toPrint = toPrint + i + ". " + s.getName() + " " + s.getScore() + "\n";
						i++;
					}
					else
						break;
				}
			}
			else
				toPrint = "No highscores available";
			
			JOptionPane.showMessageDialog (null, toPrint);
		}
	}
	
	/*
	 * action listener class for when user clicks on Game->Reset
	 * resets the game
	 * adds 10 mines in 10 random locations
	 */
	private class resetAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Panel.resetButtons();
			Panel.addMines();
		}
	}
	
	/*
	 *  action listener class for when user click on Game->Exit
	 *  exits the game 
	 */
	private class exitAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}	
	}
	
	/*
	 * Displays the rules of the game
	 * Makes use of JOptionPane to show the output in a small window
	 */
	private class helpAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String text = "Your objective is to find all the mines.\n" + 
							"Left click to explode, or right to guess.\n";
			JOptionPane.showMessageDialog (null, text);
		}
	}
	
	/*
	 * shows a small dialogue with game information
	 * also shows authors' information too
	 * Makes use of JOptionPane for the output
	 */
	private class aboutAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String text = "Author: 	Jay Patel and David Cho\n" +
							"Netid:	jpate64 and dcho8";
			JOptionPane.showMessageDialog (null, text);
		}
	}
	
	/*
	 * lambda method for ArrayList<HighScore> to sort the output
	 */
	private static class sortList implements Comparator<HighScore>
	{
		@Override
		public int compare(HighScore hs, HighScore hs2)
		{
			if(hs.getScore() > hs2.getScore())
				return 1;
			else
				return 0;
		}
	}
}