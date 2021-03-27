package Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Mushroom extends Enemy {

    private BufferedImage[]sprites;
    public Mushroom(TileMap tm)
    {
        super(tm);

        moveSpeed=0.3;
        maxSpeed=0.3;
        fallSpeed=0.2;
        maxFallSpeed=10.0;
        width=150;
        height=150;
        cwidth=20;
        cheight=15;

        health=maxHealth=2;
        damage=1;
        try{
            BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Mushroom.png"));
            sprites=new BufferedImage[11];
            for(int i=0;i<sprites.length;i++)
            {
                sprites[i]=spritesheet.getSubimage(i*width,0,width,height);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        animation=new Animation();
        animation.setFrames(sprites);
        animation.setDelay(100);
        left=false;
        right=false;

    }
    private void getNextPosition()
    {
        // movement
        if(left) {
            dx -= moveSpeed;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        else if(right) {
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
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
        //check flinching

        if(flinching)
        {
            long elapsed=(System.nanoTime()-flinchTimer)/1000000;
            if(elapsed>400)
            {
                flinching=false;
            }
        }
        //if it hits a wall go other direction{
            if(right && dx==0)
            {
                right=false;
                left=true;
                facingRight=false;
            }
            else if(left&& dx==0)
            {
                right=true;
                left=false;
                facingRight=true;
            }
            animation.update();
    }
    public void draw(Graphics2D g)
    {
        if(notOnScreen()){return;}
        setMapPosition();
        super.draw(g);
    }
}
