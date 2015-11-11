import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by Sirozha on 03.11.2015.
 */
public class Gameboard extends JPanel
                       implements KeyListener, ActionListener{
    Tile[][] board; // Массив хранящий расположение тайлов на доске
    int cell_height = 100; // размеры тайла
    int cell_width = 100;
    int yoffset = 110; // Отступ от верха окна
    Tile next; // Следующее число
    boolean gameover = false; // Конец игры
    boolean exit = false;   // Выход
    boolean start = false;  // Запуск игры
    Timer timer = new Timer(1000/60, this); // Таймер для анимации, 60 кадров в секунду

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
        board[x][y].setXY(y, x);
        next = new Tile();
        next.setVal(random.nextInt(2)+1);
        timer.start();
    }



    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(start) {
            paintGameBoard(g);
            paintTiles(g);
            paintNext(g);
        }
        else{
            if(!gameover)
                paintInvitation(g);
            else {
                paintGameBoard(g);
                paintTiles(g);
                paintNext(g);
                paintEndGame(g);
            }
        }
    }

    /***
     * Отрисовывает предложение сыграть ещё
     * @param g
     */
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

    /**
     * Отрисовывает предложение начать игру
     * @param g
     */
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
            }
        }
    }

    /**
     * Рисует тайлы с цифрами
     * @param g
     */
    public void paintTiles(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(board[i][j].getVal() != 0)
                    g2d.drawImage(board[i][j].sprite, board[i][j].x, board[i][j].y+yoffset, cell_width, cell_height, this);
            }
        }
    }

    /**
     * Рисует следующий тайл над игровым полем
     * @param g
     */
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
     * Склеивает два тайла в один
     * @param x - координата первого тайла
     * @param y - координата первого тайла
     * @param x1 - координата второго тайла
     * @param y1- координата второго тайла
     */
    void merge(int y, int x, int y1, int x1){
        int val;
        val = board[y][x].getVal() + board[y1][x1].getVal();
        board[y][x].setXY(x1, y1);
        board[y][x].setVal(val);
        board[y1][x1].setVal(0);
        board[y][x].setNewX(x);
        board[y][x].setNewY(y);
    }




    /***
     * Сдвиг чисел влево
     */
    void move_left(){
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if((board[i][j].getVal() == board[i][j+1].getVal() && board[i][j].getVal() > 2) || (board[i][j].getVal() + board[i][j+1].getVal() == 3 && board[i][j].getVal() != 3) || board[i][j].getVal() == 0) {
                    merge(i, j, i, j + 1);
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
                if((board[i][j].getVal() == board[i][j-1].getVal() && board[i][j].getVal() > 2) || (board[i][j].getVal() + board[i][j-1].getVal() == 3 && board[i][j].getVal() != 3) || board[i][j].getVal() == 0){
                    merge(i, j, i, j-1);
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
                if((board[i][j].getVal() == board[i+1][j].getVal() && board[i][j].getVal() > 2) || (board[i][j].getVal() + board[i+1][j].getVal() == 3 && board[i][j].getVal() != 3) || board[i][j].getVal() == 0) {
                    merge(i, j, i + 1, j);
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
                if((board[i][j].getVal() == board[i-1][j].getVal() && board[i][j].getVal() > 2) || (board[i][j].getVal() + board[i-1][j].getVal() == 3 && board[i][j].getVal() != 3) || board[i][j].getVal() == 0) {
                    merge(i, j, i - 1, j);
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
                    if(board[y][3].getVal() == 0){
                        board[y][3].setVal(next.getVal());
                        board[y][3].setXY(3, y);
                        generated = true;
                    }
                }
            case 'r':
                while(!generated){
                    int y = random.nextInt(4);
                    if(board[y][0].getVal() == 0){
                        board[y][0].setVal(next.getVal());
                        board[y][0].setXY(0, y);
                        generated = true;
                    }
                }
            case 'u':
                while(!generated) {
                    int x = random.nextInt(4);
                    if (board[3][x].getVal() == 0) {
                        board[3][x].setVal(next.getVal());
                        board[3][x].setXY(x, 3);
                        generated = true;
                    }
                }
            case 'd':
                while(!generated) {
                    int x = random.nextInt(4);
                    if (board[0][x].getVal() == 0) {
                        board[0][x].setVal(next.getVal());
                        board[0][x].setXY(x, 0);
                        generated = true;
                    }
                }
        }
        int next1;
        while((next1 = random.nextInt(3) + 1) == next.getVal());
        next.setVal(next1);
        check_endgame();
    }

    /***
     * Проверка произошел ли конец игры
     */
    void check_endgame(){
        boolean flag = true;
        for (int i = 0; i < 3 && flag; i++) {
            for (int j = 0; j < 4 && flag; j++) {
                if((board[i][j].getVal() == board[i+1][j].getVal() && board[i][j].getVal() > 2) || (board[i][j].getVal() + board[i+1][j].getVal() == 3) || board[i][j].getVal() == 0)
                    flag = false;
            }
        }
        for (int i = 0; i < 4 && flag; i++) {
            for (int j = 0; j < 3 && flag; j++) {
                if((board[i][j].getVal() == board[i][j+1].getVal() && board[i][j].getVal() > 2) || (board[i][j].getVal() + board[i][j+1].getVal() == 3) || board[i][j].getVal() == 0)
                    flag = false;
            }
        }
        gameover = flag;
        start = !flag;
    }

    /**
     * Обработка событий клавиатуры
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(start){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT: move_left(); break;
                case KeyEvent.VK_RIGHT: move_right(); break;
                case KeyEvent.VK_UP: move_up(); break;
                case KeyEvent.VK_DOWN: move_down(); break;
            }
        }
        else
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                start();
            }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exit = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Каждые x миллисекунд изменяем координаты тайлов для анимации
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j].animate();
            }
        }
        repaint();
    }
}
