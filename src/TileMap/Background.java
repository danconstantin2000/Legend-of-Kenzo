package TileMap;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

//Backgroun class-->Background pentru state-uri
public class Background {
    private BufferedImage image;
    private double x;
    private double y;
    public Background(String s )
    {
        try
        {
            image=ImageIO.read(getClass().getResourceAsStream(s));
            x=0;
            y=0;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void setPosition(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    public void update(){}
    public void draw(Graphics2D g)
    {
        g.drawImage(image,(int)x,(int)y,null);

    }

}
