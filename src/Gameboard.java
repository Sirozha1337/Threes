import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;


/**
 * Created by Sirozha on 03.11.2015.
 */
public class Gameboard extends JPanel
                       implements KeyListener, ActionListener{
    /**
     * Массив хранящий расположение тайлов на доске
     */
    Tile[][] board;

    /**
     * Высота тайла
     */
    int cell_height = 100;

    /**
     * Ширина тайла
     */
    int cell_width = 100;

    /**
     * Отступ от верха окна
     */
    int yoffset = 110;

    /**
     * Следующее число
     */
    Tile next;

    /**
     * Конец игры
     */
    boolean gameover = false;

    /**
     * Запуск игры
     */
    boolean start = false;

    /**
     * Таймер для анимации, 60 кадров в секунду
     */
    Timer timer = new Timer(1000/60, this);

    /**
     * Ширина окна
     */
    int w = 405;

    /**
     * Высота окна
     */
    int h = 540;

    /**
     * Буффер для двойной буфферизации
     */
    static Image  buffer = null;

    Gameboard(){
    }

    /**
    *   Генерирует начальное состояние игрового поля
    *   и задает начальные значения некоторых флагов
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
    public void update(Graphics g){
        paint(g);
    }

    /**
     * Отрисовывает игровое поле
     * @param g
     */
    @Override
    public void paint(Graphics g){
        super.paintComponent(g);
        buffer = createImage(w, h);
        Graphics screen = buffer.getGraphics(); // Двойная буфферизация
        screen.setColor(Color.white);
        screen.fillRect(0, 0, w, h);

        // Если игра не начата и не был достигнут конец игры, рисуем приглашение
        if(!start && !gameover)
             paintInvitation(screen);
        // Иначе рисуем игровое поле
        else {
            paintGameBoard(screen);
            paintTiles(screen);
            paintNext(screen);
        }
        // Если был достигнут конец игры, то рисуем предложение сыграть ещё
        if(gameover)
            paintEndGame(screen);
        g.drawImage(buffer, 0, 0, null);
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

    /**
     * Рисует таблицу
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

    /**
     * Склеивает два тайла в один
     * @param x координата первого тайла
     * @param y координата первого тайла
     * @param x1 координата второго тайла
     * @param y1 координата второго тайла
     */
    void merge(int y, int x, int y1, int x1){
        int val = board[y][x].getVal() + board[y1][x1].getVal(); // Считаем значение переменной val для получившегося тайла
        board[y][x].setXY(x1, y1); // Выставляем ему координаты для отрисовки
        board[y][x].setVal(val);   // И новое значение
        board[y1][x1].setVal(0);   // Обнуляем его предыдущее местоположение
        board[y][x].setNewX(x);    // Выставляем координаты для анимации
        board[y][x].setNewY(y);
    }


    /**
     * Сдвиг тайлов влево
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
        // Если хоть один тайл был сдвинут, генерируем новый тайл
        if(k > 0)
            generate('l');
    }

    /**
     * Сдвиг тайлов вправо
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
        // Если хоть один тайл был сдвинут, генерируем новый тайл
        if(k > 0)
            generate('r');
    }

    /**
     * Сдвиг тайлов вверх
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
        // Если хоть один тайл был сдвинут, генерируем новый тайл
        if(k > 0)
            generate('u');
    }

    /**
     * Сдвиг тайлов вниз
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
        // Если хоть один тайл был сдвинут, генерируем новый тайл
        if(k > 0)
            generate('d');
    }

    /**
     * Генерирует новый тайл, в зависимости от того в какую сторону был произведен сдвиг
     * @param d в какую сторону был произведен сдвиг, 'l' - влево. 'r' - вправо. 'u' - вверх. 'd' - вниз.
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
                        board[y][3].setXY(4, y);
                        board[y][3].setNewX(3);
                        board[y][3].setNewY(y);
                        generated = true;
                    }
                }
            case 'r':
                while(!generated){
                    int y = random.nextInt(4);
                    if(board[y][0].getVal() == 0){
                        board[y][0].setVal(next.getVal());
                        board[y][0].setXY(-1, y);
                        board[y][0].setNewX(0);
                        board[y][0].setNewY(y);
                        generated = true;
                    }
                }
            case 'u':
                while(!generated) {
                    int x = random.nextInt(4);
                    if (board[3][x].getVal() == 0) {
                        board[3][x].setVal(next.getVal());
                        board[3][x].setXY(x, 4);
                        board[3][x].setNewX(x);
                        board[3][x].setNewY(3);
                        generated = true;
                    }
                }
            case 'd':
                while(!generated) {
                    int x = random.nextInt(4);
                    if (board[0][x].getVal() == 0) {
                        board[0][x].setVal(next.getVal());
                        board[0][x].setXY(x, -1);
                        board[0][x].setNewX(x);
                        board[0][x].setNewY(0);
                        generated = true;
                    }
                }
        }
        /* Генерация следующего тайла */
        int next1;
        while((next1 = random.nextInt(3) + 1) == next.getVal()); // Генерирует значение следующего тайла так, чтобы оно не было равно предыдущему
        next.setVal(next1);
        timer.start();
        check_endgame();
    }

    /**
     * Проверка произошел ли конец игры
     */
    void check_endgame(){
        boolean flag = true;
        // Проверяем можно ли соединить тайл[i][j] с тайлом стоящим справа от него
        for (int i = 0; i < 3 && flag; i++) {
            for (int j = 0; j < 4 && flag; j++) {
                if((board[i][j].getVal() == board[i+1][j].getVal() && board[i][j].getVal() > 2) || (board[i][j].getVal() + board[i+1][j].getVal() == 3) || board[i][j].getVal() == 0)
                    flag = false;
            }
        }
        // Проверяем можно ли соединить тайл[i][j] с тайлом стоящим снизу от него
        for (int i = 0; i < 4 && flag; i++) {
            for (int j = 0; j < 3 && flag; j++) {
                if((board[i][j].getVal() == board[i][j+1].getVal() && board[i][j].getVal() > 2) || (board[i][j].getVal() + board[i][j+1].getVal() == 3) || board[i][j].getVal() == 0)
                    flag = false;
            }
        }
        // На основании этих проверок выставляем необходимые флаги
        gameover = flag;
        start = !flag;
    }

    /**
     * Обработка событий клавиатуры
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Если игра начата
        if(start){
            // Сдвигаем тайлы в зависимости от того, какая стрелочка была нажата
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT: move_left(); break;
                case KeyEvent.VK_RIGHT: move_right(); break;
                case KeyEvent.VK_UP: move_up(); break;
                case KeyEvent.VK_DOWN: move_down(); break;
            }
        }
        else
        // Иначе начинаем игру при нажатии кнопки 'Space'
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                start();
            }
        // При нажатии кнопки 'Esc' окно закрывается
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Каждые 1000/60 миллисекунд изменяет координаты тайлов для анимации
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int timer_stopper = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(board[i][j].animate())
                    timer_stopper++;
            }
        }
        repaint();
        if(timer_stopper == 16) {
            timer.stop();
        }
    }
}
