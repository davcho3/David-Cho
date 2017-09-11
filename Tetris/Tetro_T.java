//Robert Hull, Eilbron Davood, David Cho
//
//UIC
//CS342 - Spring 2016
//
//Tetris - Tetro_T.java

import javax.swing.*;

public class Tetro_T extends Tetromino {

    private int rotate_cords1A[][];
    private int rotate_cords1B[][];
    private int rotate_cords1C[][];
    private int rotate_cords1D[][];
    private int rotate_cords2A[][];
    private int rotate_cords2B[][];
    private int rotate_cords2C[][];
    private int rotate_cords2D[][];
    private int pos;

    private static Tetro_T instance = new Tetro_T();

    private Tetro_T() {
        color = new ImageIcon(getClass().getResource("Images/purple.jpg"));
    }

    public static Tetro_T getInstance() {
        return instance;
    }

    public void initialPos(){
        pos = 0;
        coordinates = new int[][]{{4, -1}, {5, -1}, {6, -1}, {5, -2}};

        //Coordinates for Rotate Left
        rotate_cords1A = new int[][]{{0, 0}, {0, 0}, {-1, 1}, {0, 0}};
        rotate_cords1B = new int[][]{{0, 0}, {0, 0}, {1, -1}, {0, 2}};
        rotate_cords1C = new int[][]{{1, 1}, {0, 0}, {0, 0}, {0, -2}};
        rotate_cords1D = new int[][]{{-1, -1}, {0, 0}, {0, 0}, {0, 0}};

        //Coordinates for Rotate Right
        rotate_cords2A = new int[][]{{1, 1}, {0, 0}, {0, 0}, {0, 0}};
        rotate_cords2B = new int[][]{{-1, -1}, {0, 0}, {0, 0}, {0, 2}};
        rotate_cords2C = new int[][]{{0, 0}, {0, 0}, {-1, 1}, {0, -2}};
        rotate_cords2D = new int[][]{{0, 0}, {0, 0}, {1, -1}, {0, 0}};


    }

    public void rotateLeft(TetrisLabel[][] grid) {

        int tempPos;
        temp = copy2dArray(coordinates);
        if (pos == 0) {
            tempPos = 1;
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords1A[i][j];
                }
            }
        }
        else if (pos == 1){
            tempPos = 2;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords1B[i][j];
                }
            }
        }
        else if (pos == 2){
            tempPos = 3;
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords1C[i][j];
                }
            }
        }

        else{
            tempPos = 0;
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords1D[i][j];
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
            tempPos = 3;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords2A[i][j];
                }
            }
        }

        else if (pos == 3){
            tempPos = 2;
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords2B[i][j];
                }
            }
        }
        else if (pos == 2){
            tempPos = 1;
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords2C[i][j];
                }
            }
        }

        else{
            tempPos = 0;
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 2; j++) {
                    temp[i][j] += rotate_cords2D[i][j];
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