package Entity.Enemies;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Mushroom extends Enemy {
    private AudioPlayer sfx;
    private BufferedImage[]sprites;
    private Player myPlayer;

    public Mushroom(TileMap tm,Player p)
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
        myPlayer=p;

        health=maxHealth=5;
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
        sfx=new AudioPlayer("/SFX/Bomb-Explosion.mp3");



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
        //check flinching
        if(myPlayer.getx()>this.x)
        {
            facingRight=true;
        }

        if(animation.hasPlayedOnce())
        {
            MushroomProjectile mp;
            if(abs(this.x-myPlayer.getx())<150) {

                if(facingRight) {
                  mp = new MushroomProjectile(tileMap, true,myPlayer);

                }
                else
                {
                  mp = new MushroomProjectile(tileMap, false,myPlayer);

                }

                mp.setPosition(x, y);
                Projectile.projectiles.add(mp);
                animation.setPlayedOnce(false);
            }

        }

        if(flinching)
        {

            long elapsed=(System.nanoTime()-flinchTimer)/1000000;
            if(elapsed>400)
            {
                flinching=false;
            }
        }

            if(dead)
            {
                sfx.play();
            }
            animation.update();
    }
    public void draw(Graphics2D g)
    {
       // if(notOnScreen()){return;}
        if(flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0) {
                return;
            }
        }

        setMapPosition();
        super.draw(g);
    }
}
