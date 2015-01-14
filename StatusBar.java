import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *  This class is used by Game class to display the information about the
 *  game when it is being played
 */
public class StatusBar extends JPanel implements ActionListener {

    private static Timer timer;
    private JLabel timeElapsed;
    private JLabel remainingMines;
    private JButton restart;
    private ImageIcon reStart = new ImageIcon("restart.jpg");

    // CONSTRUCTOR
    StatusBar(Game game) {
        timer = new Timer(1000, this);
        timeElapsed = new JLabel("0");
        remainingMines = new JLabel(game.getMinesLeft()+"");
        restart = new JButton();
        restart.setIcon(reStart);
        prepareStatusElements(game);
        setBackground(Color.darkGray);
    }

    // Sets up all elements of the status bar
    private void prepareStatusElements(Game game) {
        timeElapsed.setForeground(Color.white);
        remainingMines.setForeground(Color.white);
        restart.setPreferredSize(new Dimension(34,34));
        restart.addMouseListener(game);
        add(timeElapsed);
        add(restart);
        add(remainingMines);
    }

    // Updates the timer
    protected void updateTimer() {
        String time = timeElapsed.getText().trim();
        int t = Integer.parseInt(time);
        if(t >= 1000)
            timer.stop();
        else
        {
            t++;
            timeElapsed.setText(t+"");
        }
    }

    // Starts the Timer
    protected static void startTimer() {
        timer.start();
    }

    // Stops the Timer
    protected static void stopTimer() {
        timer.stop();
    }

    // Sets the Timer to Zero
    protected void resetTimerLabel() {
        timeElapsed.setText("0");
    }

    // Returns True if Timer is on, otherwise false
    protected static boolean isTimerOn() {
        if(timer.isRunning())
            return true;

        return false;
    }

    // Returns the Timer
    protected Timer getTimer() {
        return timer;
    }

    // Returns an integer of the Timer
    protected int getTimerInt() {
        String time = timeElapsed.getText().trim();
        return Integer.parseInt(time);
    }

    // Returns the restart button
    protected JButton getRestartButton() {
        return restart;
    }

    // Updates the Mine label to a new number
    protected void updateMineLabel(int n) {
        remainingMines.setText(n+"");
    }

    // Resets the Mine label back to the proper # of Mines
    protected void resetMineLabel(int mines) {
        remainingMines.setText(Integer.toString(mines));
    }

    // Updates the timer as the time goes on
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == timer)
            updateTimer();
    }
}