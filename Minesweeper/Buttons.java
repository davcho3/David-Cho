/*
 * David Cho
 * file:// Buttons.java
 * 
 * An individual button
 * Every button acts as its own
 * Contains all the ActionListener that is involved by a Button
 */

package minesweeper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class Buttons
{
	private JButton		 	button;
	private boolean 		isMine;
	private int 			rightClick;
	private int 			position;
	private boolean 		buttonAvailable;
	
	// constructor with 1Param 
	Buttons(int val)
	{
		button = new JButton(new ImageIcon("button_normal.gif"));
		isMine = false;
		rightClick = 0;
		position = val;
		buttonAvailable = true;
		button.addMouseListener(new MouseClickHandler());
	}
	
	/*
	 * resets the button to Normal mode
	 * resets buttons ImageIcon
	 * resets the button to original start of not being a mine
	 * rightClick set to 0
	 * buttonAvaialbe to true to make it available by the user for clicks
	 */
	public void resetButton()
	{
		button.setIcon(new ImageIcon("button_normal.gif"));
		isMine = false;
		rightClick = 0;
		buttonAvailable = true;
	}
	
	//////////////////////////START GETTER/////////////////////////////////////
	public int getRightClick()
	{
		return rightClick;
	}
	
	public JButton getButton()
	{
		return button;
	}
	//////////////////////////END GETTER/////////////////////////////////////
	
	//////////////////////////START SETTER/////////////////////////////////////
	public void setButtonAvailable(boolean val)
	{
		buttonAvailable = val;
	}
	
	public void setMine()
	{
		isMine = true;
	}

	//////////////////////////END SETTER/////////////////////////////////////
	
	/*
	 * returns a boolean based on if the button is available or not
	 */
	public boolean isButtonAvailable()
	{
		if(buttonAvailable == true)
			return true;
		else
			return false;
	}
	
	/*
	 * returns true of false depending if button is a mine or not
	 */
	public boolean isButtonMine()
	{
		return isMine;
	}
	
	/*
	 * ActionListener for a mouse click that gets performed on a button
	 * Checks the MouseEvent to see if it was a left mouse click or right mouse click
	 */
	private class MouseClickHandler extends MouseAdapter
	{
	 public void mouseClicked (MouseEvent e)
	   {
		 // check if the mouse was right or left clicked
		 if (SwingUtilities.isLeftMouseButton(e))
		 {
			 /*
			  * activates the selected button to see if Mine is under it or not
			  * if a Mine is found under the button than the game ends
			  * all the mines get displayed
			  * also, disable all the mines to ignore anymore clicks from the user
			  */
			 if(isMine)
			 {
				 Panel.displayMines(); 
				 Panel.disableAllButtons();
				 Panel.getTimer().stopTimer();
			 }
			 else
				 Panel.blowMines(position);
			 
			 Panel.checkIfWon();
		 }
		 else if (SwingUtilities.isRightMouseButton(e))
		 { 
			 rightClick++;
			 
			 if(rightClick > 2)
				 rightClick = 0;
			 
			 /*
			  *  first click is activating mine
			  *  second click = ?
			  */
			 
			 if(rightClick == 1)
			 {
				 if(Panel.getMinesLeft() == 0)
					 return;
				 
				 Panel.decMinesLeft();
				 button.setIcon(new ImageIcon("button_flag.gif"));
			 }
			 else if(rightClick == 2)
				 button.setIcon(new ImageIcon("button_question.gif"));
			 else
				 button.setIcon(new ImageIcon("button_normal.gif"));
		 }
	   }
	}
}