import javax.swing.*;
import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 *  This class deals with the logic of the Score Board. The logic
 *  includes things like loading the top 10 scores from a file,
 *  saving the new top 10 scores to a file, parsing the lines
 *  read from a file and showing the Score Board when User clicks
 *  the Score button
 */
public class ScoreBoard extends JOptionPane {

    private TreeMap<Integer,String> scores;             // Used to store user names and score
    private final int N_SCORES = 10;                    // Used to specify the # scores to keep

    // CONSTRUCTOR
    ScoreBoard() {
        scores = new TreeMap<Integer, String>();
    }

    // Loads the names and scores from a file, parses it and stores it into a TreeMap
    protected void load() {
        try {
            File score = new File("score.txt");

            if(!score.exists()) {
                score.createNewFile();
            }

            FileInputStream file = new FileInputStream(score);
            DataInputStream in = new DataInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String strLine;
            String parsedLine[];
            String playerName = "";
            int playerScore;

            int loadedScores = 0;
            while ((strLine = reader.readLine()) != null && loadedScores < N_SCORES)   {
                parsedLine = strLine.split(" ");
                for(int j = 0; j < parsedLine.length-1; j++) {
                    playerName += parsedLine[j] + " ";
                }
                playerScore = Integer.parseInt(parsedLine[parsedLine.length-1]);
                scores.put(playerScore, playerName);
                playerName = "";
                loadedScores++;
            }

            in.close();
            file.close();
            reader.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Saves the names and scores stored in a TreeMap into a file
    private void save() {
        try {
            File score = new File("score.txt");

            if(!score.exists())
                score.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(score));

            for(Map.Entry<Integer,String> entry : scores.entrySet()) {
                bw.write(entry.getValue() + " " + entry.getKey());
                bw.newLine();
            }

            bw.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Gets the length of longest name in the TreeMap (Used to somewhat format the ScoreBoard)
    private int getLongestName() {
        int nameLength = 0;
        int i = 0;

        for(Map.Entry<Integer,String> entry : scores.entrySet()) {
            if(entry.getValue().length() > nameLength && i != N_SCORES)
                nameLength = entry.getValue().length();
            i++;
        }

        return nameLength;
    }

    // Prompts the user to input the name, gets the score and puts it into a TreeMap
    protected void passWinnerScore(int score) {
        String playerName;
        playerName = JOptionPane.showInputDialog(null, "Please type in your name", "You win", JOptionPane.PLAIN_MESSAGE);
        scores.put(score, playerName);
        save();
    }

    // Pops up a new Window with the top 10 scores
    protected void showScore() {
        String scoreString = "";
        int printedScores = 0;
        int shiftAmount;
        String shift = "";

        for(Map.Entry<Integer,String> entry : scores.entrySet()) {
            if(printedScores < N_SCORES && entry.getValue() != null) {
                shiftAmount = getLongestName() - entry.getValue().length();
                for(int i = 0; i < shiftAmount; i++)
                    shift += " ";
                scoreString += shift + entry.getValue() + " | Score: " + entry.getKey() + "\n";
                shift = "";
            }
            printedScores++;
        }

        Object options[] = {"RESET","OK"};
        int m = JOptionPane.showOptionDialog(null, scoreString, "Top 10 Scores",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        if (m ==0)
            reset();
    }

    // Resets the top 10 score board
    private void reset() {
        try {
            File score = new File("score.txt");
            if (!score.exists())
                ;
            else {
                score.delete();
                scores.clear();
            }
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}