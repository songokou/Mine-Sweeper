import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  This class deals with creating the Menu that sits on top of the JFrame
 */
public class Menu extends JMenuBar {

    // Indentation represents which Menus are nested inside which Menus
    JMenu menuGame;
      JMenu menuNew;
        JMenuItem easy;
        JMenuItem medium;
        JMenuItem hard;
      JMenuItem scoreItem;
      JMenuItem exit;

    JMenu menuInfo;
      JMenuItem help;
      JMenuItem about;

    // CONSTRUCTOR
    Menu() {
        super();
        menuGame = createMenu("Game", 'G');
        menuNew = addSubMenu("New", 'N', menuGame);
        easy = addItem("Easy", 'E', menuNew);
        medium = addItem("Normal", 'N', menuNew);
        hard = addItem("Difficult", 'D', menuNew);
        scoreItem = addItem("Score", 'S', menuGame);
        exit = addItem("Exit", 'E', menuGame);
        menuInfo = createMenu("Info", 'I');
        help = addItem("Help", 'H', menuInfo);
        about = addItem("About", 'A', menuInfo);

        add(menuGame);
        add(menuInfo);
    }

    // Helper function to create a Menu
    private JMenu createMenu(String name, char mnemonic) {
        JMenu temp = new JMenu(name);
        temp.setMnemonic(mnemonic);
        return temp;
    }

    // Helper function to create a SubMenu and add it to a Menu
    private JMenu addSubMenu(String name, char mnemonic, JMenu menu) {
        JMenu temp = new JMenu(name);
        temp.setMnemonic(mnemonic);
        menu.add(temp);
        return temp;
    }

    // Helper function to create a Menu Item and add it to a Menu
    private JMenuItem addItem(String name, char mnemonic, JMenu menu) {
        JMenuItem temp = new JMenuItem(name);
        temp.setMnemonic(mnemonic);
        menu.add(temp);
        return temp;
    }
}