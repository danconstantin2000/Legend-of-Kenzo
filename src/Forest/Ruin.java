package Forest;


import Forest.ForestThings;

import javax.imageio.ImageIO;
import java.awt.*;

public class Ruin extends ForestThings {
    public Ruin(int x,int y)
    {

        this.x=x;
        this.y=y;
        height=30;
        width=40;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/MapObjects/ruin.png"));
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