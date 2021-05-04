package Entity;

import Entity.Enemies.MushroomProjectile;
import Entity.Enemies.Projectile;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class Portal extends MapObject{
    private BufferedImage[]sprites;
    public Portal(TileMap tm)
    {
        super(tm);
        width=64;
        height=64;
        try{
            //citire spitesheet
            BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Portal.png"));
            sprites=new BufferedImage[8];
            for(int i=0;i<sprites.length;i++)
            {
                sprites[i]=spritesheet.getSubimage(i*width,0,width,height);

            }
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
        //setare animatie
        animation=new Animation();
        animation.setFrames(sprites);
        animation.setDelay(100);

    }

    public void update()
    {

        checkTileMapCollision();
        setPosition(xtemp,ytemp);
        animation.update();


    }
    public void draw(Graphics2D g)
    {
        setMapPosition();
        super.draw(g);
    }
}
