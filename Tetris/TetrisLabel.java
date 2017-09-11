//Robert Hull, Eilbron Davood, David Cho
//
//UIC
//CS342 - Spring 2016
//
//Tetris - TetrisLabel.java - Labels that keep track of their type, their default color, and their current color.

import javax.swing.*;

public class TetrisLabel extends JLabel {

    private int type;
    private ImageIcon grey;
    private ImageIcon color;

    /**
     * Create a label, that is type "blank", store the default color, and set the current color to the default color.
     * @param i The default color.
     */
    public TetrisLabel(ImageIcon i){
        super(i);
        type = 0;
        grey = i;
        color = i;
    }

    /**
     * Change the type, 0 - blank, 1 - active, 2 - inactive
     * @param x the type to be set to
     */
    public void setType(int x){
        type = x;
    }

    /**
     * Let us know what type the label is.
     * @return 0 - blank, 1 - active, 2 - inactive
     */
    public int Type(){
        return type;
    }

    /**
     * Reset label to default values.
     * Type is blank and color is grey.
     */
    public void reset() {
        type = 0;
        color = grey;
        setIcon(grey);
    }

    /**
     * Change the label.
     * @param x The color.
     * @param y The type.
     */
    public void updateLabel(ImageIcon x, int y){
        type = y;
        color = x;
        setIcon(x);
    }

    /**
     * Copy the variables of this label, to the parameter label.
     * @param x Label to be changed.
     */
    public void copyTo(TetrisLabel x){
        x.type = type;
        x.color = color;
        x.setIcon(color);
    }
}


