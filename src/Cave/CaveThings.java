package Cave;

import Main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class CaveThings {

    protected BufferedImage image;
    protected int x;
    protected int y;
    protected int xmap;
    protected int ymap;
    protected int width;
    protected int height;
    public void setMapPosition(int x,int y) {
        xmap=x;
        ymap=y;
    }
    public void draw(Graphics2D g) {
        if(notOnScreen())
        {
            return;
        }
        else {
            g.drawImage(image, x + xmap - width / 2, y + ymap - height / 2 + 5, null);
        }
    }
    public boolean notOnScreen() {
        return x + xmap + width < 0 ||
                x + xmap - width > GamePanel.WIDTH ||
                y + ymap + height < 0 ||
                y + ymap - height > GamePanel.HEIGHT;
    }

}
