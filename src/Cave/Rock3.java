package Cave;
import javax.imageio.ImageIO;
import java.awt.*;

public class Rock3 extends CaveThings {

    public Rock3(int x,int y) {
        this.x=x;
        this.y=y;
        height=90;
        width=120;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/MapObjects/Rock4.png"));
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
