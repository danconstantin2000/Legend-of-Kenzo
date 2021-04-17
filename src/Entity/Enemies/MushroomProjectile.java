package Entity.Enemies;

import Entity.Animation;
import Entity.MapObject;
import Entity.Player;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class MushroomProjectile extends MapObject {

    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;
    private Player myPlayer;
    private int damage;
    private int health;
    public MushroomProjectile(TileMap tm, boolean right, Player p)
    {

        super(tm);
        moveSpeed=1;
        facingRight=right;
        if(right)
        {
            dx=moveSpeed;

        }
        else
        {
            dx=-moveSpeed;
        }
        width=50;
        height=50;
        cwidth=14;
        cheight=14;
        myPlayer=p;
        health=1;

        try
        {
            BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/MushroomProj.png"));
            sprites=new BufferedImage[4];
            hitSprites=new BufferedImage[4];
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
             animation.setDelay(200);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void hit(int damage)
    {
        health-=damage;
        if(health<0){health=0;}
        if(health==0)setHit();
    }

    public void setHit()
    {
        if(hit)return;
        hit=true;
        animation.setFrames(hitSprites);
        animation.setDelay(70);
        dx=0;

    }
    public boolean shouldRemove(){return remove;}
    public void update()
    {
        checkTileMapCollision();
        setPosition(xtemp,ytemp);
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
