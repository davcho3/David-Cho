//Robert Hull, Eilbron Davood, David Cho
//
//UIC
//CS342 - Spring 2016
//
//Tetris - Tetromino.java - Parent class of all the Tetro pieces.  This class is abstract, so it implements
//                          some of the functions, and requires the child classes to implement the rest.

import javax.swing.*;
import java.util.Collections;
import java.util.Vector;

public abstract class Tetromino {

    protected int coordinates[][];
    protected int temp[][];
    protected ImageIcon color;

    protected Tetromino(){}

    /**
     * If there are open spaces below, we move each part of the Tetromino down one position, and update the grid.
     * Otherwise, we are done dropping and the piece is deactivated.
     * @param grid This is what we are manipulating.
     * @return true if we are done dropping, false if we successfully drop
     */
    public boolean drop(TetrisLabel[][] grid) {

        temp = copy2dArray(coordinates);
        for (int i = 0; i < 4; i++) {
            if (coordinates[i][1] == 19 || (coordinates[i][1] > 0 && grid[coordinates[i][1] + 1][coordinates[i][0]].Type() == 2)){
                deactivate(grid);
                return true;
            } else {
                temp[i][1] += 1;
            }
        }
        change(grid, coordinates, temp);
        coordinates = copy2dArray(temp);
        return false;
    }

    /**
     * Move each part of the Tetromino to the left one position
     * If the new positions are valid, we keep the changes and update the grid.
     * Otherwise the changes are disgarded.
     * @param grid This is what we are manipulating.
     */
    void moveLeft(TetrisLabel[][] grid){
        temp = copy2dArray(coordinates);
        for (int i = 0; i < 4; i++) {
            temp[i][0] -= 1;
        }
        if (check(grid, temp)){
            change(grid, coordinates, temp);
            coordinates = copy2dArray(temp);
        }
    }

    /**
     * Move each part of the Tetromino to the right one position.
     * If the new positions are valid, we keep the changes and update the grid.
     * Otherwise the changes are disgarded.
     * @param grid This is what we are manipulating.
     */
    void moveRight(TetrisLabel[][] grid){
        temp = copy2dArray(coordinates);
        for (int i = 0; i < 4; i++) {
            temp[i][0] += 1;
        }
        if (check(grid, temp)){
            change(grid, coordinates, temp);
            coordinates = copy2dArray(temp);
        }
    }

    /**
     * Find filled rows, clear them, gravity drop, and return number of rows cleared.
     * @param grid This is what we are manipulating.
     * @return number of rows cleared
     */
    public int clearRows(TetrisLabel[][] grid) {
        Vector<Integer> rows = new Vector<>();
        boolean lineIsFull = true;

        //Figure out which rows need to be deleted
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 10; j++){
                if (grid[coordinates[i][1]][j].Type() == 0){
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull){
                rows.add(coordinates[i][1]);
            }
            lineIsFull = true;
        }
        //If there are rows that need to be deleted
        if (rows.size() > 0) {
            int removedLines = 0;

            //Copy rows down
            for (int i = Collections.max(rows); i >= 0; i--) {
                if (rows.contains(i)) {
                    removedLines += 1;
                } else {
                    for (int j = 0; j < 10; j++) {
                        grid[i][j].copyTo(grid[i + removedLines][j]);
                    }
                }
            }
            //Reset the rows at the top
            for (int i = rows.size() - 1; i >= 0; i--) {
                for (int j = 0; j < 10; j++) {
                    grid[i][j].reset();
                }
            }
        }

        return rows.size();
    }

    /**
     * Check if the new position is a valid spot on the grid
     * @param newPos coordinates of the 4 parts of the Tetromino
     * @param grid This is what we are manipulating.
     * @return false if invalid position, true if valid position
     */
    public boolean check(TetrisLabel[][] grid, int[][] newPos){

        for (int i = 0; i < 4; i++) {
            if (newPos[i][0] < 0 || newPos[i][0] > 9 || newPos[i][1] > 19){    //Out of bounds
                return false;
            }
            else if (newPos[i][1] > 0 && grid[newPos[i][1]][newPos[i][0]].Type() == 2){  //Occupied by inactive piece
                return false;
            }
        }
        return true;

    }

    /**
     * Update the change of position on the grid.
     * Make the old position grey (blank), and make the new position the color of the tetromino
     * @param grid This is what we are manipulating.
     * @param oldPos coordinates of the 4 parts of the Tetromino before the change
     * @param newPos coordinates after the change
     */
    public void change(TetrisLabel[][] grid, int[][] oldPos, int[][] newPos){
        for (int i = 0; i < 4; i++) {
            if (oldPos[i][1] >= 0) {
                grid[oldPos[i][1]][oldPos[i][0]].reset();  //make the old position grey
            }
        }
        for (int i = 0; i < 4; i++) {
            if (newPos[i][1] >= 0) {
                grid[newPos[i][1]][newPos[i][0]].updateLabel(color, 1);  //make the new position the color of the active piece
            }
        }
    }

    /**
     * Change the TetrisLabel types of the Tetromino to inactive
     * @param grid This is what we are manipulating.
     */
    public void deactivate(TetrisLabel[][] grid) {
        for (int i = 0; i < 4; i++) {
            if (coordinates[i][1] >= 0) {
                grid[coordinates[i][1]][coordinates[i][0]].setType(2);
            }
        }
    }

    /**
     * Make a deep copy of the 2-d array
     * @param from 2-d array we want to copy
     * @return the new 2-d array
     */
    public int[][] copy2dArray(int[][] from){
        int[][] result = new int[4][2];
        for(int i = 0; i < 4; i++) {
            result[i] = from[i].clone();
        }
        return result;
    }

    /**
     * Check to see if the Tetromino is fully within the grid.
     * @param grid This is what we are checking.
     * @return true if not fully inside grid, otherwise false.
     */
    public boolean gameover(TetrisLabel[][] grid){
        for (int i = 0; i < 4; i++){
            if (coordinates[i][1] < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Define the position of the Tetromino when it is first added to grid.
     */
    abstract void initialPos();

    /**
     * Define the instructions used to change the position of the Tetromino
     * when we want to rotate left.
     * @param grid This is what we are manipulating.
     */
    abstract void rotateLeft(TetrisLabel[][] grid);

    /**
     * Define the instructions used to change the position of the Tetromino
     * when we want to rotate left.
     * @param grid This is what we are manipulating.
     */
    abstract void rotateRight(TetrisLabel[][] grid);
}
