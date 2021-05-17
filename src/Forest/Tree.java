package Forest;
import javax.imageio.ImageIO;
import java.awt.*;

public class Tree extends ForestThings {

    public Tree(int x,int y) {
        this.x=x;
        this.y=y;
        height=112;
        width=112;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/MapObjects/tree.png"));
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
