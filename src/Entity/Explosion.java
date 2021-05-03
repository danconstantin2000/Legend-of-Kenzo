package Entity;

import Audio.AudioPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

//Clasa pentru explozia care apare la uciderea unui inamic.
public class Explosion {

    private int x;
    private int y;
    private int xmap;
    private int ymap;

    private int width;
    private int height;

    private Animation animation;
    private BufferedImage[] sprites;

    private boolean remove;


    public Explosion(int x,int y)
    {
        this.x=x;
        this.y=y;
        width=30;
        height=30;
        try{
            //citire spitesheet
            BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/explosion.gif"));
            sprites=new BufferedImage[6];
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
        //update animatie
        animation.update();
        //Daca animatia s-a termiat,explozia dispare
        if(animation.hasPlayedOnce())
        {
            remove=true;
        }
    }
    public boolean shouldRemove()
    {
        return remove;
    }
    public void setMapPosition(int x,int y)
    {
        xmap=x;
        ymap=y;
    }
    public void draw(Graphics2D g)
    {
        g.drawImage(animation.getImage(),x+xmap-width/2,y+ymap-height/2-17,null);
    }




}
