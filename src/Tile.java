import java.awt.*;

/**
 * Created by Sirozha on 04.11.2015.
 */

public class Tile{
    int val;
    int x, y;
    int newx, newy;
    Image sprite;

    Tile(){
        val = 0;
    }

    /**
     * Устанавливает значение переменной val
     * @param val1
     */
    void setVal(int val1){
        val = val1;
        if(val != 0) {
            String path = "/" + val + ".png";
            sprite = Toolkit.getDefaultToolkit().getImage(getClass().getResource(path));
        }
    }

    void setXY(int x1, int y1){
        newx = x = x1*100;
        newy = y = y1*100;
    }

    void setNewX(int nx){
        newx = nx * 100;
    }

    void setNewY(int ny){
        newy = ny * 100;
    }

    int getVal(){
        return val;
    }

    void animate(){
        if(val == 0)
            return;
        if(x < newx)
            x += 10;
        if(x > newx)
            x -= 10;
        if(y < newy)
            y += 10;
        if(y > newy)
            y -= 10;
    }

}
