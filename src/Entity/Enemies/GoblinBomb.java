package Entity.Enemies;

import Entity.Animation;
import Entity.MapObject;
import Entity.Player;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import static java.lang.Math.abs;

public class GoblinBomb  extends Projectile{

    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;
    private Player myPlayer;
    private int damage;
    private int health;
    private double startpoz;

    public GoblinBomb(TileMap tm, boolean right,Player p,double x)
    {

        super(tm);
        moveSpeed=2.2;
        facingRight=right;
        if(right)
        {
            dx=moveSpeed;

        }
        else
        {
            dx=-moveSpeed;
        }
        width=100;
        height=100;
        cwidth=14;
        cheight=1;
        fallSpeed=0.2;
        health=1;
        damage=2;
        myPlayer=p;
        startpoz=x;
        try
        {
            BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/GoblinBombs.png"));
            sprites=new BufferedImage[9];
            hitSprites=new BufferedImage[9];
            for(int i=0;i<sprites.length;i++)
            {
                sprites[i]=spritesheet.getSubimage(i*width,0,width,height);
            }
            for(int i=0;i<hitSprites.length;i++)
            {
                hitSprites[i]=spritesheet.getSubimage(i*width,height,width,height);
            }
            animation=new Animation();
            animation.setFrames(sprites);
            animation.setDelay(100);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public int getDamage()
    {
        return damage;
    }

    public void hit(int damage)
    {
        health-=damage;
        if(health<0){health=0;}
        if(health==0)setHit();
    }
    private void getNextPosition()
    {

    }

    public void setHit()
    {
        if(hit)return;
        hit=true;
        animation.setFrames(hitSprites);
        animation.setDelay(120);
        dx=0;

    }
    public boolean shouldRemove(){return remove;}
    public void update()
    {

        checkTileMapCollision();
        setPosition(xtemp,ytemp);
        getNextPosition();
        if(dx==0 && !hit)
        {
            setHit();
        }

        if(hit &&animation.hasPlayedOnce())
        {
            remove=true;

        }
        if(x<0)
        {
            x=0;
            setHit();
        }


        if(abs(x-startpoz)>50)
        {
            if(falling)
            {
                dy+=fallSpeed;
            }
        }

        animation.update();

    }
    public void draw(Graphics2D g)
    {
        setMapPosition();
        if (facingRight) {
            g.drawImage(
                    animation.getImage(),
                    (int) (x + xmap - width / 2),
                    (int) (y + ymap - height / 2),
                    null
            );
        }
        else
        {

            g.drawImage(
                    animation.getImage(),
                    (int) (x + xmap - width / 2 + width),
                    (int) (y + ymap - height / 2  ),
                    -width,
                    height,
                    null
            );

        }
    }


}
