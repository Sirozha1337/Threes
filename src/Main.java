import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created by Sirozha on 03.11.2015.
 */
public class Main {
    public static void main(String args[]){
        JFrame f = new JFrame();
        Gameboard gameBoard = new Gameboard();
        f.addKeyListener(gameBoard);
        f.setBounds(30, 30, 405, 540);
        f.setContentPane(gameBoard);
        f.setResizable(false);
        f.setVisible(true);
        f.setTitle("Threes");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
