import java.awt.*;

/**
 * Created by Sirozha on 04.11.2015.
 */

public class Tile{
    /**
     * Значение тайла
     */
    int val;

    /**
     * Координаты для отрисовки спрайта
     */
    int x;

    /**
     * Координаты для отрисовки спрайта
     */
    int y;

    /**
     * Новые координаты для анимации
     */
    int newx;

    /**
     * Новые координаты для анимации
     */
    int newy;

    /**
     * Спрайт для отрисовки
     */
    Image sprite;

    /**
     * Массив спрайтов
     */
    static Image[] sprites;

    /**
     * Открывает спрайты тайлов и записывает их в массив
     */
    static{
        int m = 0;
        sprites = new Image[11];
        for (int i = 0; i < 11; i++) {
            if(i < 3)
                m = m + 1;
            else
                m = m * 2;
            String path = "/" + m + ".png";
            sprites[i] = Toolkit.getDefaultToolkit().getImage(Tile.class.getResource(path));
        }
    }

    Tile(){
        val = 0;
    }

    /**
     * Устанавливает значение переменной val
     * @param val1 новое значение перменной val
     */
    void setVal(int val1){
        val = val1;
        setSprite();
    }

    /**
     * На основании переменнной val, подбирает спрайт для тайла
     */
    void setSprite(){
        switch(val){
            case 1: sprite = sprites[0]; break;
            case 2: sprite = sprites[1]; break;
            case 3: sprite = sprites[2]; break;
            case 6: sprite = sprites[3]; break;
            case 12: sprite = sprites[4]; break;
            case 24: sprite = sprites[5]; break;
            case 48: sprite = sprites[6]; break;
            case 96: sprite = sprites[7]; break;
            case 192: sprite = sprites[8]; break;
            case 384: sprite = sprites[9]; break;
            case 768: sprite = sprites[10]; break;
            default:  break;
        }
    }

    /**
     * Установить начальные координаты тайла
     * @param x1
     * @param y1
     */
    void setXY(int x1, int y1){
        newx = x = x1*100;
        newy = y = y1*100;
    }

    /**
     * Установить новую координату x
     * @param nx
     */
    void setNewX(int nx){
        newx = nx * 100;
    }

    /**
     * Установить новую координату y
     * @param ny
     */
    void setNewY(int ny){
        newy = ny * 100;
    }

    /**
     * Получить значение переменной val
     * @return val
     */
    int getVal(){
        return val;
    }


    /**
     * Изменение координат до тех пор пока они не станут равны новым координатам
     * @return true в случае если координаты данного тайла менять не нужно, в обратном случае false
     */
    boolean animate(){
        if(val == 0)
            return true;
        if(x < newx)
            x += 10;
        if(x > newx)
            x -= 10;
        if(y < newy)
            y += 10;
        if(y > newy)
            y -= 10;
        if(x == newx && y == newy)
            return true;
        else
            return false;
    }

}
