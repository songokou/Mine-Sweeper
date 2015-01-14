import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.*;

/**
 *  This class deals with the logic of the Game. The logic includes things
 *  like performing actions when different Menus are clicked, creating
 *  appropriate Board size according to difficulty, checking if User won
 *  the Game and updating necessary statusBar elements
 */
public class Game extends JFrame implements MouseListener, ActionListener {

    private Container container;                // Contains the gameBoard and statusBar
    private Menu toolbar;                       // Used for the top Game Menu
    private static StatusBar statusBar;         // Used to display Game Information
    private static ScoreBoard scoreBoard;       // Used to save Players' Scores
    private static Board gameBoard;             // Game Board used for Minesweeper
    private Level gameMode;                     // Used to set Difficulty of the Game
    private static int minesLeft;               // Used to display # of Mines left to Flag
    // Game constants
    private static int WIN_CONDITION;           // Used to check if User won the Game
    private final int MINES_EASY = 10;          // # of Mines for Easy Level
    private final int MINES_MEDIUM = 40;        // # of Mines for Medium Level
    private final int MINES_HARD = 60;          // # of Mines for Hard Level

    // Represents the Difficulty of the Game and # of Mines
    protected enum Level {
        EASY(10), MEDIUM(40), HARD(60);

        int mines;

        Level(int m) {
            mines = m;
        }
    }

    // CONSTRUCTOR
    Game() {
        container = getContentPane();
        // Initializes all parts of the Game
        initializeToolbar();
        initializedBoard();
        initializeGameElements();
        // Sets the Win condition appropriately according to Board size and Mine count
        WIN_CONDITION = gameBoard.getBoardSize() - gameBoard.getMineCount();

        setResizable(false);
        setVisible(true);
        setSize(450, 500);
    }

    public static void main(String args[]) {
        Game newGame = new Game();
        scoreBoard.load();
        newGame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

    }

    // Helper function to create the Menu for the Game, used inside Constructor
    private void initializeToolbar() {
        toolbar = new Menu();
        toolbar.easy.addActionListener(this);
        toolbar.medium.addActionListener(this);
        toolbar.hard.addActionListener(this);
        toolbar.scoreItem.addActionListener(this);
        toolbar.help.addActionListener(this);
        toolbar.about.addActionListener(this);
        toolbar.exit.addActionListener(this);
        setJMenuBar(toolbar);
    }

    // Helper function to create the Board for the Game, used inside Constructor
    private void initializedBoard() {
        gameBoard = new Board(10,10, 10);
        gameBoard.addMouseListener(this);
        gameMode = Level.EASY;
    }

    // Helper function to create the rest of Game elements, used inside Constructor
    private void initializeGameElements() {
        statusBar = new StatusBar(this);
        scoreBoard = new ScoreBoard();
        minesLeft = gameBoard.getMineCount();
        statusBar.updateMineLabel(minesLeft);
        container.add(gameBoard, BorderLayout.CENTER);
        container.add(statusBar, BorderLayout.NORTH);
    }

    // Erases the old Board, resets all necessary elements and creates a new Board
    // based on the level of difficulty
    private void newGame(Level lv) {
        StatusBar.stopTimer();
        statusBar.resetTimerLabel();
        statusBar.resetMineLabel(lv.mines);
        minesLeft = lv.mines;
        switch (lv) {
            case EASY:
                container.remove(gameBoard);
                gameBoard = new Board(10,10,MINES_EASY);
                gameMode = lv;
                container.add(gameBoard, BorderLayout.CENTER);
                setSize(450, 500);
                setVisible(true);
                break;
            case MEDIUM:
                container.remove(gameBoard);
                gameBoard = new Board(15,15,MINES_MEDIUM);
                gameMode = lv;
                container.add(gameBoard, BorderLayout.CENTER);
                setSize(670, 709);
                setVisible(true);
                break;
            case HARD:
                container.remove(gameBoard);
                gameBoard = new Board(15, 20, MINES_HARD);
                gameMode = lv;
                container.add(gameBoard, BorderLayout.CENTER);
                setSize(850, 709);
                setVisible(true);
                break;
        }
    }

    // Checks to see if the Win condition has been met. If yes, it opens the rest of
    // the needed Tiles and notifies scoreBoard object to get the User's info
    public static void checkForWin() {
        if(gameBoard.getTilesOpened() == WIN_CONDITION) {
            statusBar.getTimer().stop();
            gameBoard.showBoard();
            scoreBoard.passWinnerScore(statusBar.getTimerInt());
        }
    }

    // Updates the Mine label in the statusBar that reports # of Mines left
    protected static void updateMinesLeft(int n) {
        statusBar.updateMineLabel(minesLeft+=n);
    }

    // Returns # of Mines left to mark
    protected int getMinesLeft() {
        return minesLeft;
    }

    // Deals with actions of the Menu
    public void actionPerformed(ActionEvent event) {

        if(event.getSource() == toolbar.easy) {
            statusBar.getTimer().stop();
            newGame(Level.EASY);
        }
        else if(event.getSource() == toolbar.medium) {
            statusBar.getTimer().stop();
            newGame(Level.MEDIUM);
        }
        else if(event.getSource() == toolbar.hard) {
            statusBar.getTimer().stop();
            newGame(Level.HARD);
        }
        else if(event.getSource() == toolbar.scoreItem) {
            scoreBoard.showScore();
        }
        else if(event.getSource() == toolbar.help) {
            String message = "Step 1: Select a difficulty level.\n" +
                             "        - Click Game in the upper-left corner,\n" +
                             "          and select Easy, Normal, or Difficult\n" +
                             "Step 2: Notice the numbers above the board.\n" +
                             "         - Number on the left is the time,\n" +
                             "          number on the right is the number\n" +
                             "          of bombs left, and in middle is reset\n" +
                             "Step 3: Click on any tile.\n" +
                             "        - Left click opens a tile, right click\n" +
                             "          marks the tile with a flag or question.\n" +
                             "          Flag means you know there is a Mine under\n" +
                             "          the tile and Question means you are not sure.\n" +
                             "Step 4: Check the numbers.\n" +
                             "        - The numbers represent how many Mines are\n" +
                             "          adjacent to the Tile you just opened. They" +
                             "          help you find out where Mines are hidden.\n" +
                             "Step 5: Win the Game.\n" +
                             "        - To win the game you must open ALL the Tiles\n" +
                             "          that do not contain Mines.";
            JOptionPane.showMessageDialog(null, message, "Rules of Minesweeper", JOptionPane.PLAIN_MESSAGE);
        }
        else if(event.getSource() == toolbar.about) {
            String message = "MySweeper created by: Ze Li and Titus Juocepis";
            JOptionPane.showMessageDialog(null, message, "About", JOptionPane.PLAIN_MESSAGE);
        }
        else if(event.getSource() == toolbar.exit) {
            System.exit(0);
        }
    }

    // Restarts the Board when the button is clicked
    public void mouseClicked(MouseEvent event) {
        if(event.getSource() == statusBar.getRestartButton()) {
            newGame(gameMode);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        }
    }

    // UNUSED MOUSE ACTION METHODS
    public void mousePressed(MouseEvent event) {

    }

    public void mouseReleased(MouseEvent event) {

    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {

    }
}