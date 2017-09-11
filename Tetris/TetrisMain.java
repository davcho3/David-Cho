//Robert Hull, Eilbron Davood, David Cho
//
//UIC
//CS342 - Spring 2016
//
//Tetris - TetrisMain.java - Make the GUI, Handle all events, and Keep Track of all game values.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Random;

public class TetrisMain extends JFrame {

    private final int COLS = 10;
    private final int ROWS = 20;
    private int score, level, rowsCleared, time, delay, nextIndex;
    private Timer timer, clock;
    private boolean flag, paused, endgame;
    private Random rand;
    private Tetromino current, next;
    private TetroFactory tetrofactory;

    private TetrisLabel labelGrid[][];
    private JPanel grid_panel;
    private JLabel next_label, score_label, level_label, rows_label, time_label;
    private ImageIcon pieces[], grey;
    private JButton pause;

    /**
     * Set everything up.
     */
    private TetrisMain() {
        super("Tetris");

        rand = new Random();
        tetrofactory = new TetroFactory();

        //Initial values for all our flags
        flag = true;
        paused = false;
        endgame = false;

        //Make the panel for the grid, load the full-piece pictures and the grey label picture needed to make grid, then make the grid.
        grid_panel = new JPanel(new GridLayout(ROWS, COLS));
        loadPictures();
        makeGrid();

        //Choose the first piece, put it on the grid. Choose the next piece.
        current = tetrofactory.getTetro(rand.nextInt(7));
        current.initialPos();
        nextIndex = rand.nextInt(7);
        next = tetrofactory.getTetro(nextIndex);

        //Sidebar panel (made of info panel and controls panel)
        JPanel sidebar = new JPanel(new GridLayout(2, 1));

        //Info panel - all values associated with the game, and also the picture of the next piece.
        JPanel info = new JPanel(new GridLayout(5, 1));
        next_label = new JLabel(pieces[nextIndex]);
        score_label = new JLabel();
        level_label = new JLabel();
        rows_label = new JLabel();
        time_label = new JLabel();
        info.add(next_label);
        info.add(score_label);
        info.add(level_label);
        info.add(rows_label);
        info.add(time_label);
        score = 0;
        level = 1;
        rowsCleared = 0;
        time = 0;
        setInfo();

        //Controls panel - 6 buttons for playing game
        JPanel controls = new JPanel(new GridLayout(2, 3));
        JButton rotateLeft = new JButton("Rotate Left");
        JButton rotateRight = new JButton("Rotate Right");
        JButton moveLeft = new JButton("Left");
        JButton moveRight = new JButton("Right");
        JButton drop = new JButton("Drop");
        pause = new JButton("Pause");

        controls.add(rotateLeft);
        controls.add(pause);
        controls.add(rotateRight);
        controls.add(moveLeft);
        controls.add(drop);
        controls.add(moveRight);

        //Don't let buttons take focus, so JFrame always has focus and keyListener works correctly
        rotateLeft.setFocusable(false);
        rotateRight.setFocusable(false);
        moveLeft.setFocusable(false);
        moveRight.setFocusable(false);
        drop.setFocusable(false);
        pause.setFocusable(false);

        //Add the 6 button listener, and the 1 key listener
        rotateLeft.addActionListener(new rotateLeft());
        rotateRight.addActionListener(new rotateRight());
        moveLeft.addActionListener(new moveLeft());
        moveRight.addActionListener(new moveRight());
        drop.addActionListener(new hardDrop());
        pause.addActionListener(new pause());
        addKeyListener(new keyControl());

        sidebar.add(info);
        sidebar.add(controls);

        //Setup menu
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem restart = new JMenuItem("Restart");
        JMenuItem about = new JMenuItem("About");
        JMenuItem help = new JMenuItem("Help");
        JMenuItem quit = new JMenuItem("Quit");

        menu.add(file);
        file.add(restart);
        file.add(about);
        file.add(help);
        file.add(quit);

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score = 0;
                level = 1;
                rowsCleared = 0;
                time = 0;
                setInfo();

                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        labelGrid[i][j].reset();
                    }
                }
                current = tetrofactory.getTetro(rand.nextInt(7));
                current.initialPos();
                nextIndex = rand.nextInt(7);
                next = tetrofactory.getTetro(nextIndex);

                timer.start();
                clock.start();
                paused = false;
                endgame = false;
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog( null,
                        "Made by Robert Hull, Eilbron Davood, and David Cho\nUIC CS 342 Spring 2016.", "About", JOptionPane.PLAIN_MESSAGE);
            }
        });
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog( null,
                        "Clear rows by filling them, to gain points/level up.\nYou can use the buttons or a keyboard to play.\nThe keys 'a', 's', 'd' correspond to the buttons on the top row, and 'left', 'down', 'right' arrows correspond to the bottom row.", "Help", JOptionPane.PLAIN_MESSAGE);
            }
        });
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                clock.stop();
                dispose();
            }
        });

        //Setup JFrame
        setLayout(new BorderLayout());
        add(grid_panel, BorderLayout.CENTER);
        add(sidebar, BorderLayout.WEST);
        setJMenuBar(menu);
        setSize( 700, 700 );
        setLocationRelativeTo(null);
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setVisible( true );
        setResizable(false);
        setFocusable(true);

        //Create the timers, and start the game.
        timer = new Timer(800, new TimerAction());
        clock = new Timer(250, new ClockUpdate());
        timer.start();
        clock.start();
    }

    public static void main(String args[]){
        new TetrisMain();
    }

    /**
     * Load the 7 full-piece pictures, and also the grey color.
     */
    private void loadPictures(){
        pieces = new ImageIcon[7];
        pieces[0] = new ImageIcon(getClass().getResource("Images/I.jpg"));
        pieces[1] = new ImageIcon(getClass().getResource("Images/J.jpg"));
        pieces[2] = new ImageIcon(getClass().getResource("Images/L.jpg"));
        pieces[3] = new ImageIcon(getClass().getResource("Images/O.jpg"));
        pieces[4] = new ImageIcon(getClass().getResource("Images/S.jpg"));
        pieces[5] = new ImageIcon(getClass().getResource("Images/T.jpg"));
        pieces[6] = new ImageIcon(getClass().getResource("Images/Z.jpg"));

        grey = new ImageIcon(getClass().getResource("Images/grey.jpg"));
    }

    /**
     * Make the 2-d array of TetrisLabels and add them to the grid.
     */
    private void makeGrid(){
        labelGrid = new TetrisLabel[20][10];

        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                labelGrid[i][j] = new TetrisLabel(grey);
                grid_panel.add(labelGrid[i][j]);
            }
        }
    }

    /**
     * Take the current "game info" values and update the values being displayed on the GUI
     */
    private void setInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        score_label.setText("Score: " + Integer.toString(score));
        level_label.setText("Level: " + Integer.toString(level));
        rows_label.setText("Rows Cleared: " + Integer.toString(rowsCleared));
        time_label.setText(sdf.format(time));
    }

    /**
     * Calculate how many points were earned for the last clearing of rows.
     * @param x How many rows were cleared.
     * @param y Current level
     * @return points earned
     */
    private int calcScore(int x, int y) {

        switch(x) {
            case 0:
                return 0;
            case 1:
                return y * 40;
            case 2:
                return y * 100;
            case 3:
                return y * 300;
            case 4:
                return y * 1200;
            default:
                return 0;
        }
    }

    /**
     * If the game isn't paused, set the flag to false to tell the game to not drop this cycle,
     * and then rotate the current piece on the grid to the left.
     */
    private void rotateLeft() {
        if (!paused) {
            flag = false;
            current.rotateLeft(labelGrid);
        }
    }

    /**
     * Listener for button, calls the rotateLeft() function.
     */
    private class rotateLeft implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            rotateLeft();
        }
    }

    /**
     * If the game isn't paused, set the flag to false to tell the game to not drop this cycle,
     * and then rotate the current piece on the grid to the right.
     */
    private void rotateRight() {
        if (!paused) {
            flag = false;
            current.rotateRight(labelGrid);
        }
    }

    /**
     * Listener for button, calls the rotateRight() function.
     */
    private class rotateRight implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            rotateRight();
        }
    }

    /**
     * If the game is paused and the game isn't over, resume the game.
     * If the game is being play, pause the game.
     */
    private void pause() {
        if (paused && !endgame) {
            pause.setText("Pause");
            timer.start();
            clock.start();
            paused = false;
        } else {
            pause.setText("Resume");
            timer.stop();
            clock.stop();
            paused = true;
        }
    }

    /**
     * Listener for button, calls the pause() function.
     */
    private class pause implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            pause();
        }
    }

    /**
     * If the game isn't paused, set the flag to false to tell the game to not drop this cycle,
     * and then move the current piece on the grid to the left.
     */
    private void moveLeft() {
        if (!paused) {
            flag = false;
            current.moveLeft(labelGrid);
        }
    }

    /**
     * Listener for button, calls the moveLeft() function.
     */
    private class moveLeft implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            moveLeft();
        }
    }

    /**
     * If the game isn't paused, set the flag to false to tell the game to not drop this cycle,
     * and then move the current piece on the grid to the right.
     */
    private void moveRight() {
        if (!paused) {
            flag = false;
            current.moveRight(labelGrid);
        }
    }

    /**
     * Listener for button, calls the moveRight() function.
     */
    private class moveRight implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            moveRight();
        }
    }

    /**
     * If the game isn't paused, move the current piece on the grid straight down as
     * far as it can go.
     */
    private void hardDrop() {
        if (!paused) {
            paused = true;
            while(!current.drop(labelGrid));
        }
    }

    /**
     * Listener for button, calls the hardDrop() function.
     */
    private class hardDrop implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            hardDrop();
        }
    }

    /**
     * Listener for keys, calls any of the move/rotate/drop/pause functions
     */
    private class keyControl implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {

            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    moveRight();
                    break;
                case KeyEvent.VK_DOWN:
                    hardDrop();
                    break;
                case KeyEvent.VK_A:
                    rotateLeft();
                    break;
                case KeyEvent.VK_S:
                    pause();
                    break;
                case KeyEvent.VK_D:
                    rotateRight();
                    break;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {}


    }

    /**
     * Calculate the new delay.
     * Drop the piece if we didn't just rotate/move.
     * If we hit something when we dropped, check if the game is over.
     *      If the game is over, stop everything, and display "game over" popup.
     *      If the game isn't over, check if rows need to be cleared, calculate new score
     *          update number of rows cleared and update level.
     *          Make the next piece, the current piece and choose a new random next piece.
     * Call setInfo() to update the GUI info panel.
     */
    private class TimerAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            delay = 16 * (50 - (level * 2));
            timer.setDelay(delay);

            if (flag && current.drop(labelGrid)) {
                paused = false;
                if (current.gameover(labelGrid)){
                    timer.stop();
                    clock.stop();
                    paused = true;
                    endgame = true;
                    JOptionPane.showMessageDialog( null,
                            "Game Over!\nPress File->Restart to play again.", "Game Over!", JOptionPane.PLAIN_MESSAGE);
                } else {
                    int tempRows = current.clearRows(labelGrid);
                    score += calcScore(tempRows, level);
                    rowsCleared += tempRows;
                    level = (rowsCleared / 10) + 1;

                    current = next;
                    current.initialPos();
                    nextIndex = rand.nextInt(7);
                    next = tetrofactory.getTetro(nextIndex);
                    next_label.setIcon(pieces[nextIndex]);

                }
            } else {
                flag = true;
            }
            setInfo();
        }
    }

    /**
     * Keeps track of how long the game has been playing.
     * Call setInfo() to update the GUI info panel.
     *
     */
    private class ClockUpdate implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            time += 250;
            setInfo();
        }
    }
}


