import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Created by Sirozha on 03.11.2015.
 */
public class Gameboard extends JPanel
                       implements KeyListener{
   // int[][] board; // Массив хранящий расположение чисел на доске
    Tile[][] board;
    int cell_height = 100; // размеры клеточки с числом
    int cell_width = 100;
    int yoffset = 110; // Отступ от верха
    Tile next; // Следующее число
    boolean gameover = false; // Конец игры
    boolean exit = false;   // Выход
    boolean start = false;  // Запуск игры

    Gameboard(){
    }

    /*
    *   Генерирует начальное состояние игрового поля
    *   И задает начальные значения некоторых флагов
    */
    public void start(){
        start = true;
        gameover = false;
        board = new Tile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = new Tile();
                board[i][j].setVal(0);
            }
        }
        Random random = new Random();
        int x = random.nextInt(4);
        int y = random.nextInt(4);
        board[x][y] = new Tile();
        board[x][y].setVal(random.nextInt(2)+1);
        next = new Tile();
        next.setVal(random.nextInt(2)+1);
    }



    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(start) {
            paintGameBoard(g);
            paintNext(g);
        }
        else{
            if(!gameover)
                paintInvitation(g);
            else {
                paintGameBoard(g);
                paintNext(g);
                paintEndGame(g);
            }
        }
    }

    public void paintEndGame(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.white);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2d.fillRect(0, 0, 400, 540);
        g.setColor(Color.black);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g2d.drawString("You've lost!", 15, 200);
        g2d.drawString("Press space to play!", 15, 240);
    }

    public void paintInvitation(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g2d.drawString("Press space to play!", 15, 200);
    }

    /***
     * Рисует игровое поле
     * @param g
     */
    public void paintGameBoard(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g2d.setPaint(Color.white);
                Rectangle2D rect = new Rectangle2D.Double(j*cell_width, i*cell_height+yoffset, cell_width, cell_height);
                g2d.fill(rect);
                g2d.setColor(Color.black);
                g2d.draw(rect);
                if(board[i][j].val != 0)
                    g2d.drawImage(board[i][j].sprite, j*cell_width, i*cell_height+yoffset, cell_width, cell_height, this);
            }
        }
    }

    public void paintNext(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.white);
        Rectangle2D rect = new Rectangle2D.Double(150, 5, cell_width, cell_height);
        g2d.fill(rect);
        g2d.setColor(Color.black);
        g2d.draw(rect);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g2d.drawString("NEXT:", 50, cell_height/2);
        g2d.drawImage(next.sprite, 150, 5, cell_width, cell_height, this);
    }


    /***
     * Склеивает два числа в одно
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param d
     */
    void merge(int x, int y, int x1, int y1, char d){
        int val;

        if(board[x][y] == board[x1][y1])
            val = board[x][y].val * 2;
        else
            val = board[x][y].val + board[x1][y1].val;
        board[x][y].setVal(val); board[x1][y1].setVal(0);
    }

    /***
     * Сдвиг чисел влево
     */
    void move_left(){
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if((board[i][j].val == board[i][j+1].val && board[i][j].val > 2) || (board[i][j].val + board[i][j+1].val == 3) || board[i][j].val == 0) {
                    merge(i, j, i, j + 1, 'l');
                    k++;
                }
            }
        }
        if(k > 0)
            generate('l');
    }

    /***
     * Сдвиг чисел вправо
     */
    void move_right(){
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j > 0; j--) {
                if((board[i][j].val == board[i][j-1].val && board[i][j].val > 2) || (board[i][j].val + board[i][j-1].val == 3) || board[i][j].val == 0){
                    merge(i, j, i, j-1, 'r');
                    k++;
                }
            }
        }
        if(k > 0)
            generate('r');
    }

    /***
     * Сдвиг чисел вверх
     */
    void move_up(){
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if((board[i][j].val == board[i+1][j].val && board[i][j].val > 2) || (board[i][j].val + board[i+1][j].val == 3) || board[i][j].val == 0) {
                    merge(i, j, i + 1, j, 'u');
                    k++;
                }
            }
        }
        if(k > 0)
            generate('u');
    }

    /***
     * Сдвиг чисел вниз
     */
    void move_down(){
        int k = 0;
        for (int i = 3; i > 0; i--) {
            for (int j = 0; j < 4; j++) {
                if((board[i][j].val == board[i-1][j].val && board[i][j].val > 2) || (board[i][j].val + board[i-1][j].val == 3) || board[i][j].val == 0) {
                    merge(i, j, i - 1, j, 'd');
                    k++;
                }
            }
        }
        if(k > 0)
            generate('d');
    }

    /***
     * Генерирует новое число
     * @param d - в какую сторону был произведен сдвиг
     */
    void generate(char d){
        boolean generated = false;
        Random random = new Random();
        switch(d){
            case 'l': ;
                while(!generated){
                    int y = random.nextInt(4);
                    if(board[y][3].val == 0){
                        board[y][3].setVal(next.val);
                        generated = true;
                    }
                }
            case 'r':
                while(!generated){
                    int y = random.nextInt(4);
                    if(board[y][0].val == 0){
                        board[y][0].setVal(next.val);
                        generated = true;
                    }
                }
            case 'u':
                while(!generated) {
                    int x = random.nextInt(4);
                    if (board[3][x].val == 0) {
                        board[3][x].setVal(next.val);
                        generated = true;
                    }
                }
            case 'd':
                while(!generated) {
                    int x = random.nextInt(4);
                    if (board[0][x].val == 0) {
                        board[0][x].setVal(next.val);
                        generated = true;
                    }
                }
        }
        int next1;
        while((next1 = random.nextInt(3) + 1) == next.val);
        next.setVal(next1);
        check_endgame();
    }

    void check_endgame(){
        boolean flag = true;
        for (int i = 0; i < 3 && flag; i++) {
            for (int j = 0; j < 4 && flag; j++) {
                if((board[i][j].val == board[i+1][j].val && board[i][j].val > 2) || (board[i][j].val + board[i+1][j].val == 3) || board[i][j].val == 0)
                    flag = false;
            }
        }
        for (int i = 0; i < 4 && flag; i++) {
            for (int j = 0; j < 3 && flag; j++) {
                if((board[i][j].val == board[i][j+1].val && board[i][j].val > 2) || (board[i][j].val + board[i][j+1].val == 3) || board[i][j].val == 0)
                    flag = false;
            }
        }
        gameover = flag;
        start = !flag;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(start){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT: move_left(); repaint(); break;
                case KeyEvent.VK_RIGHT: move_right(); repaint(); break;
                case KeyEvent.VK_UP: move_up(); repaint(); break;
                case KeyEvent.VK_DOWN: move_down(); repaint(); break;
            }
        }
        else
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                start();
                repaint();
            }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exit = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


}
