
package Cave;
import Cave.CaveThings;
import Forest.ForestThings;

import javax.imageio.ImageIO;
import java.awt.*;

public class MagicalTree extends CaveThings {

    public MagicalTree(int x,int y)
    {
        this.x=x;
        this.y=y;
        height=120;
        width=200;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/MapObjects/MagicalTree2.png"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void draw(Graphics2D g)
    {
        super.draw(g);
    }
}
