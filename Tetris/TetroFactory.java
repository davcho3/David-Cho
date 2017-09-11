//Robert Hull, Eilbron Davood, David Cho
//
//UIC
//CS342 - Spring 2016
//
//Tetris - TetroFactory.java - Used for retrieving Tetro pieces.

public class TetroFactory {

    //Return the requested Tetromino
    public Tetromino getTetro(int x) {

        switch(x) {
            case 0:
                return Tetro_I.getInstance();
            case 1:
                return Tetro_J.getInstance();
            case 2:
                return Tetro_L.getInstance();
            case 3:
                return Tetro_O.getInstance();
            case 4:
                return Tetro_S.getInstance();
            case 5:
                return Tetro_T.getInstance();
            case 6:
                return Tetro_Z.getInstance();
            default:
                return null;
        }
    }
}
