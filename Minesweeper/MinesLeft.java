/*
 * David Cho
 * file:// MinesLeft.java
 * 
 * Contains all the methods that involve counting for mines
 * Makes use of Swing Timer class to check if the minesLeft has been updated inside Panel class
 */

package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

public class MinesLeft
{
	private Timer	t;
	private JLabel	label;
	
	// constructor with no param
	MinesLeft()
	{
		label = new JLabel();
		label.setOpaque(true);
		t = new Timer(1000, new TimerListener());
		t.start();
	}
	
	public JLabel getMinesLabel()
	{
		return label;
	}
	
	/*
	 * this is the method that gets called every time the Timer performs an action
	 * gets the value associated with minesLeft within the Panel class
	 */
	private void logic()
	{
		label.setFont(new Font("Digital-7", Font.PLAIN, 30));
		label.setBackground(Color.black);
		label.setForeground(Color.RED);
		label.setText("" + Panel.getMinesLeft());
	}
	
	/*
	 * ActionListener used by the Timer class
	 */
	private class TimerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			logic();
			label.repaint();
		}	
	}
}
