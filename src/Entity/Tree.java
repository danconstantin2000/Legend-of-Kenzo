package Entity;


import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Tree  {

    private BufferedImage image;
    private int height;
    private int weight;
    private double x;
    private double y;
    private double dx;
    private double dy;

    public Tree() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/MapObjects/tree.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPosition(double x,double y)
    {
        this.x=x;
        this.y=x;
    }
    public void draw(Graphics2D g) {

        g.drawImage(image, (int) x, (int) y, null);
    }


}
