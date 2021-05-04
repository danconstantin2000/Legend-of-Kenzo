package Entity;

import Entity.MapObject;
import Forest.ForestThings;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class KenzoHead extends MapObject{

    private BufferedImage image;
    private boolean remove;
    public KenzoHead(TileMap tm)
    {
        super(tm);
        height=20;
        width=32;
        cheight=15;
        cwidth=15;
        fallSpeed=0.2;
        maxFallSpeed=10.0;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/MapObjects/Kenzo_Head2.png"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    private void getNextPosition()
    {
        // movement
        if(falling)
        {
            dy+=fallSpeed;

        }
    }
    public void setRemove(boolean b)
    {
        remove=b;
    }
    public boolean shouldRemove()
    {
        return remove;
    }

    public void update()
    {
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);
    }


    public void draw(Graphics2D g)
    {
        setMapPosition();

        g.drawImage(
                image,
                (int) (x + xmap - width / 2),
                (int) (y + ymap - height / 2-17),
                width
                ,height,
                null
        );
    }
}

