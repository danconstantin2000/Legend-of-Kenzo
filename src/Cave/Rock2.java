package Cave;
import javax.imageio.ImageIO;
import java.awt.*;

public class Rock2 extends CaveThings {

    public Rock2(int x,int y) {
        this.x=x;
        this.y=y;
        height=60;
        width=160;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/MapObjects/Rock3.png"));
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
