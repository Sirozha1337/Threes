import javax.swing.*;

/**
 * Created by Sirozha on 03.11.2015.
 */
public class Main {
    public static void main(String args[]){
        JFrame f = new JFrame();
        Gameboard gameBoard = new Gameboard();
        f.addKeyListener(gameBoard);
        f.setBounds(30, 30, 400, 540);
        f.setResizable(false);
        f.setContentPane(gameBoard);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
