package Entity.Enemies;
import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import static java.lang.Math.abs;

public class Skeleton extends Enemy {
    private AudioPlayer sfx;
    private BufferedImage[]sprites;
    private Player myPlayer;
    public Skeleton(TileMap tm,Player p) {
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
        Score=500;
        health=maxHealth=5;
        damage=1;
        try{
            BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Skeleton.png"));
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
        animation=new Animation();
        animation.setFrames(sprites);
        animation.setDelay(150);
        left=false;
        right=false;
        sfx=new AudioPlayer("/SFX/Bomb-Explosion.mp3");
    }
    private void getNextPosition() {
        if(falling)
        {
            dy+=fallSpeed;

        }
    }
    private void faceByPlayerPoz() {
        if(myPlayer.getx()>this.x)
        {
            facingRight=true;
        }
        else
        {
            facingRight=false;
        }
    }
    private void startAttack() {
        if(abs(this.x-myPlayer.getx())<200) {
            animation.update();
            if (animation.hasPlayedOnce()) {
                SkeletonProj sp;

                if (facingRight) {
                    sp = new SkeletonProj(tileMap, true, myPlayer);

                } else {
                    sp = new SkeletonProj(tileMap, false, myPlayer);

                }

                sp.setPosition(x, y);
                Projectile.projectiles.add(sp);
                animation.setPlayedOnce(false);

            }
        }
    }
    private void turnOffFlinching(){
        if(flinching)
        {

            long elapsed=(System.nanoTime()-flinchTimer)/1000000;
            if(elapsed>400)
            {
                flinching=false;
            }
        }
    }
    public void update() {

        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);
        faceByPlayerPoz();
        startAttack();
        turnOffFlinching();
        if(dead)
        {
            sfx.play();
        }

    }
    public void draw(Graphics2D g) {
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
