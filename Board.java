import java.awt.event.*;
import java.util.Random;
import java.awt.*;
import javax.swing.*;

/**
 *  This class deals with the logic of the Minesweeper board.
 *  The logic includes things like placing mines, linking neighbors,
 *  and determining # of mines adjacent to each tile. It also deals
 *  with things like revealing a single or multiple tiles when clicked
 *  on a tile and keeping track of tiles opened or marked. There is a
 *  Listener sub-class nested inside that listens to when Minesweeper tiles
 *  are being interacted with. This class is immutable and has jurisdiction
 *  only inside the Game class.
 */

public final class Board extends JPanel {

    private final Tile[][] board;                   // Used to represent the board of Tiles
    private final int xSize;                        // # of Columns
    private final int ySize;                        // # of Rows
    private final int nMines;                       // # of Mines
    private int tilesOpened;                        // Used to keep track of Tiles opened

    // Game Icons
    ImageIcon mine = new ImageIcon("mine.png");
    ImageIcon flag = new ImageIcon("flag.png");
    ImageIcon question = new ImageIcon("q.png");

    // CONSTRUCTOR
    public Board(int x, int y, int mines) {
        board = new Tile[x][y];
        xSize = x;
        ySize = y;
        nMines = mines;
        tilesOpened = 0;
        prepareBoard(x, y);
        printBoard();
    }

    // Initializes, prepares and fills the board with Tiles
    private void prepareBoard(int height, int width) {
        Listener clickHandler = new Listener();                     // Used to listen to Tile clicks
        setLayout(new GridLayout(height,width,3,3));
        setBackground(Color.darkGray);
        int colorChange = 100 / (height + width);                   // Used for gradient effect
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                int changeAmount = j+i;                             // Used for gradient effect
                changeAmount *= colorChange;                        // Increases with board dimensions
                Color paint = new Color(0,100-changeAmount,255);
                board[i][j] = new Tile(0);
                add(board[i][j]);
                board[i][j].setPreferredSize(new Dimension(20, 20));
                board[i][j].addMouseListener(clickHandler);
                board[i][j].setBackground(paint);
                board[i][j].saveColor(100-changeAmount);            // Save color for hover effect
            }
        }
        placeMines();
        linkNeighbors();
        setBoardNumbers();
    }

    // Randomly places mines throughout the board represented by an integer 9
    private void placeMines() {
        Random generator = new Random();
        for(int i = 0; i < nMines; i++) {
            int x = generator.nextInt(xSize);
            int y = generator.nextInt(ySize);
            if(!board[x][y].isMine()) {
                board[x][y].setSymbol(9);
                board[x][y].setMineStatus(true);
            }
            else                                        // If picked tile is a mine then redo
                i--;
        }
    }

    // Initializes Tile neighbor pointers
    private void linkNeighbors() {
        for(int i = 0; i < xSize; i++) {
            for(int j = 0; j < ySize; j++) {
                if(i > 0 && i < xSize-1 && j > 0 && j < ySize-1) {
                    board[i][j].upLeft = board[i-1][j-1];
                    board[i][j].up = board[i][j-1];
                    board[i][j].upRight = board[i+1][j-1];
                    board[i][j].right = board[i+1][j];
                    board[i][j].downRight = board[i+1][j+1];
                    board[i][j].down = board[i][j+1];
                    board[i][j].downLeft = board[i-1][j+1];
                    board[i][j].left = board[i-1][j];
                }
                else if(i > 0 && i < xSize-1 && j == 0) {
                    board[i][j].upLeft = null;
                    board[i][j].up = null;
                    board[i][j].upRight = null;
                    board[i][j].right = board[i+1][j];
                    board[i][j].downRight = board[i+1][j+1];
                    board[i][j].down = board[i][j+1];
                    board[i][j].downLeft = board[i-1][j+1];
                    board[i][j].left = board[i-1][j];
                }
                else if(i == 0 && j > 0 && j < ySize-1) {
                    board[i][j].up = board[i][j-1];
                    board[i][j].upRight = board[i+1][j-1];
                    board[i][j].right = board[i+1][j];
                    board[i][j].downRight = board[i+1][j+1];
                    board[i][j].down = board[i][j+1];
                    board[i][j].downLeft = null;
                    board[i][j].left = null;
                    board[i][j].upLeft = null;
                }
                else if(i > 0 && i < xSize-1 && j == ySize-1) {
                    board[i][j].left = board[i-1][j];
                    board[i][j].upLeft = board[i-1][j-1];
                    board[i][j].up = board[i][j-1];
                    board[i][j].upRight = board[i+1][j-1];
                    board[i][j].right = board[i+1][j];
                    board[i][j].downRight = null;
                    board[i][j].down = null;
                    board[i][j].downLeft = null;
                }
                else if(i == xSize-1 && j > 0 && j < ySize-1) {
                    board[i][j].down = board[i][j+1];
                    board[i][j].downLeft = board[i-1][j+1];
                    board[i][j].left = board[i-1][j];
                    board[i][j].upLeft = board[i-1][j-1];
                    board[i][j].up = board[i][j-1];
                    board[i][j].upRight = null;
                    board[i][j].right = null;
                    board[i][j].downRight = null;
                }
                else if(i == 0 && j == 0) {
                    board[i][j].upLeft = null;
                    board[i][j].up = null;
                    board[i][j].upRight = null;
                    board[i][j].right = board[i+1][j];
                    board[i][j].downRight = board[i+1][j+1];
                    board[i][j].down = board[i][j+1];
                    board[i][j].downLeft = null;
                    board[i][j].left = null;
                }
                else if(i == 0 && j == ySize-1) {
                    board[i][j].up = board[i][j-1];
                    board[i][j].upRight = board[i+1][j-1];
                    board[i][j].right = board[i+1][j];
                    board[i][j].downRight = null;
                    board[i][j].down = null;
                    board[i][j].downLeft = null;
                    board[i][j].left = null;
                }
                else if(i == xSize-1 && j == ySize-1) {
                    board[i][j].left = board[i-1][j];
                    board[i][j].upLeft = board[i-1][j-1];
                    board[i][j].up = board[i][j-1];
                    board[i][j].upRight = null;
                    board[i][j].right = null;
                    board[i][j].downRight = null;
                    board[i][j].down = null;
                    board[i][j].downLeft = null;
                }
                else {
                    board[i][j].down = board[i][j+1];
                    board[i][j].downLeft = board[i-1][j+1];
                    board[i][j].left = board[i-1][j];
                    board[i][j].upLeft = null;
                    board[i][j].up = null;
                    board[i][j].upRight = null;
                    board[i][j].right = null;
                    board[i][j].downRight = null;
                }
            }
        }
    }

    // Using Tile's knowledge of it's neighbors sets the # of adjacent Mines
    private void setBoardNumbers() {
        int adjMines = 0;
        for(int i = 0; i < xSize; i++) {
            for(int j = 0; j < ySize; j++) {
                if(board[i][j].up != null && board[i][j].up.isMine() ) { adjMines++; }
                if(board[i][j].upRight != null && board[i][j].upRight.isMine() ) { adjMines++; }
                if(board[i][j].right != null && board[i][j].right.isMine() ) { adjMines++; }
                if(board[i][j].downRight != null && board[i][j].downRight.isMine() ) { adjMines++; }
                if(board[i][j].down != null && board[i][j].down.isMine() ) { adjMines++; }
                if(board[i][j].downLeft != null && board[i][j].downLeft.isMine() ) { adjMines++; }
                if(board[i][j].left != null && board[i][j].left.isMine() ) { adjMines++; }
                if(board[i][j].upLeft != null && board[i][j].upLeft.isMine() ) { adjMines++; }
                if(!board[i][j].isMine() ) {
                    board[i][j].setSymbol(adjMines);
                }
                adjMines = 0;
            }
        }
    }

    // Returns the # of Tiles opened
    protected int getTilesOpened() {
        return tilesOpened;
    }

    // Returns the # of Mines on a board
    protected int getMineCount() {
        return nMines;
    }

    // Returns the area of the Board (rows*columns)
    protected int getBoardSize() {
        return xSize*ySize;
    }

    // Debugger function to print the Board in console
    private void printBoard() {
        for(int i = 0; i < xSize; i++) {
            for(int j = 0; j < ySize; j++) {
                System.out.print(board[i][j].getSymbol() + " ");
                if(j != 0 && j % 9 == 0) { System.out.println(); }
            }
        }
        System.out.println();
    }

    // Using Tile's knowledge of it's neighbors it recursively opens all
    // Tiles that have no adjacent Mines
    private void openAdjZeros(Tile tile) {
        reveal(tile);
        if(tile.up != null && tile.up.isHidden() && !tile.up.isMine() && tile.up.getRightClickStatus() != 1) {
            if(tile.up.getSymbol() == 0) {
                openAdjZeros(tile.up);
            }
            else {
                reveal(tile.up);
            }
        }
        if(tile.upRight != null && tile.upRight.isHidden() && !tile.upRight.isMine() && tile.upRight.getRightClickStatus() != 1) {
            if(tile.upRight.getSymbol() == 0) {
                openAdjZeros(tile.upRight);
            }
            else {
                reveal(tile.upRight);
            }
        }
        if(tile.right != null && tile.right.isHidden() && !tile.right.isMine() && tile.right.getRightClickStatus() != 1) {
            if(tile.right.getSymbol() == 0) {
                openAdjZeros(tile.right);
            }
            else {
                reveal(tile.right);
            }
        }
        if(tile.downRight != null && tile.downRight.isHidden() && !tile.downRight.isMine() && tile.downRight.getRightClickStatus() != 1) {
            if(tile.downRight.getSymbol() == 0) {
                openAdjZeros(tile.downRight);
            }
            else {
                reveal(tile.downRight);
            }
        }
        if(tile.down != null && tile.down.isHidden() && !tile.down.isMine() && tile.down.getRightClickStatus() != 1) {
            if(tile.down.getSymbol() == 0) {
                openAdjZeros(tile.down);
            }
            else {
                reveal(tile.down);
            }
        }
        if(tile.downLeft != null && tile.downLeft.isHidden() && !tile.downLeft.isMine() && tile.downLeft.getRightClickStatus() != 1) {
            if(tile.downLeft.getSymbol() == 0) {
                openAdjZeros(tile.downLeft);
            }
            else {
                reveal(tile.downLeft);
            }
        }
        if(tile.left != null && tile.left.isHidden() && !tile.left.isMine() && tile.left.getRightClickStatus() != 1) {
            if(tile.left.getSymbol() == 0) {
                openAdjZeros(tile.left);
            }
            else {
                reveal(tile.left);
            }
        }
        if(tile.upLeft != null && tile.upLeft.isHidden() && !tile.upLeft.isMine()  && tile.upLeft.getRightClickStatus() != 1) {
            if(tile.upLeft.getSymbol() == 0) {
                openAdjZeros(tile.upLeft);
            }
            else {
                reveal(tile.upLeft);
            }
        }
    }

    // Reveals a Tile and it's contents by changing to appropriate Icon or Symbol
    protected void reveal(Tile tile) {
        tile.setHidden(false);
        tile.setBackground(new Color(0,100,200));
        tile.setBorder(BorderFactory.createBevelBorder(1));

        if(tile.getRightClickStatus() == 2)
            tile.setIcon(null);
        if(tile.getSymbol() == 0)
            tile.setText("");
        else if(!tile.isMine()) {
            if(tile.getSymbol() > 2)
                tile.setForeground(new Color(255,150,0));
            tile.setText(Integer.toString(tile.getSymbol()));
        }
        else {
            // Add Code to notify the game that we lost
            tile.setIcon(mine);
        }

        tilesOpened++;
    }

    // Opens the entire Board and shows wrongly guessed Mines
    protected void showBoard() {
        for(int i = 0; i < xSize; i++) {
            for(int j = 0; j < ySize; j++) {
                if(board[i][j].isHidden())
                    if(board[i][j].getRightClickStatus() == 1 && !board[i][j].isMine()) {
                        board[i][j].setBackground(Color.red);
                    }
                    else
                        reveal(board[i][j]);
            }
        }
    }

    /**
     *  This class handles the actions when a Tile is clicked.
     *  It starts and stops the Timer, updates StatusBar, opens
     *  or marks Tiles with appropriate Icons, checks to see if
     *  User won or Lost the Game. It also handles the case
     *  when a Tile is hovered to add cool visual effects.
     *  This class is a Private Sub-Class making it visible
     *  only to Board Class.
     */
    private class Listener implements MouseListener {

        public void mouseClicked(MouseEvent event) {
            Tile tile = (Tile)event.getSource();
            if(!StatusBar.isTimerOn() && tile.isHidden())
                StatusBar.startTimer();
            if(tile.isHidden() && SwingUtilities.isRightMouseButton(event)) {
                // If Tile has not been marked mark it with a Flag
                if(tile.getRightClickStatus() == 0) {
                    Game.updateMinesLeft(-1);
                    tile.setIcon(flag);
                    tile.toggleRightClick();
                }
                // If Tile has been marked with a Flag then mark it with Question
                else if(tile.getRightClickStatus() == 1) {
                    Game.updateMinesLeft(1);
                    tile.setIcon(question);
                    tile.toggleRightClick();
                }
                // If Tile has been marked with Question then change to blank Tile
                else if (tile.getRightClickStatus()==2){
                    tile.setIcon(null);
                    tile.toggleRightClick();
                }
            }
            // If Tile has not been marked and revealed
            else if(tile.isHidden() && tile.getRightClickStatus() == 0) {
                if(tile.isMine()) {
                    StatusBar.stopTimer();
                    showBoard();
                    JOptionPane.showMessageDialog(null, "You hit a mine!", "Game Over", JOptionPane.PLAIN_MESSAGE);
                }
                else if(tile.getSymbol() == 0) {
                    openAdjZeros(tile);
                }
                else {
                    reveal(tile);
                }
                Game.checkForWin();
            }
        }

        public void mouseEntered(MouseEvent event) {
            Tile tile = (Tile)event.getSource();
            if(tile.isHidden())
                tile.setBackground(new Color(0,tile.getColor()+35,255));
        }

        public void mouseExited(MouseEvent event) {
            Tile tile = (Tile)event.getSource();
            if(tile.isHidden())
                tile.setBackground(new Color(0,tile.getColor(),255));
        }

        public void mousePressed(MouseEvent event) {

        }

        public void mouseReleased(MouseEvent event) {

        }
    }
}