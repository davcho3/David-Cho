/*
 * David Cho
 * file:// mainView.java
 * 
 * Contains all the panels and sub-panels that gets added to the JFrame inside containsMain.java
 */

package minesweeper;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class mainView
{
	private JFrame frame;
	private JPanel panel;
	private JPanel counterAndPointPanel;
	private JPanel counterPanel;
	private JPanel pointPanel;
	
	mainView()
	{
		counterAndPointPanel = new JPanel(new GridBagLayout());
		counterAndPointPanel.setBackground(Color.black);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		// created to add points
		pointPanel = new JPanel();
		pointPanel.setBackground(Color.black);
		
		counterAndPointPanel.add(pointPanel, gbc);
		
		// creating this panel to take space
			
			JButton resetButton = new JButton("Reset");
			resetButton.addActionListener(new resetButton());
		
			JPanel stupid = new JPanel();
			stupid.setBackground(Color.black);
			stupid.add(resetButton);
		
			gbc.gridx = 1;
			gbc.gridy = 0;
		
		counterAndPointPanel.add(stupid, gbc);
		
		// ignore everything in between
		
		// created to add counter
		counterPanel = new JPanel();
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		
		counterAndPointPanel.add(counterPanel, gbc);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		// adding counterAndPointPanel into panel
		panel.add(counterAndPointPanel);
		
		frame = new JFrame("Mine Sweeper");
		frame.setSize(272,392);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.add(panel);
	}
	
	/*
	 * adds a JMenuBar component to JFrame
	 * taken in a JMenuBar variable as a parameter
	 */
	public void setmenuBar(JMenuBar val)
	{
		frame.setJMenuBar(val);
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public JPanel getCounterPanel()
	{
		return counterPanel;
	}
	
	public JPanel getPointPanel()
	{
		return pointPanel;
	}
	
	private class resetButton implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Panel.resetButtons();
			Panel.addMines();
		}
		
	}
}