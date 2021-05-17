package Entity;


import Entity.MapObject;
import Forest.ForestThings;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Trap extends MapObject{

    private BufferedImage image;
    public Trap(TileMap tm)
    {
        super(tm);
        height=16;
        width=48;
        cheight=15;
        cwidth=15;
        fallSpeed=0.2;
        maxFallSpeed=10.0;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/MapObjects/Trap.png"));
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
                (int) (y + ymap - height / 2),
                width
                ,height,
                null
        );
    }
}

