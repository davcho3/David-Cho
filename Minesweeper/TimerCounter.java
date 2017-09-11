/*
 * David Cho
 * file:// TimerCounter.java
 * 
 * Contains all the methods that are involved with the main counter
 * Makes use of Timer class to update the time
 */

package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

public class TimerCounter
{
	private Timer 	t;
	private int 	outputTime;
	private JLabel 	label;
	
	// constructor
	TimerCounter()
	{
		label = new JLabel();
		label.setOpaque(true);
		t = new Timer(1000, new TimerListener());
		t.stop();
		outputTime = 0;
	}
	
	//////////////////////GETTER START//////////////////////////////
	public JLabel getTimerLabel()
	{
		return label;
	}
	
	public int getOutputTime()
	{
		return outputTime;
	}
	
	/////////////////////END GETTERS//////////////////////////////
	
	/*
	 * This is the method that gets called every time an action gets performed by the Timer class
	 * Timer is set to update once every second to update the main timer by one second each time
	 */
	private void logic()
	{
		outputTime++;
		
		label.setFont(new Font("Digital-7", Font.PLAIN, 30));
		label.setBackground(Color.black);
		label.setForeground(Color.RED);
		label.setText("  " +outputTime);
	}
	
	/*
	 * starts the Timer
	 */
	public void activateTimer()
	{
		t.start();
	}
	
	/*
	 * stops the Timer
	 */
	public void stopTimer()
	{
		outputTime = -1;
		logic();
		t.stop();
	}

	/*
	 * ActionListener for Timer
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