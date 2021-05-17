package Cave;
import javax.imageio.ImageIO;
import java.awt.*;


public class Rock extends CaveThings {
    public Rock(int x,int y) {
        this.x=x;
        this.y=y;
        height=90;
        width=70;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/MapObjects/Rock2.png"));
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
