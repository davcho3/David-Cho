//Robert Hull, Eilbron Davood, David Cho
//
//UIC
//CS342 - Spring 2016
//
//Tetris - Tetro_Z.java

import javax.swing.*;

public class Tetro_Z extends Tetromino {

    private int rotate_cords[][];
    private int pos;

    private static Tetro_Z instance = new Tetro_Z();

    private Tetro_Z() {
        color = new ImageIcon(getClass().getResource("Images/lime.jpg"));
    }

    public static Tetro_Z getInstance() {
        return instance;
    }

    public void initialPos(){
        pos = 0;
        coordinates = new int[][]{{4, -2}, {5, -2}, {5, -1}, {6, -1}};
        rotate_cords = new int[][]{{2, -1}, {0, 0}, {0, 0}, {0, -1}};
    }

    public void rotateLeft(TetrisLabel[][] grid) {

        int tempPos;
        temp = copy2dArray(coordinates);
        if (pos == 0) {
            tempPos = 1;
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords[i][j];
                }
            }
        } else {
            tempPos = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    temp[i][j] -= rotate_cords[i][j];
                }
            }
        }
        if (check(grid, temp)){
            change(grid, coordinates, temp);
            coordinates = copy2dArray(temp);
            pos = tempPos;
        }
    }

    public void rotateRight(TetrisLabel[][] grid) {
        int tempPos;
        temp = copy2dArray(coordinates);
        if (pos == 0) {
            tempPos = 1;
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords[i][j];
                }
            }
        } else {
            tempPos = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    temp[i][j] -= rotate_cords[i][j];
                }
            }
        }
        if (check(grid, temp)){
            change(grid, coordinates, temp);
            coordinates = copy2dArray(temp);
            pos = tempPos;
        }
    }
}