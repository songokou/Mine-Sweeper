import javax.swing.*;
import java.awt.*;

/**
 *  A simple Tile class that deals with the logic of a Minesweeper tile.
 *  The logic includes things like changing the look and feel of a tile,
 *  and determining/changing symbol, right click, and hidden status. It
 *  also has awareness of all it's 8 neighbours after Board is created.
 *  This class is immutable and has jurisdiction only inside the Board
 *  class.
 */
public final class Tile extends JButton {

    private int symbol;                     // Used to determine what is under tile
    private int rightClickToggle;           // Used to keep track of right click status
    private boolean hidden;                 // Used to determine if tile is opened
    private boolean isMine;                 // Used to determine if tile is a mine
    private int color;

    // POINTERS TO ADJACENT TILES
    protected Tile left;
    protected Tile right;
    protected Tile up;
    protected Tile down;
    protected Tile upLeft;
    protected Tile upRight;
    protected Tile downLeft;
    protected Tile downRight;

    // CONSTRUCTOR
    Tile(int s) {
        symbol = s;
        rightClickToggle = 0;
        hidden = true;
        isMine = false;
        color = 0;
        // Changing the look and feel of the tile
        //setBackground(new Color(0,100,255));
        setForeground(Color.white);
        setBorder(BorderFactory.createBevelBorder(0));
        setVisible(true);
    }

    // Returns true if a tile has not been opened
    protected boolean isHidden() {
        return hidden;
    }

    // Changes status whether tile is hidden or not
    protected void setHidden(boolean status) {
        hidden = status;
    }

    // Returns true if tile is a mine
    protected boolean isMine() {
        return isMine;
    }

    // Changes whether the tile is a mine or not
    protected void setMineStatus(boolean b) {
        isMine = b;
    }

    // Returns the symbol of a tile
    protected int getSymbol() {
        return symbol;
    }

    // Changes the symbol of a tile
    protected void setSymbol(int s) {
        symbol = s;
    }

    // Stores the Green integer value of RGB color
    protected void saveColor(int c) {
        color = c;
    }

    // Returns the integer value of Green of RGB
    protected int getColor() {
        return color;
    }

    // Returns 0, 1 or 2 depending on the right click status
    protected int getRightClickStatus() {
        return rightClickToggle;
    }

    // Changes the right click status from 0 to 1 to 2 and back to 0
    // 0 if tile not right clicked, 1 if right clicked once and 2 if right clicked twice
    protected void toggleRightClick() {
        if(rightClickToggle == 0)
            rightClickToggle++;
        else if(rightClickToggle == 1)
            rightClickToggle++;
        else
            rightClickToggle = 0;
    }
}