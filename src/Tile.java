import java.awt.*;

/**
 * Created by Sirozha on 04.11.2015.
 */
public class Tile {
    int val;
    Image sprite;

    Tile(){
        val = 0;
    }

    void setVal(int val1){
        val = val1;
        if(val != 0) {
            String path = "/" + val + ".png";
            sprite = Toolkit.getDefaultToolkit().getImage(getClass().getResource(path));
        }
    }

}
