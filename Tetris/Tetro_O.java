//Robert Hull, Eilbron Davood, David Cho
//
//UIC
//CS342 - Spring 2016
//
//Tetris - Tetro_O.java


import javax.swing.*;

public class Tetro_O extends Tetromino {

    private static Tetro_O instance = new Tetro_O();

    private Tetro_O() {
        color = new ImageIcon(getClass().getResource("Images/blue.jpg"));
    }

    public static Tetro_O getInstance() {
        return instance;
    }

    public void initialPos(){
        coordinates = new int[][]{{4, -2}, {5, -2}, {5, -1}, {4, -1}};
    }

    //leave blank, you can't rotate this piece
    public void rotateLeft(TetrisLabel[][] grid) {    }

    //leave blank you can't rotate this piece
    public void rotateRight(TetrisLabel[][] grid) {    }
}
